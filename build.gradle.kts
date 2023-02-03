plugins {
    kotlin("jvm") version "1.7.20"
}

group = "org.isaiahliu"
version = "1.0.0"

repositories {
    mavenCentral()
}

tasks {
    processResources {
        from("src/main/kotlin") {
            include("**/*.txt")
        }
    }
//    sourceSets {
//        main {
//            java.srcDirs("src")
//        }
//    }
//
//    wrapper {
//        gradleVersion = "7.6"
//    }
}
