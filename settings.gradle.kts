pluginManagement {
    val loomVersion: String by settings
    val detektVersion: String by settings
    val dokkaVersion: String by settings
    val kotlinVersion: String by settings

    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net")
        }

        maven {
            name = "Detekt"
            url = uri("https://plugins.gradle.org/m2")
        }

        maven {
            name = "Dokka"
            url = uri("https://dl.bintray.com/kotlin/dokka")
        }
    }

    plugins {
        id("fabric-loom") version loomVersion
        id("io.gitlab.arturbosch.detekt") version detektVersion
        id("org.jetbrains.dokka") version dokkaVersion
        kotlin("jvm") version kotlinVersion
    }
}

val modId: String by settings
rootProject.name = modId