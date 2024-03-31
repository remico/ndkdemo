// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("ndkdemo");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("ndkdemo")
//      }
//    }
#include "ndkdemo.h"

#include <android/log.h>
#include <sstream>
#include <string>

#include "yi4kcam.h"

static const char *tag = "ndkdemo-cpp";

extern "C" jstring
Java_it_nekotak_ndkdemo_JNIWrapper_helloFromCpp(
        JNIEnv *env,
        jobject
) {
    // hex formatted JNI version
    std::stringstream ss;
    ss << std::hex << "0x000" << env->GetVersion();
    std::string jni_version_hex = ss.str();

    // build result string
    std::string hello = "Hello from C++: JNI version " + jni_version_hex;

    // use android NDK logger
    __android_log_print(ANDROID_LOG_VERBOSE, tag,
                        "helloFromCpp() call returns: \"%s\"", hello.c_str());

    return env->NewStringUTF(hello.c_str());
}

extern "C" jboolean
Java_it_nekotak_ndkdemo_JNIWrapper_authenticate(JNIEnv *env, jobject thiz, jstring ip) {
    const char *ip_utf = env->GetStringUTFChars(ip, NULL);
    if (ip_utf == NULL) JNI_FALSE;
    std::string camera_ip(ip_utf);
    env->ReleaseStringUTFChars(ip, ip_utf);
    return authenticate(camera_ip);
}

extern "C" void
Java_it_nekotak_ndkdemo_JNIWrapper_startLivePreview(JNIEnv *env, jobject thiz) {
    startLivePreview();
}

extern "C" void
Java_it_nekotak_ndkdemo_JNIWrapper_stopLivePreview(JNIEnv *env, jobject thiz) {
    stopLivePreview();
}
