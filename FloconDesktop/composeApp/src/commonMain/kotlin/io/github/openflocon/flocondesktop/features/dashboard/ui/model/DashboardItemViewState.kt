package io.github.openflocon.flocondesktop.features.dashboard.ui.model

import androidx.compose.ui.graphics.Color
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.ContainerType

data class DashboardItemViewState(
    val containerId: String,
    val containerName: String,
    val containerType: ContainerType,
    val rows: List<RowItem>,
) {
    sealed interface RowItem {
        data class Text(
            val label: String,
            val value: String,
            val color: Color?,
        ) : RowItem

        data class PlainText(
            val label: String,
            val value: String,
        ) : RowItem

        data class Button(
            val text: String,
            val id: String,
        ) : RowItem

        data class TextField(
            val label: String,
            val placeHolder: String?,
            override val value: String,
            override val id: String,
        ) : RowItem, FormValueItem

        data class CheckBox(
            val label: String,
            override val value: Boolean,
            override val id: String,
        ) : RowItem, FormValueItem
    }

    /** Any item that can be used as a value in a form */
    interface FormValueItem {
        val id: String
        val value: Any
    }
}

fun previewDashboardItemViewState() = DashboardItemViewState(
    containerId = "user",
    containerName = "User",
    containerType = ContainerType.SECTION,
    rows = listOf(
        DashboardItemViewState.RowItem.Text("username", "flo", color = null),
        DashboardItemViewState.RowItem.Text("user.id", "1234567", color = Color.Red),
        DashboardItemViewState.RowItem.CheckBox(label = "isEnabled", value = true, "id"),
        DashboardItemViewState.RowItem.TextField(
            label = "change name",
            value = "florent",
            id = "id",
            placeHolder = "new name"
        ),
        DashboardItemViewState.RowItem.Button("click me", "id"),
    ),
)
