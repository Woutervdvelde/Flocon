package io.github.openflocon.flocondesktop.features.dashboard.domain

import io.github.openflocon.flocondesktop.core.domain.device.GetCurrentDeviceIdAndPackageNameUseCase
import io.github.openflocon.flocondesktop.features.dashboard.domain.repository.DashboardRepository

class SubmitFormToDeviceDeviceUseCase(
    private val dashboardRepository: DashboardRepository,
    private val getCurrentDeviceIdAndPackageNameUseCase: GetCurrentDeviceIdAndPackageNameUseCase,
    private val getCurrentDeviceSelectedDashboardUseCase: GetCurrentDeviceSelectedDashboardUseCase,
) {
    suspend operator fun invoke(formId: String, values: Map<String, Any>) {
        val current = getCurrentDeviceIdAndPackageNameUseCase() ?: return
        val currentDashboard = getCurrentDeviceSelectedDashboardUseCase() ?: return

        dashboardRepository.submitFormEvent(
            deviceIdAndPackageName = current,
            dashboardId = currentDashboard,
            formId = formId,
            values = values
                // Remove the dashboard prefix from the keys to match actual ID set by user
                .map { (key, value) -> key.replaceFirst("${currentDashboard}_", "") to value.toString() }
                .toMap()
        )
    }
}

