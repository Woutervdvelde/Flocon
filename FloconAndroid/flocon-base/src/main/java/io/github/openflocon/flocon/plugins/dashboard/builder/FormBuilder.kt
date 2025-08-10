package io.github.openflocon.flocon.plugins.dashboard.builder

import io.github.openflocon.flocon.plugins.dashboard.model.config.ButtonConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.FormConfig

class FormBuilder(
    val name: String,
    val submitText: String
) : ContainerBuilder() {

    override fun build(): FormConfig {
        return FormConfig(
            name,
            elements.apply {
                add(
                    ButtonConfig(
                        text = submitText,
                        id = "form_${name}_submit",
                        onClick = {})
                )
            })
    }
}