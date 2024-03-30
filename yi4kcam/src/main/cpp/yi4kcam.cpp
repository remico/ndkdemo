#include "include/yi4kcam.h"

#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <errno.h>
#include <android/log.h>

#include <jni.h>

#define TAG "Yi4Cam"  // logging tag

using namespace std;

namespace {

static int token = -1;
static int sockfd = -1;

bool open_tcp_connection(string ip) {
    // Create a TCP socket
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Error creating socket: %s", strerror(errno));
        return false;
    }

    // Server address
    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(7878);
    if (inet_pton(AF_INET, ip.c_str(), &server_addr.sin_addr) <= 0) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Invalid server IP address: %s", ip.c_str());
        close(sock);
        return false;
    }

    // Connect to the server
    if (connect(sock, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Error connecting to server: %s", strerror(errno));
        close(sock);
        return false;
    }

    sockfd = sock;
    return true;
}

void close_tcp_connection() {
    close(sockfd);
    sockfd = -1;
}

bool sendRequest(string json) {
    // replace single quotes with double quotes as JSON format strictly needs double quoted strings,
    // while when hardcoding JSON strings within source code it's easier to use single quotes
    replace(json.begin(), json.end(), '\'', '"');
    __android_log_print(ANDROID_LOG_INFO, TAG, "Sending to camera: %s", json.data());
    ssize_t sent_bytes = send(sockfd, json.data(), json.size(), 0);
    if (sent_bytes < 0) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Error sending message to server: %s", strerror(errno));
        close(sockfd);
        return false;
    }
    return true;
}

string waitForResponse() {
    uint8_t buffer[1024];
    ssize_t received_bytes = recv(sockfd, buffer, sizeof(buffer), 0);
    if (received_bytes < 0) {
        close_tcp_connection();
        return "";
    }
    return string(reinterpret_cast<const char*>(buffer), received_bytes);  // json response
}

int parseAuthResponseForToken(string json) {
    // Find the position of "param" key
    size_t paramPos = json.find("\"param\":");
    if (paramPos == string::npos) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "JSON does not contain 'param' key");
        return -1; // Return error code
    }

    // Find the position of the value after the colon and any subsequent spaces
    size_t valueStartPos = json.find_first_not_of(" \t\r\n", paramPos + 8); // "param": is 8 characters long
    if (valueStartPos == std::string::npos) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Invalid JSON format");
        return -1; // Return error code
    }

    // Find the end position of the value
    size_t valueEndPos = json.find_first_of(",}", valueStartPos);
    if (valueEndPos == std::string::npos) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Invalid JSON format");
        return -1; // Return error code
    }

    // Extract and convert the "param" value substring to integer
    string paramValueStr = json.substr(valueStartPos, valueEndPos - valueStartPos);
    try {
        return stoi(paramValueStr);
    } catch (const exception& e) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Failed to convert 'param' value to integer: %s", e.what());
        return -1; // Return error code
    }
}

}

void authenticate(string camera_ip) {
    // cleanup resources if already open
    if (-1 != sockfd) close_tcp_connection();

    // establish a new connection
    if (!open_tcp_connection(camera_ip)) return;

    // ask for a session token
    sendRequest("{'token': 0, 'msg_id': 257}");

    // remember the session token
    token = parseAuthResponseForToken(waitForResponse());
    __android_log_print(ANDROID_LOG_INFO, TAG, "Token received: %i", token);
}

void startLivePreview() {
    // TODO: use valid token
    sendRequest("{'token': 8, 'msg_id': 259, 'param': 'none_force'}");
}

void stopLivePreview() {
    // TODO: use valid token
    sendRequest("{'token': 8, 'msg_id': 260}");
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_it_nekotak_yi4kcam_Yi4KPlus_authenticate(JNIEnv *env, jobject thiz, jstring ip) {
    const char *ip_utf = env->GetStringUTFChars(ip, NULL);
    if (ip_utf == NULL) JNI_FALSE;
    std::string camera_ip(ip_utf);
    env->ReleaseStringUTFChars(ip, ip_utf);
    authenticate(camera_ip);
    return JNI_TRUE;
}
