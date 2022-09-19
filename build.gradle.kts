import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    id("org.siouan.frontend-jdk11") version "6.0.0"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "dev.robinohs"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.orekit:orekit:11.2.1")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.6.0")
    testImplementation("org.assertj:assertj-core:3.23.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

frontend {
    nodeVersion.set("16.14.2")
    packageJsonDirectory.set(File("src/frontend"))
    assembleScript.set("build")
    yarnEnabled.set(true)
    yarnVersion.set("1.22.15")
}

tasks.register<Copy>("copyFrontend") {
    from(layout.projectDirectory.dir("src/frontend/dist"))
    into(layout.projectDirectory.dir("src/main/resources/static"))
}