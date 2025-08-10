package io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room.mapper

import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room.DashboardWithSectionsAndElements
import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room.ContainerWithElements
import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room.model.DashboardElementEntity
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.DashboardDomainModel
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.DashboardElementDomainModel
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.DashboardContainerDomainModel

internal fun DashboardWithSectionsAndElements.toDomain(): DashboardDomainModel = DashboardDomainModel(
    dashboardId = dashboard.dashboardId,
    containers = sectionsWithElements.mapNotNull {
        it.toDomain()
    },
)

internal fun ContainerWithElements.toDomain(): DashboardContainerDomainModel? {
    return DashboardContainerDomainModel(
        name = this.container?.name ?: return null,
        containerType = this.container.type,
        elements = elements.mapNotNull { it.toDomain() },
    )
}

internal fun DashboardElementEntity.toDomain(): DashboardElementDomainModel? {
    this.text?.let {
        return DashboardElementDomainModel.Text(
            label = it.label,
            value = it.value,
            color = it.color,
        )
    }
    this.button?.let {
        return DashboardElementDomainModel.Button(
            text = it.text,
            id = it.actionId,
        )
    }
    this.textField?.let {
        return DashboardElementDomainModel.TextField(
            label = it.label,
            value = it.value,
            placeHolder = it.placeHolder,
            id = it.actionId,
        )
    }
    this.checkBox?.let {
        return DashboardElementDomainModel.CheckBox(
            label = it.label,
            value = it.value,
            id = it.actionId,
        )
    }
    return null
}
