plugins {
    id("org.jetbrains.intellij") version "1.5.2"
}

group = "pl.mjedynak"
version = "1.2.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    testImplementation("org.mockito:mockito-junit-jupiter:4.6.1")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.springframework:spring-test:5.2.22.RELEASE")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    version.set("2021.2")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("java"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("201.6668.121")
        untilBuild.set("")
    }

    test {
        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "7.4.2"
    }
}
