plugins {
    java
    application
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
    kotlin("plugin.jpa") version "2.1.0"
}

group = "ru.lizyakin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("ru.lizyakin.vending_machines")
    mainClass.set("ru.lizyakin.vending_machines.HelloApplication")
}
kotlin {
    jvmToolchain(17)
}

javafx {
    version = "17.0.14"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    // Hibernate 6
    implementation("org.hibernate.orm:hibernate-core:6.2.7.Final")
    // Драйвер PostgreSQL
    implementation("org.postgresql:postgresql:42.7.2")
    // Kotlin Reflection (обязательно!)
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Тесты

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
