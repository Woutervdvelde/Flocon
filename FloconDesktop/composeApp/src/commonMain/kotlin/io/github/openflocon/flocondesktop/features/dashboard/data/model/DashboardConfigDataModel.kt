package io.github.openflocon.flocondesktop.features.dashboard.data.model

import io.github.openflocon.flocondesktop.features.dashboard.domain.model.ContainerConfigDomainModel
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.ContainerType
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.FormContainerConfigDomainModel
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.SectionContainerConfigDomainModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import java.awt.Container

@Serializable
data class DashboardConfigDataModel(
    val dashboardId: String,
    val containers: List<DashboardContainerDataModel>,
)

@Serializable
data class DashboardContainerDataModel(
    val name: String,
    val elements: List<DashboardElementDataModel>,
    val containerConfig: ContainerConfigDataModel
)

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("containerType")
@Serializable
sealed class ContainerConfigDataModel {
    abstract fun unwrap(): ContainerConfigDomainModel
}

@Serializable
@SerialName("FORM")
data class FormContainerConfigDataModel(
    val formId: String,
    val submitText: String,
) : ContainerConfigDataModel() {
    override fun unwrap(): ContainerConfigDomainModel =
        FormContainerConfigDomainModel(
            formId = formId,
            submitText = submitText,
        )
}

@Serializable
@SerialName("SECTION")
data object SectionContainerConfigDataModel : ContainerConfigDataModel() {
    override fun unwrap(): ContainerConfigDomainModel =
        SectionContainerConfigDomainModel
}

@Serializable
data class DashboardElementDataModel(
    val button: ButtonConfigDataModel? = null,
    val text: TextConfigDataModel? = null,
    val plainText: PlainTextConfigDataModel? = null,
    val textField: TextFieldConfigDataModel? = null,
    val checkBox: CheckBoxConfigDataModel? = null,
)

@Serializable
data class ButtonConfigDataModel(
    val text: String,
    val id: String,
)

@Serializable
data class TextFieldConfigDataModel(
    val label: String,
    val placeHolder: String? = null,
    val value: String,
    val id: String,
)

@Serializable
data class CheckBoxConfigDataModel(
    val label: String,
    val value: Boolean,
    val id: String,
)

@Serializable
data class TextConfigDataModel(
    val label: String,
    val value: String,
    val color: Int? = null,
)

@Serializable
data class PlainTextConfigDataModel(
    val label: String,
    val value: String,
    val type: String,
)
