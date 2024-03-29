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
#include "include/ndkdemo.h"

#include <android/log.h>
#include <sstream>
#include <string>

static const char *tag = "ndkdemo-cpp";

extern "C"
jstring
Java_it_nekotak_ndkdemo_MainActivity_helloFromCpp(
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
