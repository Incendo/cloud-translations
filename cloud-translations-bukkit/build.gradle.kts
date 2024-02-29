plugins {
    id("cloud-translations.base-conventions")
    id("cloud-translations.publishing-conventions")
}

dependencies {
    api(projects.cloudTranslationsCore)
}

writeLocaleList.registerBundle("org.incendo.cloud.bukkit.lang.messages")
