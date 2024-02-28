plugins {
    id("cloud-translations.base-conventions")
    id("cloud-translations.publishing-conventions")
}

dependencies {
    api(libs.cloud.core)
}

tasks.writeLocales {
    key = "org.incendo.cloud.core.lang"
}
