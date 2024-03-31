//
// Created by Roman Gladyshev on 29.03.2024.
//
#pragma once

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL
Java_it_nekotak_ndkdemo_JNIWrapper_helloFromCpp(
        JNIEnv *env,
        jobject /* this */
);

JNIEXPORT jboolean JNICALL
Java_it_nekotak_ndkdemo_JNIWrapper_authenticate(JNIEnv *env, jobject thiz, jstring ip);

JNIEXPORT void JNICALL
Java_it_nekotak_ndkdemo_JNIWrapper_startLivePreview(JNIEnv *env, jobject thiz);

JNIEXPORT void JNICALL
Java_it_nekotak_ndkdemo_JNIWrapper_stopLivePreview(JNIEnv *env, jobject thiz);

#ifdef __cplusplus
}
#endif
