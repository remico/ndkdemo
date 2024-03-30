#pragma once

#include <string>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * This function must be called before any interaction with Yi4K+ action camera.
 */
void authenticate(std::string camera_ip);

/**
 * Starts a live video preview stream available via `rtsp://\<CAMERA_IP_ADDRESS\>/live`.
 */
void startLivePreview();

/**
 * Cancels live video preview streaming.
 */
void stopLivePreview();

#ifdef __cplusplus
}
#endif
