@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension


group = "org.example"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

plugins {
    java
    kotlin("jvm") version embeddedKotlinVersion
    kotlin("plugin.allopen") version "2.0.20"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.13"
}

sourceSets.configureEach {
    java.setSrcDirs(listOf("$name/src"))
    resources.setSrcDirs(listOf("$name/resources"))
}

configure<AllOpenExtension> {
    annotation("org.openjdk.jmh.annotations.State")
}

dependencies {
    api(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.13")
    implementation("org.jetbrains.kotlin:kotlin-test-junit5:${embeddedKotlinVersion}")
}

benchmark {
    targets {
        register("main")
    }
}

tasks.test {
    useJUnitPlatform()
}
