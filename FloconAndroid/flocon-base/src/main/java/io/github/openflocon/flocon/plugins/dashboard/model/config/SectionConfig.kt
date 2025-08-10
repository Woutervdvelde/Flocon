package io.github.openflocon.flocon.plugins.dashboard.model.config

data class SectionConfig(
    override val name: String,
    override val elements: List<ElementConfig>
) : ContainerConfig