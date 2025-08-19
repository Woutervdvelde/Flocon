package io.github.openflocon.flocondesktop.features.dashboard.data

import io.github.openflocon.flocondesktop.FloconIncomingMessageDataModel
import io.github.openflocon.flocondesktop.Protocol
import io.github.openflocon.flocondesktop.common.coroutines.dispatcherprovider.DispatcherProvider
import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.DashboardLocalDataSource
import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.ToDeviceDashboardDataSource
import io.github.openflocon.flocondesktop.features.dashboard.data.datasources.device.DeviceDashboardsDataSource
import io.github.openflocon.flocondesktop.features.dashboard.data.mapper.toDomain
import io.github.openflocon.flocondesktop.features.dashboard.data.model.ContainerConfigDataModel
import io.github.openflocon.flocondesktop.features.dashboard.data.model.DashboardConfigDataModel
import io.github.openflocon.flocondesktop.features.dashboard.data.model.FormContainerDataModel
import io.github.openflocon.flocondesktop.features.dashboard.data.model.SectionContainerDataModel
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.DashboardDomainModel
import io.github.openflocon.flocondesktop.features.dashboard.domain.model.DashboardId
import io.github.openflocon.flocondesktop.features.dashboard.domain.repository.DashboardRepository
import io.github.openflocon.flocondesktop.messages.domain.model.DeviceIdAndPackageNameDomainModel
import io.github.openflocon.flocondesktop.messages.domain.repository.sub.MessagesReceiverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class DashboardRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val dashboardLocalDataSource: DashboardLocalDataSource,
    private val toDeviceDashboardDataSource: ToDeviceDashboardDataSource,
    private val deviceDashboardsDataSource: DeviceDashboardsDataSource,
) : DashboardRepository,
    MessagesReceiverRepository {

    override val pluginName = listOf(Protocol.FromDevice.Dashboard.Plugin)

    // maybe inject

    private val dashboardParser =
        Json {
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
                polymorphic(ContainerConfigDataModel::class) {
                    subclass(FormContainerDataModel::class, FormContainerDataModel.serializer())
                    subclass(SectionContainerDataModel::class, SectionContainerDataModel.serializer())
                }
            }
            classDiscriminator = "containerType"
        }

    override suspend fun onMessageReceived(
        deviceId: String,
        message: FloconIncomingMessageDataModel,
    ) {
        withContext(dispatcherProvider.data) {
            decode(message)?.let {
                println("MEOW - $it") // Polymorphic working! :D
                //MEOW - DashboardConfigDataModel(dashboardId=main, containers=[SectionContainerDataModel(name=Tmp section, containerId=, elements=[DashboardElementDataModel(button=ButtonConfigDataModel(text=tmp section button, id=main_tmp_section_button), text=null, plainText=null, textField=null, checkBox=null)]), FormContainerDataModel(name=Test form, containerId=main_form_Test form, elements=[DashboardElementDataModel(button=null, text=TextConfigDataModel(label=Test text, value=Test value, color=null), plainText=null, textField=null, checkBox=null), DashboardElementDataModel(button=null, text=null, plainText=null, textField=TextFieldConfigDataModel(label=Test text field, placeHolder=placeholder, value=, id=main_test_text_field), checkBox=null)], submitText=Submit form)])
                toDomain(it)
            }?.let { request ->
                println("MEOW - ${request.containers.map { it::class }}")
                dashboardLocalDataSource.saveDashboard(
                    deviceIdAndPackageName = DeviceIdAndPackageNameDomainModel(
                        deviceId = deviceId,
                        packageName = message.appPackageName,
                    ),
                    dashboard = request,
                )
            }
        }
    }

    private fun decode(message: FloconIncomingMessageDataModel): DashboardConfigDataModel? = try {
        dashboardParser.decodeFromString<DashboardConfigDataModel>(message.body)
    } catch (t: Throwable) {
        t.printStackTrace()
        null
    }

    override fun observeDashboard(
        deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel,
        dashboardId: DashboardId
    ): Flow<DashboardDomainModel?> = dashboardLocalDataSource.observeDashboard(
        deviceIdAndPackageName = deviceIdAndPackageName,
        dashboardId = dashboardId,
    )
        .flowOn(dispatcherProvider.data)

    override suspend fun sendClickEvent(
        deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel,
        dashboardId: DashboardId,
        buttonId: String
    ) {
        withContext(dispatcherProvider.data) {
            toDeviceDashboardDataSource.sendClickEvent(
                deviceIdAndPackageName = deviceIdAndPackageName,
                buttonId = buttonId,
            )
        }
    }

    override suspend fun submitFormEvent(
        deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel,
        dashboardId: DashboardId,
        formId: String,
        values: Map<String, String>,
    ) {
        withContext(dispatcherProvider.data) {
            toDeviceDashboardDataSource.submitFormEvent(
                deviceIdAndPackageName = deviceIdAndPackageName,
                formId = formId,
                values = values,
            )
        }
    }

    override suspend fun submitTextFieldEvent(
        deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel,
        dashboardId: DashboardId,
        textFieldId: String,
        value: String,
    ) {
        withContext(dispatcherProvider.data) {
            toDeviceDashboardDataSource.submitTextFieldEvent(
                deviceIdAndPackageName = deviceIdAndPackageName,
                textFieldId = textFieldId,
                value = value,
            )
        }
    }

    override suspend fun sendUpdateCheckBoxEvent(
        deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel,
        dashboardId: DashboardId,
        checkBoxId: String,
        value: Boolean,
    ) {
        withContext(dispatcherProvider.data) {
            toDeviceDashboardDataSource.sendUpdateCheckBoxEvent(
                deviceIdAndPackageName = deviceIdAndPackageName,
                checkBoxId = checkBoxId,
                value = value,
            )
        }
    }

    override suspend fun selectDeviceDashboard(
        deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel,
        dashboardId: DashboardId
    ) {
        withContext(dispatcherProvider.data) {
            deviceDashboardsDataSource.selectDeviceDashboard(
                deviceIdAndPackageName = deviceIdAndPackageName,
                dashboardId = dashboardId,
            )
        }
    }

    override fun observeSelectedDeviceDashboard(deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel): Flow<DashboardId?> =
        deviceDashboardsDataSource.observeSelectedDeviceDashboard(
            deviceIdAndPackageName = deviceIdAndPackageName,
        )

    override fun observeDeviceDashboards(deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel): Flow<List<DashboardId>> =
        dashboardLocalDataSource.observeDeviceDashboards(
            deviceIdAndPackageName = deviceIdAndPackageName,
        )
}
