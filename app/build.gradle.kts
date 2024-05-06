plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.nexusdev.dining"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nexusdev.dining"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    // FirebaseUI for Firebase Auth
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")
    // Import the BoM for the Firebase platform
    implementation (platform("com.google.firebase:firebase-bom:32.2.3"))
    // Declare the dependency for the Cloud Firestore library
    implementation ("com.google.firebase:firebase-firestore-ktx:24.9.1")
    // Declare the dependency for the Realtime Database library
    implementation ("com.google.firebase:firebase-database-ktx:20.3.0")
    // Declare the dependencies for the Firebase Cloud Messaging and Analytics libraries
    implementation ("com.google.firebase:firebase-messaging:23.3.1")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("com.google.firebase:firebase-firestore:24.9.1")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //MaterialDesign
    implementation("com.google.android.material:material:1.11.0-alpha02")
    implementation("androidx.appcompat:appcompat:1.7.0-alpha03")

    //ImageGif
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
}