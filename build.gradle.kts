// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	alias(libs.plugins.androidApplication) apply false
	alias(libs.plugins.jetbrainsKotlinAndroid) apply false
	id("com.google.devtools.ksp") version "1.9.21-1.0.15"
	val roomVersion = "2.6.1"
	id("androidx.room") version roomVersion apply false
}