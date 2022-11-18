plugins {
    kotlin("jvm") version "1.7.21"
}

val javaToUse = JavaVersion.VERSION_17

java {
    sourceCompatibility = javaToUse
    targetCompatibility = javaToUse
}

repositories {
    mavenCentral()
}

tasks {
    wrapper {
        gradleVersion = "7.3"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaToUse.majorVersion
        }
    }
}
