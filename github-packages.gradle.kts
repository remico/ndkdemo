pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/nekotakitapps/m2")
            credentials {
                gradle.rootProject {
                    username = property("gpr.login") as String
                    password = property("gpr.token") as String
                }
            }
        }
    }
}
