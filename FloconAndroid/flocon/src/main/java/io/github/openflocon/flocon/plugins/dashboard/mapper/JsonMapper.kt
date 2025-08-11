package io.github.openflocon.flocon.plugins.dashboard.mapper

import io.github.openflocon.flocon.plugins.dashboard.model.ContainerType
import io.github.openflocon.flocon.plugins.dashboard.model.DashboardCallback
import io.github.openflocon.flocon.plugins.dashboard.model.DashboardCallback.*
import io.github.openflocon.flocon.plugins.dashboard.model.DashboardConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.ButtonConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.CheckBoxConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.ContainerConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.ElementConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.FormConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.PlainTextConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.SectionConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.TextConfig
import io.github.openflocon.flocon.plugins.dashboard.model.config.TextFieldConfig
import org.json.JSONArray
import org.json.JSONObject

fun DashboardConfig.toJson(
    registerCallback: (DashboardCallback) -> Unit,
): JSONObject {
    val rootJson = JSONObject()

    rootJson.put("dashboardId", id)

    val containersJsonArray = JSONArray()

    containers.forEach { container ->
        val sectionJson = container.toJson(
            dashboardId = id,
            registerCallback = registerCallback,
        )
        containersJsonArray.put(sectionJson)
    }

    rootJson.put("containers", containersJsonArray)
    return rootJson
}


internal fun ContainerConfig.toJson(
    registerCallback: (DashboardCallback) -> Unit,
    dashboardId: String,
): JSONObject {
    val containerJson = JSONObject()
    containerJson.put("name", this.name)
    containerJson.put("containerType", when (this) {
        is FormConfig -> ContainerType.FORM
        is SectionConfig -> ContainerType.SECTION
    })

    when (this) {
        is FormConfig -> {
            val actionId = dashboardId + "_" + this.id
            registerCallback(
                FormCallback(
                    id = actionId,
                    actions = this.onSubmitted
                )
            )
        }

        else -> Unit
    }

    val elements = JSONArray(elements.map {
        parseElementConfig(
            element = it,
            registerCallback = registerCallback,
            dashboardId = dashboardId,
        )
    })
    containerJson.put("elements", elements)
    return containerJson
}

private fun parseElementConfig(
    element: ElementConfig,
    registerCallback: (DashboardCallback) -> Unit,
    dashboardId: String,
): JSONObject = when (element) {
    is ButtonConfig -> {
        val actionId = dashboardId + "_" + element.id
        registerCallback(
            ButtonCallback(
                id = actionId,
                action = element.onClick,
            )
        )
        element.toJson(
            actionId = actionId,
        )
    }

    is TextConfig -> element.toJson()
    is PlainTextConfig -> element.toJson()
    is TextFieldConfig -> {
        val actionId = dashboardId + "_" + element.id

        registerCallback(
            TextFieldCallback(
                id = actionId,
                action = element.onSubmitted,
            )
        )
        element.toJson(
            actionId = actionId
        )
    }

    is CheckBoxConfig -> {
        val actionId = dashboardId + "_" + element.id
        registerCallback(
            CheckBoxCallback(
                id = actionId,
                action = element.onUpdated,
            )
        )
        element.toJson(
            actionId = actionId,
        )
    }
}

// {
//     "button" : {
//         "text": "click me",
//         "id": "1"
//     }
// }
internal fun ButtonConfig.toJson(actionId: String): JSONObject {
    return JSONObject().apply {
        put("button", JSONObject().apply {
            put("text", text)
            put("id", actionId)
        })
    }
}

// {
//     "text" : {
//         "label": "user id",
//         "value": "01010101010"
//     }
// }
internal fun TextConfig.toJson(): JSONObject {
    return JSONObject().apply {
        put("text", JSONObject().apply {
            put("label", label)
            put("value", value)
            color?.let {
                put("color", it)
            }
        })
    }
}

// {
//     "textField" : {
//         "id": "1",
//         "label": "update name",
//         "placeHolder": "new name",
//         "value: "florent",
//     }
// }
internal fun TextFieldConfig.toJson(actionId: String): JSONObject {
    return JSONObject().apply {
        put("textField", JSONObject().apply {
            put("id", actionId)
            put("label", label)
            put("placeHolder", placeHolder)
            put("value", value)
        })
    }
}


// {
//     "checkBox" : {
//         "id": "1",
//         "label": "update name",
//         "value: "florent",
//     }
// }
internal fun CheckBoxConfig.toJson(actionId: String): JSONObject {
    return JSONObject().apply {
        put("checkBox", JSONObject().apply {
            put("id", actionId)
            put("label", label)
            put("value", value)
        })
    }
}

// {
//     "plainText" : {
//         "label": "user id",
//         "value": "01010101010",
//         "type": "text" / "json"
//     }
// }
internal fun PlainTextConfig.toJson(): JSONObject {
    return JSONObject().apply {
        put("text", JSONObject().apply {
            put("label", label)
            put("value", value)
            put("type", type)
        })
    }
}
