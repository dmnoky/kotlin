
plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.jpa") version "1.9.10"
    war
}

group = "ru.tbank"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    testImplementation("org.hibernate:hibernate-testing:5.6.15.Final")
    testImplementation("org.testcontainers:postgresql:1.20.4")
    testImplementation("org.testcontainers:junit-jupiter:1.17.4")
    implementation ("org.postgresql:postgresql:42.7.5")
    implementation ("org.hibernate:hibernate-core:5.6.15.Final")
    implementation("org.liquibase:liquibase-core:4.31.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.0")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(19)
}

