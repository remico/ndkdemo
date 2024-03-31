import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.tasks.ExternalNativeBuildJsonTask
import org.gradle.configurationcache.extensions.capitalized

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
                    "-std=c++17",
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


/**
 * Custom task to copy libyi4kcam.so artifact in order to prepare it for using in the main application.
 * It takes the build type into account.
 */
project.extensions.configure<ApplicationAndroidComponentsExtension> {
    onVariants { variant ->
        rootProject.subprojects.find {
            it.name == "yi4kcam"
        }?.let { camPrj ->
            val variantName = variant.name.capitalized()
            val produceCamLibTask =
                camPrj.tasks.getByPath(":${camPrj.name}:copy${variantName}JniLibsProjectAndLocalJars")

            val copyCamLibTask = project.tasks.register<Copy>("copy${variantName}Yi4KCamLib") {
                dependsOn(produceCamLibTask)
                from(produceCamLibTask.outputs.files.asPath)
                into("libs")
            }

            project.tasks.withType<ExternalNativeBuildJsonTask> {
                if (variantName in name
                    // the below is to support release variant: `configureCMakeRelWithDebInfo[<ABI>]`
                    || (variantName == "Release" && "RelWithDebInfo" in name)
                ) {
                    dependsOn(copyCamLibTask)
                }
            }
        }
    }
}
