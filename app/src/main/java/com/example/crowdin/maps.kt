package com.example.crowdin

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout() {
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
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow",
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
                            imageRes = R.drawable.pets_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.secondary,
                            name = "Animals"
                        )
                        BottomIconItem(
                            imageRes = R.drawable.notifications_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.secondary,
                            name = "Alerts"
                        )
                        BottomIconItem(
                            imageRes = R.drawable.roofing_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.secondary,
                            name = "Home"
                        )
                        BottomIconItem(
                            imageRes = R.drawable.my_location_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.redish,
                            name = "Location"
                        )
                        BottomIconItem(
                            imageRes = R.drawable.admin_panel_settings_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            color = ColorPalette.secondary,
                            name = "Account"
                        )
                    }
                }
            },
            containerColor = Color.Transparent,
            content = {
                MapViewMain(it)
            }
        )
    }
}

val points = ArrayList<LatLng>(
    listOf(
        LatLng(10.953551, 75.946148),
        LatLng(10.953622, 75.945468),
        LatLng(10.953619, 75.944995),
        LatLng(10.953596, 75.944636),
        LatLng(10.953444, 75.94428),
        LatLng(10.953208, 75.944022),
        LatLng(10.953081, 75.94392),
        LatLng(10.952738, 75.943747),
        LatLng(10.951761, 75.943305),
        LatLng(10.951454, 75.943186),
        LatLng(10.951121, 75.943008),
        LatLng(10.950609, 75.942696),
        LatLng(10.949821, 75.942061),
        LatLng(10.949414, 75.941824),
        LatLng(10.949259, 75.941761),
        LatLng(10.949101, 75.941697),
        LatLng(10.948488, 75.941659),
        LatLng(10.946162, 75.941515),
        LatLng(10.945985, 75.941493),
        LatLng(10.94585, 75.941444),
        LatLng(10.945781, 75.941408),
        LatLng(10.945584, 75.941271),
        LatLng(10.945402, 75.941093),
        LatLng(10.944577, 75.940135),
        LatLng(10.943799, 75.939153),
        LatLng(10.943211, 75.938587),
        LatLng(10.942999, 75.938399),
        LatLng(10.942917, 75.938333),
        LatLng(10.942468, 75.938034),
        LatLng(10.942239, 75.937923),
        LatLng(10.941885, 75.937795),
        LatLng(10.941658, 75.937729),
        LatLng(10.941385, 75.937673)
    )
)

data class Alert(
    val id: Int,
    val title: String,
    val description: String,
    val location: LatLng,
    val radius: Double,
    val type: String,
)

// dummy data, close by 5 alerts
val alerts = listOf(
    Alert(
        id = 1,
        title = "Flood Alert",
        description = "Heavy rain expected in the area",
        location = LatLng(10.953551, 75.946148),
        radius = 1000.0,
        type = "Flood",
    ),
    Alert(
        id = 2,
        title = "Earthquake Alert",
        description = "Earthquake expected in the area",
        location = LatLng(10.953622, 75.945468),
        radius = 1000.0,
        type = "Earthquake",
    ),
    Alert(
        id = 3,
        title = "Tsunami Alert",
        description = "Tsunami expected in the area",
        location = LatLng(10.953619, 75.944995),
        radius = 1000.0,
        type = "Tsunami",
    ),
    Alert(
        id = 4,
        title = "Cyclone Alert",
        description = "Cyclone expected in the area",
        location = LatLng(10.953596, 75.944636),
        radius = 1000.0,
        type = "Cyclone",
    ),
    Alert(
        id = 5,
        title = "Tornado Alert",
        description = "Tornado expected in the area",
        location = LatLng(10.953444, 75.94428),
        radius = 1000.0,
        type = "Tornado",
    ),
)

val currentLocation = mutableStateOf(LatLng(10.953551, 75.946148))

