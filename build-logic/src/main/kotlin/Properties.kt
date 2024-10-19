import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion

val utf8: String = Charsets.UTF_8.name()

val Project.javaVersionInt: Int
    get() = (
                getProperty("java.version")
                ?: throw IllegalStateException("Java version not set")
            ).toString().toInt()

val Project.javaVersion: JavaVersion
    get() = JavaVersion.toVersion(javaVersionInt)

val Project.javaLanguageVersion: JavaLanguageVersion
    get() = JavaLanguageVersion.of(javaVersionInt)

val Project.javaCompilerArgs: String
    get() = (
                getProperty("java.compilerArgs")
                ?: throw IllegalStateException("Java compiler args not set")
            ).toString()

val Project.projectGroup: String
    get() = (
                getProperty("project.group")
                ?: throw IllegalStateException("Project group not set")
            ).toString()

val Project.projectVersion: Any
    get() = (
                getProperty("project.version")
                ?: throw IllegalStateException("Project version not set")
            )

val Project.projectDescription: String
    get() = (
                getProperty("project.description")
                ?: throw IllegalStateException("Project description not set")
            ).toString()

val Project.paperVersion: String
    get() = (
                getProperty("paper.version")
                ?: throw IllegalStateException("Paper version not set")
            ).toString()

val Project.projectAuthor: String
    get() = (
                getProperty("project.author")
                ?: throw IllegalStateException("Author not set")
            ).toString()

val Project.projectContributors: String
    get() = (
                getProperty("project.contributors")
                ?: throw IllegalStateException("Contributors not set")
            ).toString()

val Project.projectWebsite: String
    get() = (
                getProperty("project.website")
                ?: throw IllegalStateException("Website not set")
            ).toString()

fun apiVersion(minecraftVersion: String): String {
    val parts = minecraftVersion.split('.')

    return if (parts.size < 2) {
        throw IllegalStateException("Invalid Minecraft version: '$minecraftVersion'")
    } else {
        "'${parts[0]}.${parts[1]}'"
    }
}

fun Project.getProperty(name: String): Any? {
    return rootProject.property(name)
}

fun Project.getProperty(
    name: String,
    default: Any
): Any {
    return rootProject.property(name) ?: default
}
