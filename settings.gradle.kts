pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = run {
                val dependencyText = providers.exec {
                    workingDir = rootDir
                    commandLine("cargo", "metadata", "--format-version", "1")
                }
                    .standardOutput
                    .asText
                    .get()
                val dependencyJson = groovy.json.JsonSlurper().parseText(dependencyText) as Map<*, *>
                val pkgMeta = (dependencyJson["packages"] as List<*>).find { (it as Map<*, *>)["name"] ==
                        "rustls-platform-verifier-android" }
                val manifestPath = file((pkgMeta as Map<*, *>)["manifest_path"] as Any)
                File(manifestPath.parentFile, "maven").toURI()
            }
            metadataSources.artifact()
        }
    }
}

rootProject.name = "jarust-android-package"
include(":jarust")
