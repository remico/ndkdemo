//
// Created by Roman Gladyshev on 29.03.2024.
//
#pragma once

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL
Java_it_nekotak_ndkdemo_MainActivity_helloFromCpp(
        JNIEnv *env,
        jobject /* this */
);

#ifdef __cplusplus
}
#endif
