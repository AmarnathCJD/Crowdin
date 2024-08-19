package com.example.crowdin

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

data class RoadClosedData(
    val lat: Double,
    val lon: Double,
)

data class RoadBlockedData(
    val line: List<LatLng>,
    val streetName: String,
    val speed: Int,
)

data class RoadData(
    val roadClosed: MutableList<RoadClosedData>,
    val roadBlocked: MutableList<RoadBlockedData>,
)

val roadData = RoadData(
    roadClosed = mutableListOf(),
    roadBlocked = mutableListOf(),
)

@Composable
fun MainLayout(nav: NavController) {
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
                        .background(Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color(0xFF05445e),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp)
                        .background(Color.Transparent)
                ) {
                    Text(
                        text = "Crowdin",
                        color = Color(0xFF05445e),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
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
                            color = ColorPalette.redish,
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
                MapViewMain(it, nav)
            }
        )
    }
}

val currentLocation = mutableStateOf(LatLng(10.953551, 75.946148))

@SuppressLint("UnrememberedMutableState")
@Composable
fun MapViewMain(paddingValues: PaddingValues, nav: NavController) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation.value, 11f)
    }

    if (currentLocation.value.latitude == 10.953551 && currentLocation.value.longitude == 75.946148) {
        val accuracy = Priority.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(LocalContext.current)
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
            return
        }
        client.getCurrentLocation(accuracy, CancellationTokenSource().token)
            .addOnSuccessListener { loc ->
                loc?.let {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        LatLng(it.latitude, it.longitude),
                        11f
                    )
                    currentLocation.value = LatLng(it.latitude, it.longitude)
                    sseClient.AlertViewModel.getRoadData(it.latitude, it.longitude)
                }
            }
            .addOnFailureListener {
                currentLocation.value = LatLng(10.953551, 75.946148)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding() - 77.dp,
                bottom = paddingValues.calculateBottomPadding(),
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            InfoPopupBoxWithZIndex(nav)
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMyLocationClick = {
                    currentLocation.value = LatLng(it.latitude, it.longitude)
                },
                properties = MapProperties(
                    isTrafficEnabled = true,
                    isBuildingEnabled = true,
                    isIndoorEnabled = true,
                    isMyLocationEnabled = isPermissionGranted(
                        LocalContext.current, Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    mapType = MapType.NORMAL,
                ),
                uiSettings = MapUiSettings(
                    compassEnabled = true,
                    scrollGesturesEnabled = true,
                    scrollGesturesEnabledDuringRotateOrZoom = true,
                    rotationGesturesEnabled = true,
                    mapToolbarEnabled = false,
                    zoomControlsEnabled = true,
                    zoomGesturesEnabled = true,
                ),
                mapColorScheme = ComposeMapColorScheme.LIGHT,
                mergeDescendants = true
            ) {
                for (road in roadData.roadBlocked) {
                    Polyline(
                        points = road.line,
                        color = Color.Red,
                        width = 20f,
                        jointType = JointType.BEVEL
                    )

                    Marker(
                        state = MarkerState(position = road.line[0]),
                        title = road.streetName,
                        snippet = "Speed: ${road.speed} km/h",
//                        icon = getCustomBitmapDescriptor(
//                            LocalContext.current,
//                            R.drawable.arrow_cool_down_24dp_e8eaed_fill0_wght400_grad0_opsz24,
//                            Color.Red,
//                            170,
//                            170,
//                        ),
//                        anchor = Offset(0.5f, 1f),
                    )
                }
//                for (road in roadData.roadClosed) {
//                    Marker(
//                        state = MarkerState(position = LatLng(road.lat, road.lon)),
//                        title = "Road Closed",
//                        snippet = "Road Closed",
//                        icon = getCustomBitmapDescriptor(
//                            LocalContext.current,
//                            R.drawable.remove_road_24dp_e8eaed_fill0_wght400_grad0_opsz24,
//                            Color.Transparent,
//                            170,
//                            170,
//                        ),
//                        anchor = Offset(0.5f, 1f),
//                    )
//                }

                for (alert in sseClient.AlertViewModel.getAllAlerts()) {
                    Circle(
                        center = LatLng(alert.lat, alert.lon),
                        radius = alert.radius.toDouble() * 1000,
                        fillColor = Color(0x220000FF),
                        tag = alert.id.toString(),
                        strokeColor = Color(0x220000FF),
                        strokeWidth = 5f,
                        onClick = {
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                                LatLng(alert.lat, alert.lon),
                                11f
                            )
                        }
                    )

                    if (cameraPositionState.position.zoom > 9) {
                        Marker(
                            state = MarkerState(position = LatLng(alert.lat, alert.lon)),
                            title = alert.title,
                            snippet = alert.message,
                            icon = getCustomBitmapDescriptor(
                                LocalContext.current,
                                getAlertIcon(alert.icon),
                                Color.Transparent,
                                170,
                                170,
                            ), // TODO: fix offset issue.
                            anchor = Offset(0.5f, 1f),
                            onClick = {
                                locationNameForPopup.value = ""
                                PopupDataObj.value = PopupData(
                                    title = alert.title,
                                    description = alert.message,
                                    location = LatLng(alert.lat, alert.lon),
                                    radius = alert.radius.toDouble(),
                                    type = alert.severity,
                                    bgColor = Color(0xFFE3F2FD),
                                    id = alert.id
                                )
                                popupState.value = true
                                Thread {
                                    locationNameForPopup.value = resolveCoordinates(
                                        alert.lat, alert.lon
                                    )
                                }.start()
                                return@Marker true
                            }
                        )
                    }
                }
            }
        }
    }
}

