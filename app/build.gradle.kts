plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" // ✅ Nuevo plugin requerido
}

android {
    namespace = "mx.edu.utng.bggg.meditationtracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "mx.edu.utng.bggg.meditationtracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true // ✅ Activa Jetpack Compose
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15" // ✅ Versión actualizada para Kotlin 2.0.x
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Activity Compose (para enableEdgeToEdge)
    implementation("androidx.activity:activity-compose:1.9.2")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Navegación
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Gráficos
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
