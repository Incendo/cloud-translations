enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            name = "sonatypeOssSnapshots"
            mavenContent { snapshotsOnly() }
        }
    }
    includeBuild("gradle/build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            name = "sonatypeOssSnapshots"
            mavenContent {
                snapshotsOnly()
            }
        }
        maven("https://m2.dv8tion.net/releases") {
            name = "dv8tion"
            mavenContent { releasesOnly() }
        }
    }
}

rootProject.name = "cloud-translations"

include("cloud-translations-bom")

include("cloud-translations-core")

// Minecraft modules
include("cloud-translations-bukkit")
include("cloud-translations-bungee")
include("cloud-translations-velocity")
include("cloud-translations-minecraft-extras")
