package com.example.crowdin

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

data class Recep(
    val lat: Double,
    val lon: Double,
    val crowded: Boolean,
    val emergencyId: String = ""
)

@Composable
fun EmergencyVehiclePage(nav: NavController) {
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
                AmbulanceEmerge(padding = it)
            }
        )
    }
}

val clearanceEnabled = mutableStateOf(false)
val currentLocationOfAmbulance = mutableStateOf(LatLng(0.0, 0.0))

@SuppressLint("UnrememberedMutableState")
@Composable
fun AmbulanceEmerge(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding() - 80.dp,
                bottom = padding.calculateBottomPadding() - 70.dp
            )
    ) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(currentLocation.value, 11f)
        }
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
        }

        LaunchedEffect(Unit) {
            while (true) {
                client.getCurrentLocation(accuracy, CancellationTokenSource().token)
                    .addOnSuccessListener { loc ->
                        loc?.let {
                            currentLocationOfAmbulance.value = LatLng(it.latitude, it.longitude)
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                                LatLng(it.latitude, it.longitude),
                                11f
                            )
                        }
                    }
                    .addOnFailureListener {}

                delay(2000)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
            ) {
                ElevatedButton(
                    onClick = { /*NONE*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 6.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "EMERGENCY SERVICES",
                            color = Color(0xFFEF5350),
                            fontSize = 18.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 20.dp)
                ) {
                    Button(
                        onClick = {
                            clearanceEnabled.value = !clearanceEnabled.value
                            sseClient.ChatViewModel.EmergencyClearanceReq(
                                currentLocationOfAmbulance.value.latitude,
                                currentLocationOfAmbulance.value.longitude,
                                clearanceEnabled.value
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (clearanceEnabled.value) Color(0xFFE57373) else Color(
                                0xFFB5B8BB
                            ),
                            contentColor = if (clearanceEnabled.value) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                    ) {
                        Text(
                            text = "ENABLE EMERGENCY VECHILE CLEARANCE",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp
                                )
                        )
                    }
                }
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 2.dp)
                        .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(15.dp))
                ){
                    Text(
                        text = "This Feature will assist in clearing the path for the emergency vehicles\n\n" +
                                "Please use this feature responsibly !!!",
                        color = Color(0xFF8A8C8F),
                        fontSize = 14.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 12.dp, top = 12.dp)
                            .padding(horizontal = 24.dp)

                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (clearanceEnabled.value) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(1.dp)
                            .padding(horizontal = 16.dp)
                            .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            text = "Started Real Time Tracking",
                            color = Color(0xFF5261C7),
                            fontSize = 14.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        Text(
                            text = "Latitude: ${currentLocationOfAmbulance.value.latitude}, Longitude: ${currentLocationOfAmbulance.value.longitude}",
                            color = Color(0xFFB71C1C),
                            fontSize = 14.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            modifier = Modifier
                                .padding(0.dp)
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 12.dp)
                        )
                    }

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .padding(top = 16.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        onMyLocationClick = {
                            currentLocationOfAmbulance.value = LatLng(it.latitude, it.longitude)
                        },
                        properties = MapProperties(
                            isMyLocationEnabled = true,
                            isTrafficEnabled = true,
                        ),
                        cameraPositionState = cameraPositionState
                    ) {
                        for (recep in sseClient.RecepModel.recepsState) {
                            Marker(
                                state = MarkerState(position = LatLng(recep.lat, recep.lon)),
                                title = if (recep.crowded) "Crowded" else "Not Crowded",
                                snippet = if (recep.crowded) "Avoid (At your own risk)" else "Safe",
                                icon = BitmapDescriptorFactory.defaultMarker(if (recep.crowded) BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_GREEN),
                            )
                        }
                    }
                }
            }
        }
    }
}