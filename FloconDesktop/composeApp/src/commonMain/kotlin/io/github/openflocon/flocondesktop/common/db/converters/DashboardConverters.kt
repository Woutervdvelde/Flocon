package io.github.openflocon.flocondesktop.common.db.converters

import androidx.room.TypeConverter
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.ContainerConfigDomainModel
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.FormContainerConfigDomainModel
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.SectionContainerConfigDomainModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object DashboardConverters {
    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(ContainerConfigDomainModel::class) {
                subclass(
                    FormContainerConfigDomainModel::class,
                    FormContainerConfigDomainModel.serializer()
                )
                subclass(
                    SectionContainerConfigDomainModel::class,
                    SectionContainerConfigDomainModel.serializer()
                )
            }
        }
    }

    @TypeConverter
    fun fromContainerConfig(value: ContainerConfigDomainModel): String =
        json.encodeToString(value)

    @TypeConverter
    fun toContainerConfig(value: String): ContainerConfigDomainModel =
        json.decodeFromString<ContainerConfigDomainModel>(value)
}
