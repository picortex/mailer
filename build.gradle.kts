@Suppress("DSL_SCOPE_VIOLATION") plugins {
    alias(kotlinz.plugins.root.multiplatform) apply false
    alias(kotlinz.plugins.root.serialization) apply false
    alias(asoft.plugins.root.library) apply false
    alias(petuska.plugins.root.npm.publish) apply false
    alias(bmuschko.plugins.root.docker) apply false
    alias(kotlinz.plugins.dokka)
}
val tmp = 0
allprojects {
    beforeEvaluate {
        repositories {
            publicRepos()
            maven {
                name = "piCortex"
                url = uri("http://${picortex.versions.server.ip.get()}:1050/repository/internal/")
                isAllowInsecureProtocol = true
                credentials {
                    username = "admin"
                    password = "admin@123"
                }
            }
            mavenLocal()
        }
        group = "com.picortex"
        version = picortex.versions.picortex.get()
    }

    afterEvaluate {
        afterEvaluate {
            tasks.configureEach {
                if (name.endsWith("MainKotlinMetadata")) {
                    enabled = false
                }
            }
        }
    }
}