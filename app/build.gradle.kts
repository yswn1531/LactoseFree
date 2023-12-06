plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
}

android {
    namespace = "com.yoon.lactosefree"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yoon.lactosefree"
        minSdk = 23
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //preference
    implementation ("androidx.preference:preference-ktx:1.2.1")
    //CircleView
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //rxbinding
    implementation ("com.jakewharton.rxbinding4:rxbinding:4.0.0")
    //Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.5")
    //LBS
    //implementation group: 'gun0912.ted', name: 'tedpermission'
    implementation ("io.github.ParkSangGwon:tedpermission-normal:3.3.0")
    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //retrofit - gson
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //workManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    //googleMap
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    //glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    //RoomDB
    implementation ("androidx.room:room-runtime:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")
    //lottie
    implementation ("com.airbnb.android:lottie:6.2.0")
    //Splash
    implementation ("androidx.core:core-splashscreen:1.0.1")


}