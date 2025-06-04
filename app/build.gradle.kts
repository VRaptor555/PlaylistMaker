plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "ru.vraptor.playlistmaker"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }
    
    defaultConfig {
        applicationId = "ru.vraptor.playlistmaker"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildToolsVersion = "35.0.0"

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas".toString())
    }
}

dependencies {
    ksp(libs.room.compiler)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.convertergson)
    implementation(libs.glide)
    implementation(libs.gson)
    implementation(libs.koin)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.vievpager2)
    implementation(libs.androidx.nav.fragment)
    implementation(libs.androidx.nav.ui)
    implementation(libs.coroutines)
    implementation(libs.lifecycle.vm)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}