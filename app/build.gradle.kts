plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.jetbrainsKotlinAndroid)
	id("com.google.devtools.ksp") version "1.9.21-1.0.15"
	id("androidx.room")
}

android {
	namespace = "com.example.flashcard"
	compileSdk = 34
	
	defaultConfig {
		applicationId = "com.example.flashcard"
		minSdk = 24
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
		kotlinCompilerExtensionVersion = "1.5.1"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	
	room {
		schemaDirectory("$projectDir/schemas")
	}
	
}

dependencies {
	
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.ui.test.junit4.android)
	implementation(libs.androidx.material3)
	testImplementation(libs.junit.jupiter)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
	
	// Navigation
	implementation(libs.androidx.navigation.compose)
	
	// Splash Screen
	implementation(libs.androidx.core.splashscreen)
	
	// Room
	implementation(libs.androidx.room.runtime)
	annotationProcessor(libs.androidx.room.compiler)
	ksp(libs.androidx.room.compiler)
	
	// ktx room dependency
	implementation(libs.androidx.room.ktx)
	
	// lifecycle
	implementation(libs.androidx.lifecycle.extensions)
	
	// document file
	implementation(libs.androidx.documentfile)
	
	// Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
	debugImplementation(libs.ui.test.manifest)
	
	// Testing
	testImplementation(libs.mockito.core)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.junit.jupiter.api)
	testRuntimeOnly(libs.junit.jupiter.engine)
	testImplementation(libs.androidx.core.testing)
	
	
	// Add JUnit 4 dependency
	testImplementation(libs.junit)
	
	// Add AndroidX Test - for UI testing and instrumented tests
	androidTestImplementation(libs.androidx.junit)
	
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}