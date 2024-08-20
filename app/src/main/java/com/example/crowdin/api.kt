package com.example.crowdin

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.model.LatLng
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.URI
import java.time.Duration

var BASE_URL = "https://running-krill-amarnathcjd-d8e56e16.koyeb.app"

var sseClient = SSEClient()
data class ChatMessage(
    val chatId: String,
    val message: String,
    val sender: String,
    val timestamp: Long
)

data class AlertObject(
    val lat: Double,
    val lon: Double,
    val radius: Int,
    val message: String,
    val title: String,
    val time: Long,
    val severity: String,
    val user: String,
    val id: Int,
    val icon: String,
    var upVotes: Int = 0,
    var downVotes: Int = 0,
    var probability: Double = 0.0
)

data class ChatState(
    val messages: List<ChatMessage> = emptyList()
)

data class AlertState(
    val alerts: List<AlertObject> = emptyList()
)

class AlertViewModel {
    var alertState by mutableStateOf(AlertState())

    fun addAlert(alert: AlertObject) {
        alertState = alertState.copy(alerts = alertState.alerts + alert)
    }

    fun getAllAlerts(): List<AlertObject> {
        return alertState.alerts
    }

    fun upsertAlertData(alerts: List<AlertObject>) {
        val isFirstReqAfterInit = alertState.alerts.isEmpty()
        val updatedAlerts = alertState.alerts.toMutableList()
        for (alert in alerts) {
            if (updatedAlerts.none { it.id == alert.id }) {
                if (!isFirstReqAfterInit && alert.user != userName.value && alert.time > (System.currentTimeMillis() - 30000)) {
                    notification.value = Notification(alert.title, alert.message)
                    isTherePendingAlert.value = true
                }
                updatedAlerts.add(alert)
            }
        }

        alertState = alertState.copy(alerts = updatedAlerts)
    }

    fun getRoadData(lat: Double, lon: Double) {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/ways_data?lat=$lon&lon=$lat")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e(AppName, "Failed to get road data: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.e(AppName, "Failed to get road data: ${response.code}")
                } else {
                    try {
                        val json = response.body?.string()?.let { JSONObject(it) }
                        val items = json?.getJSONArray("details")
                        if (items != null) {
                            for (i in 0 until items.length()) {
                                val item = items.getJSONObject(i)
                                if (item.getString("type") == "ROAD_CLOSED") {
                                    roadData.roadClosed.add(
                                        RoadClosedData(
                                            item.getDouble("lat"),
                                            item.getDouble("lon"),
                                        )
                                    )
                                } else if (item.getString("type") == "JAM") {
                                    val lines = item.getJSONArray("line")
                                    val points = mutableListOf<LatLng>()
                                    for (j in 0 until lines.length()) {
                                        val line = lines.getJSONObject(j)
                                        points.add(
                                            LatLng(
                                                line.getDouble("lat"),
                                                line.getDouble("lon")
                                            )
                                        )
                                    }
                                    roadData.roadBlocked.add(
                                        RoadBlockedData(
                                            points,
                                            item.getString("street"),
                                            item.getInt("speed")
                                        )
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(AppName, "Failed to parse road data: ${e.message}")
                        return
                    }
                }
            }
        })
    }

    fun sendAlert(
        lat: Double,
        lon: Double,
        message: String,
        title: String,
        severity: String,
        user: String,
        radius: Int,
        type: String,
    ) {
        val time = System.currentTimeMillis()
        val alert = AlertObject(lat, lon, radius, message, title, time, severity, user, 0, "")
        addAlert(alert)

        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/add_alert")
            .post(
                JSONObject(
                    mapOf(
                        "type" to type,
                        "lat" to lat,
                        "lon" to lon,
                        "message" to message,
                        "title" to title,
                        "time" to time,
                        "severity" to severity,
                        "username" to user,
                        "radius" to radius,
                        "media" to ""
                    )
                ).toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e(AppName, "Failed to send alert: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.e(AppName, "Failed to send alert: ${response.code}")
                }
            }
        })
    }

    fun upvoteAlert(alertId: Int, dec: Boolean = false) {
        alertState.alerts.find { it.id == alertId }?.upVotes =
            alertState.alerts.find { it.id == alertId }?.upVotes?.plus(1) ?: 0
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/upvote_alert")
            .post(
                JSONObject(
                    mapOf(
                        "alert_id" to alertId,
                        "dec" to dec
                    )
                ).toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e(AppName, "Failed to upvote alert: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.e(AppName, "Failed to upvote alert: ${response.code}")
                }
            }
        })
    }

    fun downvoteAlert(alertId: Int, dec: Boolean = false) {
        alertState.alerts.find { it.id == alertId }?.downVotes =
            alertState.alerts.find { it.id == alertId }?.downVotes?.plus(1) ?: 0
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/downvote_alert")
            .post(
                JSONObject(
                    mapOf(
                        "alert_id" to alertId,
                        "dec" to dec
                    )
                ).toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e(AppName, "Failed to downvote alert: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.e(AppName, "Failed to downvote alert: ${response.code}")
                }
            }
        })
    }
}