class PopupData(
    val title: String,
    val description: String,
    val location: LatLng,
    val radius: Double,
    val type: String,
    val bgColor: Color = Color.White,
    val id: Int
)

var PopupDataObj = mutableStateOf(
    PopupData(
        title = "-",
        description = "Alert Data isEmpty",
        location = LatLng(10.953551, 75.946148),
        radius = 100.0,
        type = "~",
        bgColor = Color(0xFFE3F2FD),
        id = 0
    )
)

val locationNameForPopup = mutableStateOf("")

val popupState = mutableStateOf(false)

@Composable
fun InfoPopupBoxWithZIndex(nav: NavController) {
    val distance = remember { mutableDoubleStateOf(0.0) }
    if (popupState.value) {
        distance.doubleValue = calculateDistance(
            currentLocation.value.latitude,
            currentLocation.value.longitude,
            PopupDataObj.value.location.latitude,
            PopupDataObj.value.location.longitude
        )

        Box(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize()
                .padding(bottom = 5.dp)
                .padding(horizontal = 5.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .background(PopupDataObj.value.bgColor)
                    .clip(RoundedCornerShape(20.dp))
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = PopupDataObj.value.title,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    if (distance.doubleValue < PopupDataObj.value.radius) Color(
                                        0xFFE57373
                                    ) else Color(
                                        0xFF9CCC65
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .clickable {
                                    println(
                                        "Update status clicked: ${
                                            currentLocation.value.latitude
                                        }, ${
                                            currentLocation.value.longitude
                                        }"
                                    )
                                }
                        ) {
                            Text(
                                text = "Update Status",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                            )
                        }
                    }

                    Text(
                        text = "Close",
                        color = Color(0xFF05445e),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable { popupState.value = false }
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = PopupDataObj.value.description,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Column {
                        Row {
                            Text(
                                text = "${PopupDataObj.value.location}",
                                color = Color(0xFF05445e),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Row {
                            Text(
                                text = "${PopupDataObj.value.radius} km",
                                color = Color(0xFF05445e),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = " | ",
                                color = Color(0xFF05445e),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${distance.doubleValue} km away~",
                                color = Color(0xFF05445e),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        if (locationNameForPopup.value != "") {
                            Spacer(modifier = Modifier.height(5.dp))
                            Row {
                                Text(
                                    text = locationNameForPopup.value,
                                    color = Color(0xFF05445e),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "transition1")
                    val color by infiniteTransition.animateColor(
                        initialValue = Color(0xFF05445e),
                        targetValue = Color(0xFF75c9c8),
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 1000),
                            repeatMode = RepeatMode.Reverse
                        ), label = "anim1"
                    )
                    Button(
                        onClick = {
                            // get directions
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (distance.doubleValue < PopupDataObj.value.radius) color else Color(
                                0xFF05445e
                            ),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 10.dp
                        )
                    ) {
                        Text(
                            text = "Get To Safety",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Button(
                        onClick = {
                            // call emergency
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF05445e),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 10.dp
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Call Emergency",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Button(
                        onClick = {
                            convoId = PopupDataObj.value.id
                            chatTitle = PopupDataObj.value.title
                            nav.navigate("ChatView")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF05445e),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(
                            defaultElevation = 10.dp
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Support Chat",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .zIndex(2f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                //.background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Search",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}