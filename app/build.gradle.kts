import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val firebaseEnabled = providers.gradleProperty("firebaseEnabled").getOrElse("false").toBoolean()
if (firebaseEnabled) {
    require(file("google-services.json").isFile) {
        "Place the team's real google-services.json in app/ before enabling Firebase."
    }
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

android {
    namespace = "dev.example.permissioncrashdemo"
    compileSdk = 36

    defaultConfig {
        applicationId = providers.gradleProperty("demoApplicationId")
            .getOrElse("dev.example.permissioncrashdemo")
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.0-crash"
        buildConfigField("boolean", "FIREBASE_ENABLED", firebaseEnabled.toString())
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets.getByName("main").java.srcDir(
        if (firebaseEnabled) "src/firebase/kotlin" else "src/local/kotlin",
    )
}

kotlin {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
}

dependencies {
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.ui:ui:1.11.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test:runner:1.7.0")
    if (firebaseEnabled) {
        implementation(platform("com.google.firebase:firebase-bom:35.1.0"))
        implementation("com.google.firebase:firebase-crashlytics")
    }
}
