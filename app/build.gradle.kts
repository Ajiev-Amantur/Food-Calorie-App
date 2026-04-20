plugins {
    alias(libs.plugins.android.application)
        // ... другие плагины
    id("com.google.devtools.ksp") // ДОЛЖНО БЫТЬ ЭТО
}

android {
    namespace = "com.example.calories"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.calories"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

implementation(libs.gson)

    implementation(libs.mpandroidchart)
    implementation(libs.philjay.mpandroidchart)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

}