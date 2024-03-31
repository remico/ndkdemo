package it.nekotak.yi4kcam

/**
 * Helper class for ad-hoc testing the underlying c++ library behavior.
 * This class shouldn't be used in client applications.
 */
class Yi4KPlus(ip: String) {

    init {
        authenticate(ip)
    }

    private external fun authenticate(ip: String): Boolean;

    external fun startLivePreview();

    external fun stopLivePreview();

    companion object {
        // Used to load the 'yi4kcam' library on application startup.
        init {
            System.loadLibrary("yi4kcam")
        }
    }
}
