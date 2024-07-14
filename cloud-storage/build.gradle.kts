plugins {
    kotlin("jvm")
}

group = "avg.auto-gear"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinSdkVersion = "1.0.41"
val smithyKotlinVersion = "1.0.10"

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("aws.sdk.kotlin:s3:$kotlinSdkVersion")
    implementation("aws.sdk.kotlin:s3control:$kotlinSdkVersion")
    implementation("aws.sdk.kotlin:sts:$kotlinSdkVersion")
    implementation("aws.sdk.kotlin:secretsmanager:$kotlinSdkVersion")
    implementation("aws.smithy.kotlin:http-client-engine-okhttp:$smithyKotlinVersion")
    implementation("aws.smithy.kotlin:http-client-engine-crt:$smithyKotlinVersion")
    implementation("aws.smithy.kotlin:aws-signing-crt:$smithyKotlinVersion")
    implementation("aws.smithy.kotlin:http-auth-aws:$smithyKotlinVersion")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

tasks.test {
    useJUnitPlatform()
}