plugins {
    id("java-library")
    id("kotlin")
    alias(libs.plugins.jetbrainsKotlinJvm)
    id("jacoco")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}

dependencies {
    testImplementation(libs.junit)
    // flow
    implementation(libs.kotlinx.coroutines.core)
}