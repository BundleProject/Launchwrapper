plugins {
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    `maven-publish`
}

group = "org.bundleproject"
version = "0.1.1"

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion: String by project
    val kotlinVersion: String by project

    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.0")
    implementation("ch.qos.logback:logback-classic:1.2.7")
}

tasks {
    shadowJar {
        archiveBaseName.set("launchwrapper")
        archiveClassifier.set("")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "org.bundleproject"
                artifactId = "launchwrapper"
                version = project.version as String

                artifact(shadowJar) {
                    builtBy(shadowJar)
                }
            }
        }
    }
}
