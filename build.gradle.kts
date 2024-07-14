import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	id("org.graalvm.buildtools.native") version "0.9.27"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("kapt") version "1.5.21"
	id("java-library")
//	id("com.bmuschko.docker-spring-boot-application") version "9.4.0"
	id("application")
	id("java")
}

val mainClassPath = project.findProperty("mainClassName")?.toString()


application{
	mainClass = mainClassPath
}


allprojects {
	group = "auto-gear"
	version = "0.0.1-SNAPSHOT"
	repositories {
		mavenCentral()
		mavenLocal()
	}
}

//
//docker{
//
//	springBootApplication{
//		baseImage.set("ghcr.io/graalvm/jdk-community:17")
//		ports.set(listOf(8001))
//		images.set(setOf(project.name + ":" + project.version))
//
//		mainClassName.set("FrontApiApplication.kt")
//
//	}
//}

kapt {
	useBuildCache =  true
}

java {
	sourceCompatibility = JavaVersion.VERSION_17

}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

dependencies {
	implementation(project(":core"))

// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")


// https://mvnrepository.com/artifact/com.google.cloud/google-cloud-storage
//	implementation("com.google.cloud:google-cloud-storage:2.40.1")

// https://mvnrepository.com/artifact/software.amazon.awssdk/s3
	implementation("software.amazon.awssdk:s3:2.26.16")

//	implementation ("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")
//
//	implementation ("com.google.api-client:google-api-client:2.0.0")
//	implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
//	implementation ("com.google.auth:google-auth-library-oauth2-http:1.11.0")

// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	implementation("org.yaml:snakeyaml:2.0")

	implementation("com.google.api-client:google-api-client:2.0.0")
	implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
	implementation("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")

	implementation("com.auth0:java-jwt:3.18.1")
	// https://mvnrepository.com/artifact/com.google.guava/guava
	implementation("com.google.guava:guava:32.1.2-jre")
//	implementation("org.springframework.cloud:spring-cloud-config-server:3.1.5")
	implementation("org.springframework.boot:spring-boot-starter-freemarker")
	implementation("com.sendgrid:sendgrid-java:4.9.3")
	implementation("org.springframework.boot:spring-boot-starter-mail")

	implementation ("org.mapstruct:mapstruct:1.5.5.Final")

	annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.5.Final")

	kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
//	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
//	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
//	implementation("org.springframework.boot:spring-boot-starter-graphql")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
//	implementation("org.springframework.session:spring-session-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
//	testImplementation("org.springframework.graphql:spring-graphql-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-tiny:latest")
}