data class Emergency(
    val lat: Double,
    val lon: Double,
    val username: String,
    val active: Boolean = true,
    val emergencyId: String
)

class RecepModel {
    var recepsState by mutableStateOf(emptyList<Recep>())
    fun upsertRecep(receps: List<Recep>) {
        val updatedReceps = recepsState.toMutableList()
        for (recep in receps) {
            if (updatedReceps.none { it.lat == recep.lat && it.lon == recep.lon }) {
                updatedReceps.add(recep)
            } else {
                val index =
                    updatedReceps.indexOfFirst { it.lat == recep.lat && it.lon == recep.lon }
                updatedReceps[index] = recep
            }
        }

        recepsState = updatedReceps
    }

    fun sendRecep(lat: Double, lon: Double, crowded: Boolean, emergencyId: String) {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/update_recep")
            .post(
                JSONObject(
                    mapOf(
                        "lat" to lat,
                        "lon" to lon,
                        "username" to userName.value,
                        "crowded" to crowded,
                        "emergency_id" to emergencyId
                    )
                ).toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e(AppName, "Failed to send recep: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.e(AppName, "Failed to send recep: ${response.code}")
                }
            }
        })
    }
}

class EmergencyModel {
    var emergenciesState by mutableStateOf(emptyList<Emergency>())

    fun upsertEmergencies(emergencies: List<Emergency>) {
        val ifIsFirstReqAfterInit = emergenciesState.isEmpty() && emergencies.size > 1
        val updatedEmergencies = emergenciesState.toMutableList()
        for (emergency in emergencies) {
            if (updatedEmergencies.none { it.username == emergency.username && it.active }) {
                if (emergency.username != userName.value && emergency.active && !ifIsFirstReqAfterInit) {
                    notification.value = Notification(
                        "Ambulance Nearby",
                        "There is an ambulance nearby, please make way",
                        true
                    )
                    isTherePendingAlert.value = true
                }
                updatedEmergencies.add(emergency)
            } else {
                if (!ifIsFirstReqAfterInit) {
                    val index =
                        updatedEmergencies.indexOfFirst { it.username == emergency.username }
                    if (updatedEmergencies[index].active != emergency.active && emergency.active) {
                        updatedEmergencies[index] = emergency
                        notification.value = Notification(
                            "Ambulance Nearby",
                            "There is an ambulance nearby, please make way",
                            true
                        )
                        isTherePendingAlert.value = true
                    }
                }
            }
        }
        emergenciesState = updatedEmergencies
    }
}

class ChatViewModel {
    private var chatState by mutableStateOf(ChatState())

    private fun addMessage(message: ChatMessage) {
        chatState = chatState.copy(messages = chatState.messages + message)
    }

    fun upsertMessage(message: ChatMessage) {
        val updatedMessages = chatState.messages.toMutableList()
        val index = updatedMessages.indexOfFirst { it.chatId == message.chatId }
        if (index != -1) {
            updatedMessages[index] = message
        } else {
            updatedMessages.add(message)
        }
        chatState = chatState.copy(messages = updatedMessages)
    }

    fun upsertChat(chatId: String, messages: List<ChatMessage>) {
        val chatMessages = chatState.messages.filter { it.chatId == chatId }
        for (message in messages) {
            if (chatMessages.none { it.timestamp == message.timestamp }) {
                chatState = chatState.copy(messages = chatState.messages + message)
            }
        }
    }

    fun clearMessages() {
        chatState = chatState.copy(messages = emptyList())
    }

    fun getMessages(chatId: String): List<ChatMessage> {
        return chatState.messages.filter { it.chatId == chatId }
    }

