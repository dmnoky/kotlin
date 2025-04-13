plugins {
    kotlin("jvm") version "2.1.10"
    id("org.jetbrains.kotlinx.dataframe") version "0.15.0-RC3"
}

group = "ru.tbank"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    implementation ("org.jetbrains.kotlinx:dataframe:0.15.0-RC3")
    implementation ("org.postgresql:postgresql:42.7.5")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}