plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.freeplaytv.shortcut"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.freeplaytv.shortcut"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = (project.findProperty("releaseVersionName") as String?) ?: "1.0"
    }

    val releaseStoreFile = System.getenv("RELEASE_STORE_FILE")
    if (releaseStoreFile != null) {
        signingConfigs {
            create("release") {
                storeFile = file(releaseStoreFile)
                storePassword = System.getenv("RELEASE_STORE_PASSWORD")
                keyAlias = System.getenv("RELEASE_KEY_ALIAS")
                keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            if (releaseStoreFile != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
