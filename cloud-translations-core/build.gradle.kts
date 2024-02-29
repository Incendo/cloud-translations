plugins {
    id("cloud-translations.base-conventions")
    id("cloud-translations.publishing-conventions")
}

dependencies {
    api(libs.cloud.core)
}

writeLocaleList.registerBundle("org.incendo.cloud.core.lang.messages")
