plugins {
    kotlin("jvm") version "1.5.31"
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

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")
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
