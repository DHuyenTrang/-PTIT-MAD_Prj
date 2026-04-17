import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

val localProperties = Properties().apply {
    val localPropsFile = rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        load(localPropsFile.inputStream())
    }
}

android {
    namespace = "com.n3t.mobile"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.n3t.mobile"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Inject Mapbox token from local.properties into BuildConfig
        val mapboxToken = localProperties.getProperty("MAPBOX_ACCESS_TOKEN", "")
        val googleMapsApiKey = localProperties.getProperty("GOOGLE_MAPS_API_KEY", "")
        val goongApiKey = localProperties.getProperty("GOONG_API_KEY", "")
        buildConfigField("String", "MAPBOX_ACCESS_TOKEN", "\"$mapboxToken\"")
        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"$googleMapsApiKey\"")
        buildConfigField("String", "GOONG_API_KEY", "\"$goongApiKey\"")
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation("androidx.window:window:1.3.0")
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)
    implementation(libs.glide)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.google.play.services.location)
    implementation(project(":lib-android-navigation"))
    val mapBoxVersion = "11.3.0"
    implementation("com.mapbox.maps:android:$mapBoxVersion") {
        exclude(group = "com.mapbox.mapboxsdk", module = "mapbox-sdk-directions-models")
        exclude(group = "com.mapbox.mapboxsdk", module = "mapbox-sdk-turf")
        exclude(group = "com.mapbox.mapboxsdk", module = "mapbox-sdk-services")
        exclude(group = "com.mapbox.mapboxsdk", module = "mapbox-sdk-directions-refresh-models")
        exclude(group = "com.mapbox.mapboxsdk", module = "mapbox-sdk-core")
    }
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.airbnb.android:lottie:6.4.0")
}
