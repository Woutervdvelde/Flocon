package io.github.openflocon.flocon.plugins.dashboard.builder

import io.github.openflocon.flocon.plugins.dashboard.model.config.ButtonConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.FormConfig

class FormBuilder(
    val name: String,
    val submitText: String,
    val onSubmitted: (Map<String, String>) -> Unit,
) : ContainerBuilder() {

    override fun build(): FormConfig {
        return FormConfig(
            id = "form_$name",
            name = name,
            elements = elements.apply {
                add(
                    ButtonConfig(
                        text = submitText,
                        id = "form_submit_$name",
                        onClick = {})
                )
            },
            onSubmitted = onSubmitted
        )
    }
}