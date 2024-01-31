import org.incendo.cloudbuildlogic.city
import org.incendo.cloudbuildlogic.jmp

plugins {
    id("org.incendo.cloud-build-logic.publishing")
}

indra {
    github("Incendo", "cloud-translations") {
        ci(true)
    }
    mitLicense()

    configurePublications {
        pom {
            developers {
                city()
                jmp()
            }
        }
    }
}
