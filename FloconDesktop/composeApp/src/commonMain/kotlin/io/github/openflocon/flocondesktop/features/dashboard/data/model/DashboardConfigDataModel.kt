package io.github.openflocon.flocondesktop.features.dashboard.data.model

import io.github.openflocon.flocondesktop.features.dashboard.domain.model.ContainerType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardConfigDataModel(
    val dashboardId: String,
    val containers: List<ContainerConfigDataModel>,
)

@Serializable
sealed class ContainerConfigDataModel {
    abstract val name: String
    abstract val containerId: String
    abstract val elements: List<DashboardElementDataModel>
}

@Serializable
@SerialName("FORM")
data class FormContainerDataModel(
    override val name: String,
    override val containerId: String,
    override val elements: List<DashboardElementDataModel>,
    val submitText: String
) : ContainerConfigDataModel()

@Serializable
@SerialName("SECTION")
data class SectionContainerDataModel(
    override val name: String,
    override val containerId: String,
    override val elements: List<DashboardElementDataModel>,
) : ContainerConfigDataModel()

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
