package com.example.crowdin

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.internal.closeQuietly
import org.json.JSONObject
import java.net.URI
import java.time.Duration

var BASE_URL = "https://63da-117-193-79-225.ngrok-free.app"

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
    val id: Int
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
        val updatedAlerts = alertState.alerts.toMutableList()
        for (alert in alerts) {
            val index = updatedAlerts.indexOfFirst { it.id == alert.id }
            if (index != -1) {
                updatedAlerts[index] = alert
            } else {
                updatedAlerts.add(alert)
            }
        }
        alertState = alertState.copy(alerts = updatedAlerts)
    }

    fun sendAlert(
        lat: Double,
        lon: Double,
        message: String,
        title: String,
        severity: String,
        user: String,
        radius: Int
    ) {
        val time = System.currentTimeMillis()
        val alert = AlertObject(lat, lon, radius, message, title, time, severity, user, 0)
        addAlert(alert)

        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("$BASE_URL/add_alert")
            .post(
                okhttp3.RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    JSONObject(
                        mapOf(
                            "lat" to lat,
                            "lon" to lon,
                            "message" to message,
                            "title" to title,
                            "time" to time,
                            "severity" to severity,
                            "user" to user
                        )
                    ).toString()
                )
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
}

class ChatViewModel {
    var chatState by mutableStateOf(ChatState())

    fun addMessage(message: ChatMessage) {
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

    fun getLatestMessage(chatId: String): ChatMessage? {
        return chatState.messages.filter { it.chatId == chatId }.lastOrNull()
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
                                // {"chat_id": "chat1", "name": "Chat 1"}
                                val chat = chats.getJSONObject(i)
                                mut.value += Chat(chat.getString("chat_id"), chat.getString("name"))
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
                okhttp3.RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    JSONObject(
                        mapOf(
                            "chat_id" to chatId,
                            "message" to message,
                            "user" to sender,
                            "timestamp" to timestamp
                        )
                    ).toString()
                )
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
            //println("Alerts: $alertMessages")
            val alertsToInsert = mutableListOf<AlertObject>()
            for (i in 0 until alertMessages.length()) {
                val alert = alertMessages.getJSONObject(i)

                alertsToInsert.add(
                    AlertObject(
                        alert.getDouble("lat"),
                        alert.getDouble("lon"),
                        alert.getInt("radius"),
                        alert.getString("message"),
                        alert.getString("title"),
                        alert.getLong("time"),
                        alert.getString("severity"),
                        alert.getString("username"),
                        alert.getInt("id")
                    )
                )
                println("Alert: $alert")

            }
            AlertViewModel.upsertAlertData(alertsToInsert)
        }

        override fun onSSEError(t: Throwable) {
            Log.e(AppName, "SSE error: ${t.message}")
        }
    }

    fun initSse(sseHandler: SSEHandler) {
        this.sseHandlers = sseHandler
        val eventHandler = sseHandlers?.let { DefaultEventHandler(it) }
        val PATH = "/chat"
        try {
            eventSourceSse = EventSource.Builder(
                eventHandler, URI.create("$BASE_URL$PATH")
            )
                .connectTimeout(Duration.ofSeconds(3))
                .backoffResetThreshold(Duration.ofSeconds(3))
                .build()

            eventSourceSse?.start()
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

    fun disconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                eventSourceSse?.closeQuietly()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}