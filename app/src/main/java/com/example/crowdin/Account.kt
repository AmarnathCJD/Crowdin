package com.example.crowdin


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun AccountMain(nav: NavController) {
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
                            color = ColorPalette.secondary,
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
                            color = ColorPalette.redish,
                            name = "Account",
                            nav = nav
                        )
                    }
                }
            },
            containerColor = Color.Transparent,
            content = {
                Account(it)
            }
        )
    }
}

@Composable
fun Account(padding: PaddingValues) {
    val alertTitle = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding() - 30.dp,
                bottom = padding.calculateBottomPadding()
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp)
                .background(Color.White)
                .clip(shape = RoundedCornerShape(10.dp))

                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Accounts & Settings", style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color.White)
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                contentColor = Color.Black,
                containerColor = Color(0xFFEDE7F6)

            ),
            border = BorderStroke(1.dp, Color.Black)

        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .background(Color.White)
                        .size(130.dp)
                        .padding(0.dp, 0.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Image(
                        painterResource(id = R.drawable.jenna),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Black, CircleShape)
                            .clip(CircleShape)
                    )

                }



                Text(
                    modifier = Modifier.padding(16.dp, 0.dp),
                    text = userName.value,
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "email: ${userName.value}@gmail.com", style = TextStyle(
                        fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )


            }

        }
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .padding(5.dp)
                .fillMaxWidth()
        )

        val update_password = remember { mutableStateOf("") }
        val confirm_password = remember { mutableStateOf("") }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                contentColor = Color.Black,
                containerColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 5.dp)
                    .height(40.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Change Password", style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp
                    ), textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 2.dp)
                    .height(50.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = update_password.value,
                    onValueChange = { update_password.value = it },
                    label = { Text("Enter New Password") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Black,
                        disabledPlaceholderColor = Color.Gray,
                        errorIndicatorColor = Color.Red,
                        focusedPlaceholderColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 2.dp)
                    .height(40.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Confirm Password", style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp
                    ), textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 5.dp)
                    .height(50.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirm_password.value,
                    onValueChange = { confirm_password.value = it },
                    label = { Text("Confirm New Password") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Black,
                        disabledPlaceholderColor = Color.Gray,
                        errorIndicatorColor = Color.Red,
                        focusedPlaceholderColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                )
            }
            var showAlertDialog by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(40.dp)
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (update_password.value == confirm_password.value) {
                            val client = OkHttpClient()
                            val url = "${BASE_URL}/user/update_password"
                            val request = Request.Builder()
                                .url(url)
                                .post(
                                    JSONObject(
                                        mapOf(
                                            "username" to userName.value,
                                            "password" to update_password.value
                                        )
                                    ).toString()
                                        .toRequestBody("application/json".toMediaTypeOrNull())
                                )
                                .build()
                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    alertTitle.value = "Failed to update password"
                                    showAlertDialog = true
                                }

                                override fun onResponse(call: Call, response: okhttp3.Response) {
                                    val body = response.body?.string()
                                    println("Response Body: $body")

                                    if (response.code == 200) {
                                        alertTitle.value = "200"
                                        showAlertDialog = true

                                    } else {
                                        alertTitle.value = "Failed to update password"
                                        showAlertDialog = true
                                    }
                                }
                            })
                        } else {
                            showAlertDialog = true
                        }

                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save Changes")
                }
            }
            if (showAlertDialog) {
                AlertDialog(
                    modifier = Modifier.padding(16.dp),
                    onDismissRequest = { showAlertDialog = false },
                    title = { Text("Password ") },
                    text = {
                        if (alertTitle.value == "200") {
                            Text("Password updated successfully")
                        } else {
                            Text("Passwords do not match")
                        }

                    },
                    confirmButton = {
                        Button(
                            onClick = { showAlertDialog = false }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }
        }
        var AccidentCheck by remember { mutableStateOf(false) }
        var DisasterCheck by remember { mutableStateOf(false) }
        var AnimalChecked by remember { mutableStateOf(false) }
        var CycloneChecked by remember { mutableStateOf(false) }
        var otherAlertChecked by remember { mutableStateOf(false) }

        Row {
            var expanded by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true }),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Select alert preference", style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp
                    )
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }

            DropdownMenu(modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White),
                offset = DpOffset(50.dp, 0.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("Accident Alert") },
                    onClick = { AccidentCheck = !AccidentCheck },
                    trailingIcon = {
                        Checkbox(checked = AccidentCheck, onCheckedChange = { AccidentCheck = it })
                    })
                DropdownMenuItem(text = { Text("Disaster Alert") },
                    onClick = { DisasterCheck = !DisasterCheck },
                    trailingIcon = {
                        Checkbox(checked = DisasterCheck, onCheckedChange = { DisasterCheck = it })
                    })
                DropdownMenuItem(
                    text = { Text("Cyclone Alert") },
                    onClick = { CycloneChecked = !CycloneChecked },
                    trailingIcon = {
                        Checkbox(checked = CycloneChecked,
                            onCheckedChange = { CycloneChecked = it })
                    }
                )
                DropdownMenuItem(text = { Text("Animal Alert") },
                    onClick = { AnimalChecked = !AnimalChecked },
                    trailingIcon = {
                        Checkbox(checked = AnimalChecked,
                            onCheckedChange = { AnimalChecked = it })
                    })

                // Third Checkbox Option
                DropdownMenuItem(text = { Text("Other Alert") },
                    onClick = { otherAlertChecked = !otherAlertChecked },
                    trailingIcon = {
                        Checkbox(checked = otherAlertChecked,
                            onCheckedChange = { otherAlertChecked = it })
                    })
            }

        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp, 5.dp)
