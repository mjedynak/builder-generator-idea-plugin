plugins {
    id("org.jetbrains.intellij") version "1.17.1"
}

group = "pl.mjedynak"
version = "1.5.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.springframework:spring-test:6.1.4")
}

intellij {
    version.set("2023.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("java"))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("233.11799.241")
        untilBuild.set("")
    }

    test {
        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "8.6"
    }
}
