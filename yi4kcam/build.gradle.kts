plugins {
    alias(libs.plugins.convention.library)
}

android {
    namespace = "it.nekotak.yi4kcam"

    defaultConfig {
        externalNativeBuild {
            cmake {
                cppFlags("-std=c++17")
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
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {

}
