package com.example.crowdin

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class CrowdEvent {
    var eventId: Int = 0
    var title: String = ""
}

@Composable
fun NearbyUsersMain(nav: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White,
            )
    ) {
        Scaffold(
            topBar = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color(0xFF05445e),
                        modifier = Modifier.size(28.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow",
                        tint = Color(0xFF05445e),
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            bottomBar = {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(bottom = 0.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ColorPalette.lightSurface)
                            .alpha(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                1.dp,
                                ColorPalette.onPrimary,
                                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                            )
                    ) {
                        BottomIconItem(
                            imageRes = R.drawable.forum_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.secondary,
                            name = "Chats",
                            nav = nav
                        )
                        BottomIconItem(
                            imageRes = R.drawable.notifications_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.secondary,
                            name = "Alerts",
                            nav = nav
                        )
                        BottomIconItem(
                            imageRes = R.drawable.roofing_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.redish,
                            name = "Home",
                            nav = nav
                        )
                        BottomIconItem(
                            imageRes = R.drawable.my_location_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.secondary,
                            name = "Location",
                            nav = nav
                        )
                        BottomIconItem(
                            imageRes = R.drawable.admin_panel_settings_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.secondary,
                            name = "Account",
                            nav = nav
                        )
                    }
                }
            },
            containerColor = Color.Transparent,
            content = {
                NearbyUsers(it, nav)
            }
        )
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyUsers(padding: PaddingValues, nav: NavController) {
    Column(
        modifier = Modifier.padding(
            top = padding.calculateTopPadding() - 30.dp,
            bottom = padding.calculateBottomPadding(),
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 16.dp)
                .background(color = Color(0xFFF3E5F5), shape = RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.nearby_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "Location Icon",
                modifier = Modifier.padding(vertical = 12.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFF311B92))
            )
            Text(
                "Nearby Users",
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF311B92),
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Card(
                colors = CardColors(
                    containerColor = Color(0xFFFFCA28),
                    contentColor = Color.White,
                    disabledContentColor = Color(0xFF6A1B9A),
                    disabledContainerColor = Color(0xFFE1BEE7),
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 16.dp, start = 16.dp, end = 16.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(45.dp)
                    .clickable {
                        nav.navigate("AddAlert")
                    },
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = "Report Something",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .background(color = Color(0xFFEDE7F6), shape = RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Find the count of users near you, in case of emergency",
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF311B92),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Text(
            "Enter the Event ID to find nearby users",
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF311B92),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        val eventId = remember { mutableStateOf("") }
        val showError = remember { mutableStateOf(false) }
        val errorMessage = remember { mutableStateOf("") }
        val cev = remember { CrowdEvent() }
        val scanOnGoing = remember { mutableStateOf(false) }
        val showSuccess = remember { mutableStateOf(false) }
        val nearbyCount = remember { mutableStateOf(0) }

        TextField(
            value = eventId.value, onValueChange = { eventId.value = it },
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 6.dp)
                .fillMaxWidth(),
            label = { Text("Event ID") },
            colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFEDE7F6),
                focusedIndicatorColor = Color(0xFF311B92),
                unfocusedIndicatorColor = Color(0xFF311B92)
            ),
            shape = RoundedCornerShape(8.dp)
        )

        if (showError.value) {
            Text(
                errorMessage.value,
                modifier = Modifier.padding(16.dp),
                color = Color(0xFFD32F2F),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        val sliderValue = remember { mutableFloatStateOf(0.01f) }

        Text(
            "Select the radius range to search for",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            color = Color(0xFF311B92),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Slider(
            value = sliderValue.floatValue,
            onValueChange = { sliderValue.floatValue = it },
            valueRange = 0f..0.25f,
            steps = 15,
            modifier = Modifier
                .padding(16.dp)
                .padding(horizontal = 24.dp),
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = Color(0xFF311B92),
                activeTrackColor = Color(0xFF311B92),
                inactiveTrackColor = Color(0xFFC5CAE9)
            )
        )

        Text(
            "Radius: ${String.format("%.0f", sliderValue.floatValue * 100)} km",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color(0xFF311B92),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        ElevatedButton(
            onClick = {
                showSuccess.value = false
                if (eventId.value.isEmpty()) {
                    showError.value = true
                    errorMessage.value = "Event ID cannot be empty"
                } else {
                    showError.value = false
                    errorMessage.value = ""

                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url("$BASE_URL/get_event")
                        .post(
                            JSONObject(
                                mapOf(
                                    "event_id" to eventId.value.toInt()
                                )
                            ).toString().toRequestBody("application/json".toMediaType())
                        )
                        .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            showError.value = true
                            println("Failed to execute request")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (!response.isSuccessful) {
                                showError.value = true
                                errorMessage.value = "Failed to get event details"
                                return
                            }
                            val body = response.body?.string()
                            val json = body?.let { JSONObject(it) }
                            if (json != null && json.has("event")) {
                                val event = json.getJSONObject("event")
                                cev.eventId = event.getInt("event_id")
                                cev.title = event.getString("title")
                                if (!scanOnGoing.value) {
                                    scanOnGoing.value = true
                                    getNearbyUsers(
                                        userName.value,
                                        (sliderValue.floatValue * 100).toInt(),
                                        nearbyCount,
                                        scanOnGoing
                                    )
                                }
                                showSuccess.value = true
                            } else {
                                showError.value = true
                                errorMessage.value = "Invalid Event ID"
                            }
                        }
                    })
                }
            },
            modifier = Modifier.padding(24.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (scanOnGoing.value) Color(0xFF7E57C2) else
                    Color(
                    0xFF9575CD
                ),
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, Color(0xFF311B92))
        ) {
            Text(
                if (scanOnGoing.value) ".... Nearby Users" else "Scan Nearby Users",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .background(color = Color(0xFFD1C4E9), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column {
                if (showSuccess.value)
                    Text(
                        "Event-ID: ${cev.eventId} | Event-Title: ${cev.title}",
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 15.dp)
                            .align(Alignment.CenterHorizontally),
                        color = Color(0xFF311B92),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                Text(
                    nearbyCount.value.toString() + " - U",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color(0x706200EA),
                    modifier = Modifier
                        .padding(horizontal = 80.dp, vertical = 15.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        Text(
            "Note: The count of users is approximate and may vary",
            modifier = Modifier.padding(32.dp),
            color = Color(0xFF311B92),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}