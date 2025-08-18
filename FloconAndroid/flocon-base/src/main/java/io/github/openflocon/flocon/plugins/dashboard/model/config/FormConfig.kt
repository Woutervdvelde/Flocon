package io.github.openflocon.flocon.plugins.dashboard.model.config

data class FormConfig(
    override val name: String,
    override val elements: List<ElementConfig>,
    val id: String,
    val submitText: String,
    val onSubmitted: (Map<String, String>) -> Unit,
) : ContainerConfig