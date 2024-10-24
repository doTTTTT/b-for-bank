plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "fr.dot.library.ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.navigation.core)

    api(libs.androidx.material.icons)

    api(libs.bundles.androidx.ui)

    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.androidx.material3)

    api(platform(libs.koin.bom))
    api(libs.bundles.koin)

}