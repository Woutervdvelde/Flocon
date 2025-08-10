package io.github.openflocon.flocon.plugins.dashboard.model.config

sealed interface ContainerConfig {
    val name: String
    val elements: List<ElementConfig>
}