package io.github.openflocon.flocondesktop.features.dashboard.data.datasources.room.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.ContainerType

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = DashboardEntity::class,
            parentColumns = ["dashboardId"],
            childColumns = ["dashboardId"],
            onDelete = ForeignKey.CASCADE, // If a dashboard is deleted, its sections are also deleted
        ),
    ],
    indices = [
        Index(value = ["dashboardId"]),
        Index(
            value = ["dashboardId", "containerOrder"],
            unique = true,
        ),
    ],
)
data class DashboardContainerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dashboardId: String,
    val containerId: String,
    val type: ContainerType,
    val containerOrder: Int,
    val name: String,
)
