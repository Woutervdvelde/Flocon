package io.github.openflocon.flocondesktop.features.dashboard.domain.model

data class DashboardDomainModel(
    val dashboardId: DashboardId,
    val containers: List<DashboardContainerDomainModel>,
)
