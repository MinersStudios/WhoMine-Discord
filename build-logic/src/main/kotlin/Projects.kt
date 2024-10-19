@file:Suppress("MemberVisibilityCanBePrivate", "unused")

import org.gradle.api.Project

const val WHOMINE_PREFIX = "WhoMine"
const val WHOLIB_PREFIX = "WhoLib"

interface Projects {
    val projectName: String
    val projectPath: String
    val projectDir: String
        get() = projectName
    val buildDir: String
        get() = "build/$projectDir"

    fun asProject(root: Project): Project =
        root.project(projectPath)

    fun exists(root: Project): Boolean =
        root.findProject(projectPath) != null

    companion object {
        fun fromName(name: String): Projects? =
            Libs.fromName(name) ?: Platforms.fromName(name)
    }
}

enum class Libs(prefix: String = WHOLIB_PREFIX) : Projects {
    Common, Paper, Velocity;

    override val projectName: String = "$prefix-${name.lowercase()}"
    override val projectPath: String = ":$projectName"
    override val projectDir:  String = "lib/$projectName"

    companion object {
        fun fromName(name: String): Projects? =
            values().find { it.projectName == name }
    }
}

enum class Platforms(prefix: String = WHOMINE_PREFIX) : Projects {
    Paper, Velocity;

    override val projectName: String = "$prefix-${name.lowercase()}"
    override val projectPath: String = ":$projectName"
    override val projectDir:  String = "platform/$projectName"

    companion object {
        fun fromName(name: String): Projects? =
            values().find { it.projectName == name }
    }
}