    fun getChatsList(mut: MutableState<List<Chat>>) {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/get_chats")
            .build()

        client.newCall(request).enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    Log.e(AppName, "Failed to get chats: ${e.message}")
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (!response.isSuccessful) {
                        Log.e(AppName, "Failed to get chats: ${response.code}")
                    } else {
                        val json = response.body?.string()?.let { JSONObject(it) }
                        val chats = json?.getJSONArray("chats")
                        if (chats != null) {
                            for (i in 0 until chats.length()) {
                                val chat = chats.getJSONObject(i)
                                mut.value += Chat(
                                    chat.getInt("id"),
                                    chat.getString("title"),
                                    chat.getString("message")
                                )
                            }
                        }
                    }
                }
            }
        )
    }

    fun sendMessage(chatId: String, message: String, sender: String) {
        val timestamp = System.currentTimeMillis()
        val chatMessage = ChatMessage(chatId, message, sender, timestamp)
        addMessage(chatMessage)

        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/add_message")
            .post(
                JSONObject(
                    mapOf(
                        "chat_id" to chatId,
                        "message" to message,
                        "user" to sender,
                        "timestamp" to timestamp
                    )
                ).toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e(AppName, "Failed to send message: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.e(AppName, "Failed to send message: ${response.code}")
                }
            }
        })
    }

    fun delEmergency(username: String) {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/delete_emergency")
            .post(
                JSONObject(
                    mapOf(
                        "username" to username
                    )
                ).toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e(AppName, "Failed to delete emergency: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.e(AppName, "Failed to delete emergency: ${response.code}")
                }
            }
        })
    }

    fun EmergencyClearanceReq(lat: Double, lon: Double, active: Boolean) {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/emergency_clearance")
            .post(
                JSONObject(
                    mapOf(
                        "lat" to lat,
                        "lng" to lon,
                        "username" to userName.value,
                        "active" to active
                    )
                ).toString()
                    .toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e(AppName, "Failed to send emergency clearance request: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.e(AppName, "Failed to send emergency clearance request: ${response.code}")
                }
            }
        })
    }
}

interface SSEHandler {
    fun onSSEConnectionOpened()
    fun onSSEConnectionClosed()
    fun onSSEEventReceived(event: String, messageEvent: MessageEvent)
    fun onSSEError(t: Throwable)
}

class SSEClient {
    private var sseHandlers: SSEHandler? = null
    private var eventSourceSse: EventSource? = null

    var ChatViewModel = ChatViewModel()
    var AlertViewModel = AlertViewModel()
    var EmergencyModel = EmergencyModel()
    var RecepModel = RecepModel()

    var handler = object : SSEHandler {
        override fun onSSEConnectionOpened() {
            Log.d(AppName, "SSE connection opened")
        }

        override fun onSSEConnectionClosed() {
            Log.d(AppName, "SSE connection closed")
        }

        override fun onSSEEventReceived(event: String, messageEvent: MessageEvent) {
            Log.d(AppName, "SSE event received: $event")
            val data = messageEvent.data.replace("data: ", "")
            val dataJson = JSONObject(data)
            val chatMessages = dataJson.getJSONArray("messages")
            for (i in 0 until chatMessages.length()) {
                val messages = chatMessages.getJSONObject(i).getJSONArray("messages")
                val chatId = chatMessages.getJSONObject(i).getString("chat_id")
                val messagesToInsert = mutableListOf<ChatMessage>()
                for (j in 0 until messages.length()) {
                    val message = messages.getJSONObject(j)
                    messagesToInsert.add(
                        ChatMessage(
                            chatId,
                            message.getString("message"),
                            message.getString("username"),
                            message.getLong("timestamp")
                        )
                    )
                }
                ChatViewModel.upsertChat(chatId, messagesToInsert)
            }

            val alertMessages = dataJson.getJSONArray("alerts")
            val alertsToInsert = mutableListOf<AlertObject>()
            for (i in 0 until alertMessages.length()) {
                val alert = alertMessages.getJSONObject(i)
                var icn = ""
                if (alert.has("icon")) {
                    icn = alert.getString("icon")
                }

                val alertObj = AlertObject(
                    alert.getDouble("lat"),
                    alert.getDouble("lon"),
                    alert.getInt("radius"),
                    alert.getString("message"),
                    alert.getString("title"),
                    alert.getLong("time"),
                    alert.getString("severity"),
                    alert.getString("username"),
                    alert.getInt("id"),
                    icn
                )

                if (alert.has("upvotes")) {
                    alertObj.upVotes = alert.getInt("upvotes")
                }

                if (alert.has("downvotes")) {
                    alertObj.downVotes = alert.getInt("downvotes")
                }

                if (alert.has("probability")) {
                    alertObj.probability = alert.getDouble("probability")
                }

                alertsToInsert.add(
                    alertObj
                )
            }

            AlertViewModel.upsertAlertData(alertsToInsert)
            val emergencies = dataJson.getJSONArray("emergencies")
            val emergenciesToInsert = mutableListOf<Emergency>()
            for (i in 0 until emergencies.length()) {
                val emergency = emergencies.getJSONObject(i)
                emergenciesToInsert.add(
                    Emergency(
                        emergency.getDouble("lat"),
                        emergency.getDouble("lng"),
                        emergency.getString("username"),
                        emergency.getBoolean("active"),
                        emergency.getString("emergency_id"),
                    )
                )
            }

            EmergencyModel.upsertEmergencies(emergenciesToInsert)

            val receps = dataJson.getJSONArray("receps")
            val recepsToInsert = mutableListOf<Recep>()
            for (i in 0 until receps.length()) {
                val recep = receps.getJSONObject(i)
                recepsToInsert.add(
                    Recep(
                        recep.getDouble("lat"),
                        recep.getDouble("lng"),
                        recep.getBoolean("crowded"),
                        recep.getString("emergency_id")
                    )
                )
            }

            RecepModel.upsertRecep(recepsToInsert)
        }

        override fun onSSEError(t: Throwable) {
            Log.e(AppName, "SSE error: ${t.message}")
        }
    }

