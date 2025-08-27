package io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room

import androidx.room.Embedded
import androidx.room.Relation
import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room.model.DashboardEntity
import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room.model.DashboardContainerEntity

data class DashboardWithContainersAndElements(
    @Embedded
    val dashboard: DashboardEntity,

    @Relation(
        parentColumn = "dashboardId",
        entityColumn = "dashboardId",
        entity = DashboardContainerEntity::class,
    )
    val containersWithElements: List<ContainerWithElements>,
)
