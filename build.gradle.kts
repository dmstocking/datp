import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    application
}

group = "me.stockingd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClassName = "MainKt"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}