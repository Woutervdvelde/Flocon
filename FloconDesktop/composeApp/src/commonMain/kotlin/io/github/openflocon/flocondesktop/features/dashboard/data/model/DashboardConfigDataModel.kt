package io.github.openflocon.flocondesktop.features.dashboard.data.model

import io.github.openflocon.flocondesktop.features.dashboard.domain.model.ContainerType
import kotlinx.serialization.Serializable

@Serializable
data class DashboardConfigDataModel(
    val dashboardId: String,
    val containers: List<ContainerConfigDataModel>,
)

@Serializable
data class ContainerConfigDataModel(
    val name: String,
    val containerId: String,
    val containerType: ContainerType,
    val elements: List<DashboardElementDataModel>,
)

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
