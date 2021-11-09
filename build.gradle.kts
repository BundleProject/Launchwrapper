plugins {
    kotlin("jvm") version "1.5.31"
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