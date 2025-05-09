pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        plugins {
            id("com.google.gms.google-services") version "4.4.1"
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
//    versionCatalogs {
//        create("libs") {
//            library("firebase-bom", "com.google.firebase:firebase-bom:33.3.0")
//            library("firebase-auth", "com.google.firebase:firebase-auth")
//            library("firebase-database", "com.google.firebase:firebase-database")
//            library("firebase-analytics", "com.google.firebase:firebase-analytics")
//            library("okhttp", "com.squareup.okhttp3:okhttp:4.12.0")
//            library("gson", "com.google.code.gson:gson:2.10.1")
//        }
//    }
}

rootProject.name = "WeatherApp"
include(":app")
 