import com.android.aaptcompiler.android.isTruthy
import java.util.Locale

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.mozilla.rust-android-gradle.rust-android")
}

android {
    namespace = "com.ghamza.jarust"
    compileSdk = GradleConfigs.compileSdk
    ndkVersion = GradleConfigs.ndkVersion

    defaultConfig {
        minSdk = GradleConfigs.minSdk
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

cargo {
    module  = CargoConfigs.modulePath
    targets = listOf("arm64", "arm", "x86_64", "x86")
    libname = CargoConfigs.libName
    profile = CargoConfigs.profile
    pythonCommand = CargoConfigs.pythonCommand
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.jna)
    implementation(libs.rustls.platform.verifier)
}

tasks.whenTaskAdded {
    if (this.name == "javaPreCompileDebug" || this.name == "javaPreCompileRelease") {
        this.dependsOn("cargoBuild")
    }
}

afterEvaluate {
    android.libraryVariants.all { variant ->
        var productFlavor = ""
        variant.productFlavors.forEach { flavor ->
            productFlavor += flavor.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString()
            }
        }
        val buildType = variant.buildType.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
        tasks["generate${productFlavor}${buildType}Assets"].setDependsOn(listOf(tasks["cargoBuild"])).isTruthy()
    }
}

//fun findRustlsPlatformVerifierProject(): URI {
//    val dependencyText = providers.exec {
//        workingDir = File("../")
//        commandLine("cargo", "metadata", "--format-version", "1", "--manifest-path", "./Cargo.toml")
//    }
//        .standardOutput
//        .asText
//        .get()
//    val dependencyJson = JsonSlurper().parseText(dependencyText) as Map<*, *>
//    val packages = dependencyJson["packages"] as List<*>
//    val targetPackage = packages.find {
//        val pkgMap = it as Map<*, *>
//        pkgMap["name"] == "rustls-platform-verifier-android"
//    } as Map<*, *>
//    val manifestPath = targetPackage["manifest_path"] as Map<*, *>
//
//    return File(manifestPath.parent).toURI()
//}
