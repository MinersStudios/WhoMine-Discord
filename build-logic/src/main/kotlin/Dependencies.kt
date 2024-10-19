@file:Suppress("unused")

import com.github.jengelman.gradle.plugins.shadow.internal.DependencyFilter
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.provider.Provider

fun DependencyFilter.exclude(provider: Provider<ExternalModuleDependency>): DependencyFilter? =
    exclude(provider.get())

fun DependencyFilter.exclude(dependency: ExternalModuleDependency): DependencyFilter? =
    exclude(dependency("${dependency.group}:${dependency.name}:.*"))

fun DependencyFilter.include(provider: Provider<ExternalModuleDependency>): DependencyFilter? =
    include(provider.get())

fun DependencyFilter.include(dependency: ExternalModuleDependency): DependencyFilter? =
    include(dependency("${dependency.group}:${dependency.name}:.*"))

