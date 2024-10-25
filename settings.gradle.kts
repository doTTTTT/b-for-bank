pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BforBank"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

include(":feature:menu")

include(":library:domain")
include(":library:navigation")
include(":library:ui")
include(":library:remote:ratp")
include(":library:data")
include(":feature:filter")
