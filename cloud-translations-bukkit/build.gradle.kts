plugins {
    id("cloud-translations.base-conventions")
    id("cloud-translations.publishing-conventions")
}

dependencies {
    api(projects.cloudTranslationsCore)
}

tasks.writeLocales {
    key = "org.incendo.cloud.bukkit.lang"
}
