pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.mappls.com/repository/mappls/")
        maven ("https://download2.dynamsoft.com/maven/aar")
    }
}

rootProject.name = "Smart Toll"
include(":app")
