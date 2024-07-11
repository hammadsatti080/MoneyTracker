import groovyjarjarantlr.build.ANTLR.compiler

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.moneytracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.moneytracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.appcompat)
    implementation (libs.constraintlayout)
    implementation (libs.material)
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor( "androidx.room:room-compiler:2.6.1")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")
}