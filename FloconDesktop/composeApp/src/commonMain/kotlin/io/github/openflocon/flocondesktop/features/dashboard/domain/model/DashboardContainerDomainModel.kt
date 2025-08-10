package io.github.openflocon.flocondesktop.features.dashboard.domain.model

data class DashboardContainerDomainModel(
    val name: String,
    val containerType: ContainerType,
    val elements: List<DashboardElementDomainModel>,
)
