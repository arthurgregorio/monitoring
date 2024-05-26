import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"

    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "br.eti.arthurgregorio"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val awsSpringVersion = "3.0.1"
val commonsLangVersion = "3.12.0"
val kotlinLoggingJvmVersion = "6.0.9"
var integrationTcpUdpSupportVersion = "6.3.0"

dependencyManagement {
    imports {
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:$awsSpringVersion")
    }
}

dependencies {
    // spring things
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-integration")

    implementation("org.springframework.integration:spring-integration-ip:$integrationTcpUdpSupportVersion")

    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")

    // aws spring
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")

    // utilities
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.apache.commons:commons-lang3:$commonsLangVersion")

    // dev tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    // testing
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.integration:spring-integration-test")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
        freeCompilerArgs.set(
            listOf(
                "-Xjsr305=strict",
                "-Xjdk-release=${java.sourceCompatibility}"
            )
        )
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
