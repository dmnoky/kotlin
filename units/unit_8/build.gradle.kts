plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.jpa") version "2.1.20"
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
    //implementation ("org.hibernate:hibernate:3.5.4-Final")
    implementation ("org.postgresql:postgresql:42.7.5")
    implementation ("org.hibernate:hibernate-core:5.6.15.Final")
    implementation("org.liquibase:liquibase-core:4.31.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}