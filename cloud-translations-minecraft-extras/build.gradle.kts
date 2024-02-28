plugins {
    id("cloud-translations.base-conventions")
    id("cloud-translations.publishing-conventions")
}

dependencies {
    api(projects.cloudTranslationsCore)
    compileOnlyApi(libs.cloud.minecraft.extras)
    testImplementation(libs.cloud.minecraft.extras)
}

tasks.writeLocales {
    key = "org.incendo.cloud.minecraft.extras.lang"
}
