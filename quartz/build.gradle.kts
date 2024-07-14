plugins{
    id("java")
    kotlin("jvm") version "1.8.22"
}

java{
    sourceCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
}




dependencies {
    implementation("com.auth0:java-jwt:3.18.1")
    implementation ("org.bouncycastle:bcprov-jdk15on:1.69")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

}