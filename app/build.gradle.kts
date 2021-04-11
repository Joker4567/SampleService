plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"] as Int)
    defaultConfig {
        applicationId = "pro.enaza.sampleservice"
        minSdkVersion(rootProject.extra["minSdkVersion"] as Int)
        targetSdkVersion(rootProject.extra["compileSdkVersion"] as Int)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isUseProguard = false
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
        }
    }
    lintOptions {
        isIgnoreTestSources = true
    }
    kapt {
        generateStubs = true
        correctErrorTypes = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    androidExtensions {
        isExperimental = true
    }
}

dependencies {

    implementation(project(":core-ui"))
    implementation(project(":feature-lock"))

    val kotlinVersion = rootProject.extra["kotlin_version"]
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    //Dagger-Hilt
    val dagger = rootProject.extra["dagger_version"]
    val daggerWork = rootProject.extra["hilt_work_version"]
    implementation("com.google.dagger:hilt-android:$dagger")
    kapt("com.google.dagger:hilt-android-compiler:$dagger")
    implementation("androidx.hilt:hilt-work:$daggerWork")
    kapt("androidx.hilt:hilt-compiler:$daggerWork")
    //WorkManager
    implementation("androidx.work:work-runtime-ktx:2.5.0")
}
