plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.alorb.smarttoll"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.alorb.smarttoll"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.compose.material:material-icons-extended:<latest-version>")

    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx")


    implementation ("io.coil-kt:coil-compose:2.4.0")
    //    hilt
    implementation("com.google.dagger:hilt-android:2.47")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    ksp("com.google.dagger:hilt-android-compiler:2.47")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation ("com.google.accompanist:accompanist-navigation-animation:0.33.2-alpha")
//mapmyindia
    implementation ("com.mappls.sdk:mappls-android-sdk:8.0.8")

//    maps
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.maps.android:maps-compose:1.0.0")


    implementation ("com.dynamsoft:dynamsoftcameraenhancer:2.3.11@aar")
    implementation ("com.dynamsoft:dynamsoftbarcodereader:9.6.20@aar")


//    implementation ("com.journeyapps:zxing-android-embedded:4.1.0")
//    implementation ("com.google.zxing:core:3.3.3")

    implementation ("com.google.android.gms:play-services-location:18.0.0")

    implementation ("dev.shreyaspatil.EasyUpiPayment:EasyUpiPayment:3.0.3")

    implementation ("com.akexorcist:google-direction-library:1.2.1")

//    val room_version = "2.6.1"
//
//    implementation("androidx.room:room-runtime:$room_version")
//    annotationProcessor("androidx.room:room-compiler:$room_version")
//    // To use Kotlin Symbol Processing (KSP)
//    ksp("androidx.room:room-compiler:$room_version")
    implementation ("com.razorpay:checkout:1.6.26")

    implementation ("com.amplifyframework:core:1.4.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}