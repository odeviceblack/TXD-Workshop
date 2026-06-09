plugins {
	alias(libs.plugins.android.application)
}

android {
	namespace = "com.gtasatutoymas.txdw"
	buildToolsVersion = "35.0.0"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.gtasatutoymas.txdw"
		minSdk = 26
		targetSdk = 35
		versionCode = 3
		versionName = "1.0.0"
	}

	signingConfigs {
		create("release") {
			storeFile = file(project.properties["storeFile"]!!)
			storePassword = project.properties["storePassword"] as String
			keyAlias = project.properties["keyAlias"] as String
			keyPassword = project.properties["keyPassword"] as String
		}
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = true
			signingConfig = signingConfigs.getByName("release")
		}
	}

	buildFeatures {
		viewBinding = true
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}

dependencies {
	implementation(libs.androidx.core)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
}
