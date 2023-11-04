@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "ru.jengle88.deliveryapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.jengle88.deliveryapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {}
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.animationGraphics)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.navigation.fragmentKtx)
    implementation(libs.androidx.navigation.uiKtx)
    implementation(libs.legacy.support.v4)
    debugImplementation(libs.androidx.compose.uiToolingDebug)
    debugImplementation(libs.androidx.compose.uiToolingRuntime)

    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.googleMaterial)
    implementation(libs.androidx.lifecycleRuntime)
    implementation(libs.androidx.lifecycleRuntimeCompose)
    implementation(libs.androidx.lifecycleService)
    implementation(libs.androidx.lifecycleViewModel)
    implementation(libs.androidx.lifecycleViewModelCompose)
    implementation(libs.androidx.lifecycleViewModelSavedState)
    annotationProcessor(libs.androidx.roomCompiler)
    implementation(libs.androidx.roomRuntime)

    kapt(libs.google.daggerCompiler)
    implementation(libs.google.daggerRuntime)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.immutableCollections)

    debugImplementation(libs.square.leakCanary)
    implementation(libs.square.okHttp)

    implementation(libs.glide.compose)

    testImplementation(libs.test.junit4)
    androidTestImplementation(libs.test.androidx.extJunit)
    androidTestImplementation(libs.test.androidx.espresso.core)
}