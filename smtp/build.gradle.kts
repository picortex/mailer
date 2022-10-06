plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("org.jetbrains.dokka")
//    id("picortex-publish")
}

kotlin {
    target { library() }

    sourceSets {
        val main by getting {
            dependencies {
                api(projects.mailerApi)
                implementation(javax.mail)
            }
        }

        val test by getting {
            dependencies {
                implementation(projects.expectCoroutines)
            }
        }
    }
}
