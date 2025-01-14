plugins {
    id("org.incendo.cloud-build-logic")
    id("org.incendo.cloud-build-logic.spotless")
    id("org.incendo.cloud-build-logic.write-locale-list")
}

indra {
    checkstyle().set(libs.versions.checkstyle)
    javaVersions {
        minimumToolchain(17)
        target(17)
        testWith().set(setOf(17))
    }
}

cloudSpotless {
    ktlintVersion = libs.versions.ktlint
}

spotless {
    java {
        importOrderFile(rootProject.file(".spotless/cloud-translations.importorder"))
    }
}

// Common dependencies.
dependencies {
    // external
    compileOnly(libs.immutables)
    annotationProcessor(libs.immutables)

    // test dependencies
    testImplementation(libs.jupiter.engine)
    testImplementation(libs.jupiter.params)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.jupiter)
    testImplementation(libs.truth)
}

tasks {
    jar {
        manifest {
            attributes("Automatic-Module-Name" to "%s.%s".format(project.group, project.name.replace('-', '.')))
        }
    }
}
