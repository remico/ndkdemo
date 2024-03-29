# Include prebuilt native libraries

If you want Gradle to package prebuilt native libraries that are not used in any external native build,
add them to the `src/main/jniLibs/<ANDROID_ABI>` directory of your module.

Note:
If you are using Android Gradle Plugin 4.0 or above, move any libraries that are used 
by `IMPORTED` CMake targets out of your `jniLibs` directory to avoid the following error:
    `> More than one file was found with OS independent path 'lib/x86/xxx.so'`
