package com.example.crowdin

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var nearbyAlertsEnabled = mutableStateOf(true)
var userName = mutableStateOf("rlx")

@Composable
fun Home(nav: NavController) {
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
                HomeMain(it, nav)
            }
        )
    }
}

@Composable
fun BottomIconItem(imageRes: Int, color: Color, name: String = "Icon", nav: NavController? = null) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    nav?.navigate(name)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource(id = imageRes),
                contentDescription = "Icon",
                modifier = Modifier.size(28.dp),
                tint = color
            )
            Text(
                text = name,
                color = Color(0xFF05445e),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HomeMain(it: PaddingValues, nav: NavController) {
    Column(
        modifier = Modifier
            .padding(
                top = it.calculateTopPadding() - 30.dp,
                bottom = it.calculateBottomPadding(),
            )
            .verticalScroll(rememberScrollState())
    ) {

        WelcomeMessage(loggedInUser = "@" + userName.value)

        val locationName = remember { mutableStateOf("Unknown") }
        val latlng = remember { mutableStateOf(Pair(0.0, 0.0)) }

        LocationDisplay(locationName.value, latlng.value.first, latlng.value.second)
        AutoUpdatingClock()
        PlaySOS()

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            ActionButton(
                backgroundColor =

                Color(0xFFEF5350),
                iconRes = R.drawable.sos_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                contentDescription = "Add",
                click = {
                    SOSSoundActive.value = !SOSSoundActive.value
                }
            )
            if (nearbyAlertsEnabled.value) {
                ActionButton(
                    backgroundColor =
                    Color(0xFF42A5F5),
                    iconRes = R.drawable.notifications_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                    contentDescription = "Alert",
                    click = {
                        nearbyAlertsEnabled.value = !nearbyAlertsEnabled.value
                    }
                )
            } else {
                ActionButton(
                    backgroundColor =
                    Color(0xFF42A5F5),
                    iconRes = R.drawable.notifications_off_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                    contentDescription = "Alert",
                    click = {
                        nearbyAlertsEnabled.value = !nearbyAlertsEnabled.value
                    }
                )
            }
        }

        InfoBox(
            message1 = "We'll Let you know when\nCrowd levels change",
            message2 =
            "We'll alert you instantly when it starts to escalate"
        )

        if (locationName.value == "Unknown") {
            val accuracy = Priority.PRIORITY_HIGH_ACCURACY
            val client = LocationServices.getFusedLocationProviderClient(LocalContext.current)
            val location = remember { mutableStateOf<Pair<Double, Double>?>(null) }

            if (ActivityCompat.checkSelfPermission(
                    LocalContext.current,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    LocalContext.current,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                RequestLocationPermission(
                    onPermissionGranted = {},
                    onPermissionDenied = {},
                    onPermissionsRevoked = {}
                )
            }

            client.getCurrentLocation(accuracy, CancellationTokenSource().token)
                .addOnSuccessListener { loc ->
                    loc?.let {
                        location.value = Pair(it.latitude, it.longitude)
                        latlng.value = Pair(it.latitude, it.longitude)
                        updateUserLocation(userName.value, it.latitude, it.longitude)
                        Thread {
                            locationName.value = resolveCoordinates(it.latitude, it.longitude)
                        }.start()
                    }
                }
                .addOnFailureListener {
                    locationName.value = "- Unknown -"
                }
        }
        NearbyPeople(nav)

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

        Spacer(modifier = Modifier.height(8.dp))

        InfoBox(message1 = "Nearby Alerts")

        // LazyColumn {
        AlertContainer(
            icon = R.drawable.emergency_heat_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            title = "Wildfire Alert",
            description = "A wildfire has been detected in the Forest of Gir",
            distance = 100L,
            time = System.currentTimeMillis(),
        )

        AlertContainer(
            icon = R.drawable.sound_detection_dog_barking_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            title = "Elephant Alert",
            description = "A herd of elephants are moving towards the village",
            distance = 100L,
            time = System.currentTimeMillis(),
        )

        AlertContainer(
            icon = R.drawable.emergency_heat_24dp_e8eaed_fill0_wght400_grad0_opsz24,
            title = "Wildfire Alert",
            description = "A wildfire has been detected in the Forest of RondoZoa",
            distance = 100L,
            time = System.currentTimeMillis(),
        )
    }
}

val nearByPeople = mutableIntStateOf(0)
val nearByPeopleScanEnabled = mutableStateOf(false)

@Composable
fun NearbyPeople(nav: NavController) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val radiusFactor = 100f
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .background(Color(0xFFF3E5F5), shape = RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 6.dp)
                    .background(Color(0xFFF3E5F5), shape = RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "People Nearby: ${nearByPeople.intValue}",
                    color = Color(0xFF9575CD),
                    modifier = Modifier
                        .padding(vertical = 1.dp, horizontal = 18.dp)
                        .clip(shape = RoundedCornerShape(14.dp)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            Text(
                text = "Radius: ${radiusFactor * sliderPosition} km",
                color = Color(0xFF9575CD),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 6.dp),
            ) {
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFDD835),
                        activeTrackColor = Color(0xFFF4511E),
                        inactiveTrackColor = Color(0xFFF8BBD0)
                    ),
                    valueRange = 0f..0.25f,
                    modifier = Modifier.width(150.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .padding(bottom = 2.dp)
            ) {
                Button(
                    onClick = {
                        if (!nearByPeopleScanEnabled.value) {
                            nearByPeopleScanEnabled.value = true
                            getNearbyUsers(
                                userName.value,
                                (radiusFactor * sliderPosition).toInt(),
                                nearByPeople,
                                nearByPeopleScanEnabled
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (nearByPeopleScanEnabled.value) Color(0xFFE1BEE7) else Color(
                            0xFF9575CD
                        ),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(6.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (nearByPeopleScanEnabled.value) "Stop Scanning" else "Start Scanning",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(6.dp)
                .background(Color(0xFFF3E5F5), shape = RoundedCornerShape(10.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        nav?.navigate("Emergency")
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .padding(top = 6.dp)
                    .background(Color(0xFFF3E5F5), shape = RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ambulance Traffick",
                    color = Color(0xFF9575CD),
                    modifier = Modifier
                        .padding(vertical = 1.dp, horizontal = 16.dp)
                        .clip(shape = RoundedCornerShape(14.dp)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            Text(
                text = "Traffic Avoidance System",
                color = Color(0xFF9575CD),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ambulance_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Ambulance",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(6.dp)
                        .padding(bottom = 12.dp),
                    colorFilter = ColorFilter.tint(Color(0xFFEF5350))
                )
            }
        }
    }
}

@Composable
fun InfoBox(message1: String, message2: String = "") {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .background(
                    Color(0x9EDDD5DF),
                    shape = RoundedCornerShape(6.dp)
                )
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notifications_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Notification",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(top = 5.dp),
                    colorFilter = ColorFilter.tint(Color(0xFFADA2AF))
                )
                Text(
                    text = message1,
                    color = Color(0xFFADA2AF),
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            if (message2.isNotEmpty()) {
                VerticalDivider(
                    color = Color(0xFFADA2AF),
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .height(24.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.notifications_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Notification",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(top = 5.dp),
                        colorFilter = ColorFilter.tint(Color(0xFFADA2AF))
                    )
                    Text(
                        text = message2,
                        color = Color(0xFFADA2AF),
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AutoUpdatingClock() {
    val currentTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(true) {
        while (true) {
            currentTime.longValue = System.currentTimeMillis()
            delay(1000)
        }
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        Text(
            text = DateUtils.formatDateTime(
                LocalContext.current,
                currentTime.longValue,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_WEEKDAY
            ).toString(),
            color = Color(0xFFADA2AF),
            modifier = Modifier.padding(vertical = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

@Composable
fun AlertContainer(
    icon: Int,
    title: String,
    description: String,
    distance: Long,
    time: Long,
) {
    val currentDt = System.currentTimeMillis()
    val timeElapsed =
        DateUtils.getRelativeTimeSpanString(time, currentDt, DateUtils.MINUTE_IN_MILLIS)
    val timeFmt =
        DateUtils.formatDateTime(LocalContext.current, time, DateUtils.FORMAT_SHOW_TIME).toString()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .padding(horizontal = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Emergency",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(Color(0xFFF4511E))
                )
                Text(
                    text = title,
                    color = Color(0xFFE91E63),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(start = 2.dp, top = 4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = timeElapsed.toString(),
                    color = Color(0xFF8A8888),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 2.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = Color(0xFF673AB7),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 6.dp, top = 0.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        timeFmt,
                        color = Color(0xFF9575CD),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 6.dp, top = 0.dp)
                    )
                }
                Text(
                    "$distance m",
                    color = Color(0xFF9575CD),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 6.dp, top = 10.dp)
                )
            }
        }
    }
}

@Composable
fun WelcomeMessage(loggedInUser: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                Color(0xFFF3E5F5),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$AppName Dashboard",
                color = Color(0xFFF4511E),
                modifier = Modifier.padding(vertical = 19.dp, horizontal = 5.dp),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
            Image(
                painter = painterResource(id = R.drawable.crowdsource_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = "$AppName Logo",
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 8.dp),
                colorFilter = ColorFilter.tint(Color(0xFFF4511E))
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFFD1C4E9),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 40.dp)
                    .padding(end = 6.dp)
            ) {
                Text(
                    text = "Welcome $loggedInUser >",
                    color = Color(0xFF9575CD),
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    backgroundColor: Color,
    iconRes: Int,
    contentDescription: String,
    click: () -> Unit = {}
) {
    Card(
        colors = CardColors(
            containerColor = backgroundColor,
            contentColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .padding(10.dp)
            .width(140.dp)
            .height(120.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(25.dp)
            )
            .clickable { click() },
        shape = RoundedCornerShape(40.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
fun LocationDisplay(location: String, latitude: Double = 0.0, longitude: Double = 0.0) {
    if (location != "Unknown") {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 14.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .border(2.dp, Color(0xFFEDE7F6), shape = RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardColors(
                    containerColor = Color(0xFFE1BEE7),
                    contentColor = Color(0xFF6A1B9A),
                    disabledContentColor = Color(0xFF6A1B9A),
                    disabledContainerColor = Color(0xFFE1BEE7),
                )
            ) {
                Row(
                    modifier = Modifier
                        .background(Color(0xFF05445e))
                        .padding(2.dp)
                        .height(200.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val markerState = remember {
                        mutableStateOf<MarkerState?>(
                            MarkerState(
                                position = LatLng(
                                    latitude,
                                    longitude
                                )
                            )
                        )
                    }
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 10f)
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            isTrafficEnabled = true,
                            isBuildingEnabled = true,
                            isIndoorEnabled = true,
                            mapType = MapType.NORMAL,
                        ),
                        uiSettings = MapUiSettings(
                            compassEnabled = true,
                            scrollGesturesEnabled = true,
                            scrollGesturesEnabledDuringRotateOrZoom = true,
                            rotationGesturesEnabled = true,
                            mapToolbarEnabled = false,
                        ),
                        mapColorScheme = ComposeMapColorScheme.LIGHT,
                        mergeDescendants = true
                    ) {
                        Marker(
                            state = markerState.value!!,
                            title = "Your Location",
                            snippet = location
                        )
                    }
                }
            }
        }
    }

    TowerInfoScreen()
}

val SOSSoundActive = mutableStateOf(false)

@Composable
fun PlaySOS() {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(SOSSoundActive.value) {
        if (SOSSoundActive.value) {
            scope.launch(Dispatchers.IO) {
                try {
                    val fileDescriptor = context.resources.openRawResourceFd(R.raw.alert_sound)
                    mediaPlayer.setDataSource(
                        fileDescriptor.fileDescriptor,
                        fileDescriptor.startOffset,
                        fileDescriptor.length
                    )
                    fileDescriptor.close()
                    mediaPlayer.prepare()
                    mediaPlayer.isLooping = true
                    mediaPlayer.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            scope.launch(Dispatchers.IO) {
                try {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}