//                .height(40.dp)
//                .background(Color.Transparent),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Button(onClick = { /*TODO*/ }) {
//                Text("Save Changes")
//
//            }
//
//        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 5.dp)
                .height(40.dp)
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select Alert Radius", style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp
                ), textAlign = TextAlign.Center
            )
        }
        val AlertDistance = remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 2.dp)
                .height(50.dp)
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically

        ) {
            OutlinedTextField(
                value = AlertDistance.value,
                onValueChange = { AlertDistance.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Preferred Alert Radius in KM") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Black,
                    disabledPlaceholderColor = Color.Gray,
                    errorIndicatorColor = Color.Red,
                    focusedPlaceholderColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
            )
        }
        if (AccidentCheck == DisasterCheck == CycloneChecked == AnimalChecked == otherAlertChecked) {
            val jasonObject = JSONObject().apply {
                put("username", userName.value)

                put("alert_preferences", JSONArray().apply {
                    if (AccidentCheck == DisasterCheck == CycloneChecked == AnimalChecked == otherAlertChecked) {
                        put("1")
                    } else {
                        put("0")
                    }
                    if (AccidentCheck) {
                        put("1")
                    } else {
                        put("0")
                    }
                    if (DisasterCheck) {
                        put("1")
                    } else {
                        put("0")
                    }
                    if (CycloneChecked) {
                        put("1")
                    } else {
                        put("0")
                    }
                    if (AnimalChecked) {
                        put("1")
                    } else {
                        put("0")
                    }
                    if (otherAlertChecked) {
                        put("1")
                    } else {
                        put("0")
                    }
                    if (AlertDistance.value.isNotEmpty()) {
                        put(AlertDistance.value)
                    } else {
                        put("0")
                    }

                })

            }
            val requestBody =
                jasonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("${BASE_URL}/user/update_alert_preferences")
                .post(requestBody)
                .build()
            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Failed to execute request")
                    alertTitle.value = "Failed to update alert preferences"
                }

                override fun onResponse(call: Call, response: okhttp3.Response) {
                    val body = response.body?.string()
                    val u = requestBody.toString()
                    println("Response Body: $body")
                    println("Request Body: $u")

                    if (response.code == 200) {
                        alertTitle.value = "200"

                    } else {
                        alertTitle.value = "Failed to update alert preferences"
                    }
                }
            })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(40.dp)
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Save Changes")
            }
        }
    }
}



