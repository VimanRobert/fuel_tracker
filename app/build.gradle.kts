plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.dagger.hilt.android)
    id("com.google.gms.google-services")
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "com.fueltracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fueltracker"
        minSdk = 31
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.kotlinx.metadata.jvm)

    implementation(platform(libs.firebase.bom))

    implementation(libs.hilt)
    implementation(libs.google.firebase.firestore.ktx)
    kapt(libs.hilt.compiler)

    androidTestImplementation(libs.hilt.android.testing)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
