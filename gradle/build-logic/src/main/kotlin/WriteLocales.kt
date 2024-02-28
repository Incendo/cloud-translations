import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject
import kotlin.io.path.listDirectoryEntries

@Suppress("LeakingThis")
abstract class WriteLocales : DefaultTask() {
    @get:InputDirectory
    abstract val dir: DirectoryProperty

    @get:Input
    abstract val key: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Inject
    abstract val layout: ProjectLayout

    init {
        outputDir.convention(layout.buildDirectory.dir("tmp/writeLocales"))
        dir.convention(
            key.map {
                layout.projectDirectory.dir("src/main/resources").dir(it.replace(".", "/"))
            }
        )
    }

    @TaskAction
    fun run() {
        val dir = outputDir.get().asFile
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val f = dir.resolve(key.get().replace(".", "/"))
            .resolve("messages-locales.list")
        if (f.exists()) {
            f.delete()
        }
        f.parentFile.mkdirs()

        f.writeText(
            this.dir.get().asFile.toPath().listDirectoryEntries()
                .mapNotNull {
                    if (!it.fileName.toString().startsWith("messages_")) {
                        return@mapNotNull null
                    }

                    it.fileName.toString().substringAfter("messages_").substringBefore(".properties")
                }
                .joinToString("\n")
        )
    }
}
