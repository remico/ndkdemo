package it.nekotak.ndkdemo

class JNIWrapper {

    external fun helloFromCpp(): String

    external fun authenticate(ip: String): Boolean

    external fun startLivePreview()

    external fun stopLivePreview()

    companion object {
        init {
            System.loadLibrary("ndkdemo")
        }
    }
}
