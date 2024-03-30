package it.nekotak.yi4kcam

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
