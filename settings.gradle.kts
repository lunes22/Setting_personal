pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
    plugins {
        id("com.android.application") version "8.9.1"
        id("com.android.library")     version "8.9.1"
        id("org.jetbrains.kotlin.android") version "1.9.24"
        //id("org.jetbrains.kotlin.android") version "2.0.21" //언제 작성 된지 모르는 라인... 증말 별별 에러로 괴롭힘;;
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "antiBrokeDSWU"
include(":app")