@SuppressLint("UnrememberedMutableState")
@Composable
fun MapViewMain(paddingValues: androidx.compose.foundation.layout.PaddingValues) {
    RequestLocationPermission(
        onPermissionGranted = {},
        onPermissionDenied = {},
        onPermissionsRevoked = {}
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding() - 77.dp,
                bottom = paddingValues.calculateBottomPadding(),
            )
    ) {
        val northamerica = LatLng(54.5260, -105.2551)
        val singapore = LatLng(1.3521, 103.8198)
        val kerala = LatLng(10.8505, 76.2711)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(kerala, 10f)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            InfoPopupBoxWithZIndex()
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMyLocationClick = {
                    currentLocation.value = LatLng(it.latitude, it.longitude)
                    println("My location clicked: $it")
                },
                properties = MapProperties(
                    isTrafficEnabled = true,
                    isBuildingEnabled = true,
                    isIndoorEnabled = true,
                    isMyLocationEnabled = isPermissionGranted(
                        LocalContext.current, Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    mapType = MapType.NORMAL,
                    mapStyleOptions = MapStyleOptions(
                        """
                [
                    {
                        "featureType": "poi",
                        "elementType": "labels",
                        "stylers": [
                            {
                                "visibility": "off"
                            }
                        ]
                    },
                    {
                        "featureType": "transit",
                        "elementType": "labels",
                        "stylers": [
                            {
                                "visibility": "on"
                            }
                        ]
                    },
                    {
                        "featureType": "road",
                        "elementType": "labels",
                        "stylers": [
                            {
                                "visibility": "on"
                            }
                        ]
                    }
                ]
                """.trimIndent()
                    )
                ),
                uiSettings = MapUiSettings(
                    compassEnabled = true,
                    scrollGesturesEnabled = true,
                    scrollGesturesEnabledDuringRotateOrZoom = true,
                    rotationGesturesEnabled = true,
                    mapToolbarEnabled = false,
                    zoomControlsEnabled = true, // make it false one done with development
                    zoomGesturesEnabled = true,
                ),
                mapColorScheme = ComposeMapColorScheme.LIGHT,
                mergeDescendants = true
            ) {

                Marker(
                    state = MarkerState(position = northamerica),
                    title = "North America",
                    snippet = "The continent"
                )
                Marker(
                    state = MarkerState(position = kerala),
                    title = "Kerala",
                    snippet = "God's own country"
                )

                Marker(
                    state = MarkerState(position = points[0]),
                    title = "Kerala",
                    snippet = "God's own countryMMMM",
                )

                Polyline(
                    points = points,
                    color = Color.Red,
                    width = 20f,
                    jointType = JointType.BEVEL
                )

                Circle(
                    center = kerala, radius = 000000.0, fillColor = Color(0x220000FF),
                    tag = "circle", strokeColor = Color(0x220000FF), strokeWidth = 5f
                )

                for (alert in sseClient.AlertViewModel.getAllAlerts()) {
                    Circle(
                        center = LatLng(alert.lat, alert.lon),
                        radius = alert.radius.toDouble() * 1000,
                        fillColor = Color(0x220000FF),
                        tag = alert.id.toString(),
                        strokeColor = Color(0x220000FF),
                        strokeWidth = 5f
                    )

                    Marker(
                        state = MarkerState(position = LatLng(alert.lat - 0.035, alert.lon)),
                        title = alert.title,
                        snippet = alert.message,
                        icon = getCustomBitmapDescriptor(
                            LocalContext.current,
                            R.drawable.emergency_heat_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                            Color.Red,
                            170,
                            170,
                        ),

                        )
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
)

var dummyPopupData = PopupData(
    title = "Flood Alert",
    description = "Heavy rain expected in the area, please take necessary precautions",
    location = LatLng(10.953551, 75.946148),
    radius = 1000.0,
    type = "Flood",
    bgColor = Color(0xFFE3F2FD)
)

val popupState = mutableStateOf(true)

@Composable
fun InfoPopupBoxWithZIndex() {
    if (popupState.value) {
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
                    .background(dummyPopupData.bgColor)
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
                            text = dummyPopupData.title,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFB71C1C),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .clickable {
                                    println("Update status clicked: ${
                                        currentLocation.value.latitude
                                    }, ${
                                        currentLocation.value.longitude
                                    }")
                                    // update status
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
                    text = dummyPopupData.description,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(5.dp))
                Row {
                    Text(
                        text = "${dummyPopupData.location}",
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
                        text = "${dummyPopupData.radius} km",
                        color = Color(0xFF05445e),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                // add buttons to get directions, call emergency, etc
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            // get directions
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF05445e),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.elevatedButtonElevation()
                    ) {
                        Text(
                            text = "Get To Safety",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Button(
                        onClick = {
                            // call emergency
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF05445e),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Call Emergency",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Button(
                        onClick = {
                            // share alert
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF05445e),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.elevatedButtonElevation(),
                        shape = RoundedCornerShape(10.dp)
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