    fun initSse(sseHandler: SSEHandler) {
        this.sseHandlers = sseHandler
        val eventHandler = sseHandlers?.let { DefaultEventHandler(it) }
        try {
            eventSourceSse = EventSource.Builder(
                eventHandler, URI.create("$BASE_URL/chat")
            )
                .connectTimeout(Duration.ofSeconds(3))
                .backoffResetThreshold(Duration.ofSeconds(3))
                .build()

            eventSourceSse?.start()
            println("SSE Event Handler Started")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private class DefaultEventHandler(private val sseHandler: SSEHandler) : EventHandler {
        override fun onOpen() {
            sseHandler.onSSEConnectionOpened()
        }

        override fun onClosed() {
            sseHandler.onSSEConnectionClosed()
        }

        override fun onMessage(event: String, messageEvent: MessageEvent) {
            sseHandler.onSSEEventReceived(event, messageEvent)
        }

        override fun onError(t: Throwable) {
            sseHandler.onSSEError(t)
        }

        override fun onComment(comment: String) {
            Log.i("SSE_CONNECTION", comment)
        }
    }
}

fun updateUserLocation(username: String, lat: Double, lon: Double) {
    val client = OkHttpClient()
    val request = okhttp3.Request.Builder()
        .url("$BASE_URL/update_user_location")
        .post(
            JSONObject(
                mapOf(
                    "username" to username,
                    "lat" to lat,
                    "lon" to lon
                )
            ).toString()
                .toRequestBody("application/json".toMediaTypeOrNull())
        )
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            Log.e(AppName, "Failed to update location: ${e.message}")
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (!response.isSuccessful) {
                Log.e(AppName, "Failed to update location: ${response.code}")
            }
        }
    })
}

fun getNearbyUsers(
    username: String,
    radius: Int,
    nearbyUsersCount: MutableState<Int>,
    scanning: MutableState<Boolean>
) {
    val client = OkHttpClient()
    val request = okhttp3.Request.Builder()
        .url("$BASE_URL/count_nearby_peoples")
        .post(
            JSONObject(
                mapOf(
                    "username" to username,
                    "radius" to radius
                )
            ).toString()
                .toRequestBody("application/json".toMediaTypeOrNull())
        )
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            Log.e(AppName, "Failed to get nearby users: ${e.message}")
            scanning.value = false
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if (!response.isSuccessful) {
                Log.e(AppName, "Failed to get nearby users: ${response.code}")
                scanning.value = false
            } else {
                val json = response.body?.string()?.let { JSONObject(it) }
                nearbyUsersCount.value = json?.getInt("count") ?: 0
                scanning.value = false
            }
        }
    })
}