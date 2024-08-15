package com.example.crowdin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
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
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = Color.Black,
                    scrolledContainerColor = Color.Transparent,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color(0xFFFFA3C2)
                ),
                modifier = Modifier.alpha(0.8f)
                    .clip(shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 10.dp, bottomEnd = 10.dp)),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Menu"
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            color = Color(0xFFE6B4C5),
                            text = "$AppName - Maps"
                        )
                    }
                }
            )
        }
    ) {
        MapViewMain()
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


@SuppressLint("UnrememberedMutableState")
@Composable
fun MapViewMain() {
    val northamerica = LatLng(54.5260, -105.2551)
    val singapore = LatLng(1.3521, 103.8198)
    val kerala = LatLng(10.8505, 76.2711)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(kerala, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isTrafficEnabled = true,
            isBuildingEnabled = true,
            isIndoorEnabled = true,
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
                                "visibility": "off"
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
        ),
        mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM,
        mergeDescendants = true
    ) {
        Marker(
            state = MarkerState(position = kerala),
            title = "Kerala",
            snippet = "God's own country"
        )

        Marker(
            state = MarkerState(position = points[0]),
            title = "Kerala",
            snippet = "God's own countryMMMM"
        )

        Polyline(points = points,
            color = Color.Red,
            width = 20f,
            jointType = JointType.BEVEL
        )
    }
}