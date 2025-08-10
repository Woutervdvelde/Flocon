package io.github.openflocon.flocondesktop.features.dashboard.data.datasources.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ToDeviceSubmittedFormMessage(
    val id: String,
    val values: Map<String, String>
)
