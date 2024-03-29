plugins {
    alias(libs.plugins.convention.application.compose)
    alias(libs.plugins.convention.releaseSigning)
}

android {
    namespace = "it.nekotak.ndkdemo"
    defaultConfig {
        applicationId = "it.nekotak.ndkdemo"
        externalNativeBuild {
            cmake {
                cppFlags += listOf(
                    "-std=c++20",
                )
                // gradle only BUILDS (does NOT package) the ABIs specified below
                // details: https://developer.android.com/studio/projects/gradle-external-native-builds#specify-abi
                abiFilters += setOf()
            }
            ndk {
                // gradle BUILDS and PACKAGES the ABIs specified below
                abiFilters += setOf(
                    "arm64-v8a",
//                    "x86_64",
                )
            }
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {

}
