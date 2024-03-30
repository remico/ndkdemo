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
    const char *data = json.c_str();
    ssize_t sent_bytes = send(sockfd, data, strlen(data), 0);
    if (sent_bytes < 0) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "Error sending message to server: %s", strerror(errno));
        close(sockfd);
        return false;
    }
    return true;
}

string waitForResponse() {
    return "";  // json response
}

int parseAuthResponse(string json) {
    return 0;
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
    // {'rval': 0, 'msg_id': 257, 'param': 5, 'model': 'Z18', 'rtsp': 'rtsp://192.168.42.1/live'}
    token = parseAuthResponse(waitForResponse());
}

void startLivePreview() {
    // TODO: use valid token
    sendRequest("{'token': 8, 'msg_id': 259, 'param': 'none_force'}");
}

void stopLivePreview() {
    // TODO: use valid token
    sendRequest("{'token': 8, 'msg_id': 260}");
}
