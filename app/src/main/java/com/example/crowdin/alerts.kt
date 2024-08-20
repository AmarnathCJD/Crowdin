package com.example.crowdin

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun AlertsPage(nav: NavController) {
    if (ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        RequestLocationPermission(
            onPermissionGranted = {},
            onPermissionDenied = {},
            onPermissionsRevoked = {}
        )
    }
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
                            color = ColorPalette.redish,
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
                            color = ColorPalette.secondary,
                            name = "Account",
                            nav = nav
                        )
                    }
                }
            },
            containerColor = Color.Transparent,
            content = {
                AlertsPageMain(padding = it)
            }
        )
    }
}

@Composable
fun AlertsPageMain(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding() - 30.dp,
                bottom = padding.calculateBottomPadding()
            )
    ) {
        AlertsTitle()
        AlertsList()
    }
}

@Composable
fun AlertsTitle() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 16.dp)
            .background(Color(0xFFFFCDD2), RoundedCornerShape(6.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Active Alerts (NearBy)",
            color = Color(0xFF3F51B5),
            fontSize = 18.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun AlertsList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            sseClient.EmergencyModel.emergenciesState.forEach {
                if (it.username != userName.value)
                    item {
                        AlertItem(it)
                    }
            }

            sseClient.AlertViewModel.alertState.alerts.forEach {
                if (it.user != userName.value)
                    item {
                        AlertItemBasic(it)
                    }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun AlertItem(emergency: Emergency) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            .background(Color(0x6AFCE4EC), RoundedCornerShape(12.dp))
            .border(2.dp, Color(0xFFE1BEE7), RoundedCornerShape(12.dp))
    ) {
        Text(
            text = "Emergency Vehicle Alert",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp)
        )
        Text(
            text = "Requesting clearance for emergency vehicle to pass through",
            color = Color(0xFF05445e),
            fontSize = 16.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Text(
            text = "(LAT ${emergency.lat}, LON ${emergency.lon})",
            color = Color(0xFFF4511E),
            fontSize = 14.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 6.dp)
        )
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(emergency.lat, emergency.lon), 15f)
        }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(220.dp)
                .border(2.dp, Color(0xFFE1BEE7), RoundedCornerShape(15.dp)),
            properties = MapProperties(
                isMyLocationEnabled = true,
                isTrafficEnabled = true,
            ),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = LatLng(emergency.lat, emergency.lon)),
            )
        }
        Text(
            text = "Please clear the way and make space for the emergency vehicle\n" +
                    "to pass through. Your cooperation is highly appreciated.",
            color = Color(0xFF666869),
            fontSize = 12.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            modifier = Modifier
                .padding(0.dp)
                .padding(horizontal = 24.dp)
                .padding(top = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    sseClient.RecepModel.sendRecep(
                        emergency.lat,
                        emergency.lon,
                        false,
                        emergency.emergencyId
                    )
                },
                modifier = Modifier
                    .padding(12.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ads_click_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Clear",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text("Clear")
            }
            Button(
                onClick = {
                    sseClient.RecepModel.sendRecep(
                        emergency.lat,
                        emergency.lon,
                        true,
                        emergency.emergencyId
                    )
                },
                modifier = Modifier
                    .padding(12.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.remove_road_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Crowded",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text("Crowded")
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun AlertItemBasic(emergency: AlertObject) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            .background(Color(0x6AFCE4EC), RoundedCornerShape(12.dp))
            .border(2.dp, Color(0xFFE1BEE7), RoundedCornerShape(12.dp))
    ) {
        Text(
            text = emergency.title,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp)
        )
        Text(
            text = emergency.message,
            color = Color(0xFF05445e),
            fontSize = 16.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        val locationName = remember { mutableStateOf("Location") }
        LaunchedEffect (Unit) {
            resolveCoordinatesAsync(emergency.lat, emergency.lon, locationName)
        }
        if (locationName.value != "Location") {
            Text(
                text = locationName.value,
                color = Color(0xFF666869),
                fontSize = 14.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 6.dp)
            )
        }
        Text(
            text = "(LAT ${emergency.lat}, LON ${emergency.lon})",
            color = Color(0xFFF4511E),
            fontSize = 12.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 6.dp)
        )
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(emergency.lat, emergency.lon), 15f)
        }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(220.dp)
                .border(2.dp, Color(0xFFE1BEE7), RoundedCornerShape(15.dp)),
            properties = MapProperties(
                isMyLocationEnabled = true,
                isTrafficEnabled = true,
            ),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = LatLng(emergency.lat, emergency.lon)),
            )
        }
        Text(
            text = "Radius of the Alert: ${emergency.radius} KM",
            color = Color(0xFF666869),
            fontSize = 14.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            modifier = Modifier
                .padding(0.dp)
                .padding(horizontal = 24.dp)
                .padding(top = 6.dp)
        )

        val isUpvotedAlready = remember { mutableStateOf(false) }
        val isDownvotedAlready = remember { mutableStateOf(false) }
        val upCount = remember { mutableIntStateOf(emergency.upVotes) }
        val downCount = remember { mutableIntStateOf(emergency.downVotes) }

        Text(
            text = "Probability > ${calcProbability(upCount.intValue, downCount.intValue)}",
            color = Color(0xFF666869),
            fontSize = 12.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            modifier = Modifier
                .padding(0.dp)
                .padding(horizontal = 24.dp)
                .padding(top = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (isDownvotedAlready.value) {
                        isDownvotedAlready.value = false
                        isUpvotedAlready.value = true
                        downCount.intValue -= 1
                        upCount.intValue += 1
                        sseClient.AlertViewModel.downvoteAlert(emergency.id, true)
                        sseClient.AlertViewModel.upvoteAlert(emergency.id)
                    } else if (!isUpvotedAlready.value) {
                        isUpvotedAlready.value = true
                        upCount.intValue += 1
                        sseClient.AlertViewModel.upvoteAlert(emergency.id)
                    } else {
                        isUpvotedAlready.value = false
                        sseClient.AlertViewModel.upvoteAlert(emergency.id, true)
                        upCount.intValue -= 1
                    }
                },
                modifier = Modifier
                    .padding(6.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.thumb_up_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Downvote",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(if (upCount.intValue > 0) upCount.intValue.toString() + " Upvotes" else "Upvote")
            }
            Button(
                onClick = {
                    if (isUpvotedAlready.value) {
                        isUpvotedAlready.value = false
                        isDownvotedAlready.value = true
                        upCount.intValue -= 1
                        downCount.intValue += 1
                        sseClient.AlertViewModel.upvoteAlert(emergency.id, true)
                        sseClient.AlertViewModel.downvoteAlert(emergency.id)
                    } else if (!isDownvotedAlready.value) {
                        isDownvotedAlready.value = true
                        sseClient.AlertViewModel.downvoteAlert(emergency.id)
                        downCount.intValue += 1
                    } else {
                        isDownvotedAlready.value = false
                        sseClient.AlertViewModel.downvoteAlert(emergency.id, true)
                        downCount.intValue -= 1
                    }
                },
                modifier = Modifier
                    .padding(6.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.thumb_down_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Downvote",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(if (downCount.intValue > 0) downCount.intValue.toString() + " Downvotes" else "Downvote")
            }
        }
    }
}