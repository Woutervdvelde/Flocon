package io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room

import androidx.room.Embedded
import androidx.room.Relation
import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room.model.DashboardElementEntity
import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room.model.DashboardContainerEntity

data class ContainerWithElements(
    @Embedded
    val container: DashboardContainerEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "sectionId",
        entity = DashboardElementEntity::class,
    )
    val elements: List<DashboardElementEntity>,
)
