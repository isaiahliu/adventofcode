plugins {
    kotlin("jvm") version "2.0.0"
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
