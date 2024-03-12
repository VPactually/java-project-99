plugins {
    java
    jacoco
    id("checkstyle")
    id("io.sentry.jvm.gradle") version "4.3.1"
    application
    id("org.springframework.boot") version "3.1.10-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.freefair.lombok") version "8.4"

}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("hexlet.code.AppApplication")
}

java {
    sourceCompatibility = JavaVersion.VERSION_20
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.jacocoTestReport {
    reports { xml.required.set(true)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

sentry {
    includeSourceContext = true

    org = "vladislav-id"
    projectName = "java"
    authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

tasks.sentryBundleSourcesJava {
    enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.projectlombok:lombok")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    implementation("org.instancio:instancio-junit:3.3.1")
    implementation("net.datafaker:datafaker:2.0.2")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.2.0")
    testImplementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.2.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}
