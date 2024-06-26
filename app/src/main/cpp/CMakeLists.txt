
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("ndkdemo")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#
# In this top level CMakeLists.txt, ${CMAKE_PROJECT_NAME} is used to define
# the target library name; in the sub-module's CMakeLists.txt, ${PROJECT_NAME}
# is preferred for the same purpose.
#
# In order to load a library into your app from Java/Kotlin, you must call
# System.loadLibrary() and pass the name of the library defined here;
# for GameActivity/NativeActivity derived applications, the same library name must be
# used in the AndroidManifest.xml file.
add_library(${CMAKE_PROJECT_NAME} SHARED
    # List C/C++ source files with relative paths to this CMakeLists.txt.
    ndkdemo.cpp)

# Specifies a path to native header files.
include_directories(${PROJECT_SOURCE_DIR}/include/)


# Standard logging library
find_library(
    # Defines the name of the path variable that stores the location of the NDK library.
    log-lib
    # Specifies the name of the NDK library that CMake needs to locate.
    log)


# 3d-party imported library
set(camera-lib yi4kcam)  # refer to the library via a variable
set(ANDROID_PROJECT_ROOT_DIR ${CMAKE_SOURCE_DIR}/../../../)
add_library(${camera-lib} SHARED IMPORTED)
include_directories(${ANDROID_PROJECT_ROOT_DIR}/libs/)
set_target_properties(
        # Specifies the target library.
        ${camera-lib}
        # Specifies the parameter you want to define.
        PROPERTIES IMPORTED_LOCATION
        # Provides the path to the library you want to import.
        ${ANDROID_PROJECT_ROOT_DIR}/libs/${ANDROID_ABI}/lib${camera-lib}.so )


# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.
target_link_libraries(${CMAKE_PROJECT_NAME}
    # List libraries link to the target library
    ${camera-lib}
    android
    ${log-lib})
