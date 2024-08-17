package com.example.crowdin

import android.annotation.SuppressLint
import android.net.Uri
import android.text.format.DateUtils
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.Image
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.layout.ContentScale
//import coils library
import coil.compose.rememberAsyncImagePainter   //add this line
import coil.request.ImageRequest
import kotlinx.coroutines.GlobalScope
//import com.hardcoreandroid.photopickerdemo.ui.theme.PhotoPickerDemoTheme


import kotlinx.coroutines.delay
import java.util.logging.Logger.global

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CreateAlert() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier

            .fillMaxSize()

            .background(Color.White)
            .verticalScroll(scrollState),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()

                .padding(16.dp),

            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom

        ) {


            Text(
                text = "Create Alert", style = TextStyle(
                    color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            )


        }
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .background(Color.Black)
                .fillMaxWidth()
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))

        ) {
            val Now_location = LatLng(9.531650, 76.820450)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(Now_location, 10f)
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
                    zoomControlsEnabled = false, // make it false one done with development
                ),
                mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM,
                mergeDescendants = true
            ) {
                Marker(
                    state = MarkerState(position = Now_location),
                    title = "Kerala",
                    snippet = "God's own country"
                )


            }


        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
                .background(Color.Black)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var SelectedItem by remember { mutableStateOf("") }
            var expanded by remember { mutableStateOf(false) }
            val options = listOf(
                "Animal Alert", "Disaster Alert", "Accident Alert", "Fire Alert", "Other Alert"
            )
            ExposedDropdownMenuBox(modifier = Modifier.background(Color.White),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(

                    value = SelectedItem,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black,
                        disabledPlaceholderColor = Color.Gray,
                        errorIndicatorColor = Color.Red,
                        focusedPlaceholderColor = Color.Black,
//                        placeholderColor= Color.Gray,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,

                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black

                    ),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Category") },
                    trailingIcon = {
//                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Localized description")
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(modifier = Modifier.background(Color.White),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    options.forEach { option ->
                        DropdownMenuItem(modifier = Modifier.background(Color.White),
                            text = { Text(text = option) },
                            onClick = {
                                SelectedItem = option
                                expanded = false
                            })
                    }

                }

            }


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {//alert title
            val alertTitle = remember { mutableStateOf("") }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = alertTitle.value,
                onValueChange = { alertTitle.value = it },
                label = { Text("Alert Title") },
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
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically


        ) {
            val alertDesc = remember { mutableStateOf("") }
            OutlinedTextField(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                value = alertDesc.value,
                onValueChange = { alertDesc.value = it },
                label = { Text("Alert Description") },
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
                .padding(16.dp),
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            var SelectedItem by remember { mutableStateOf("") }
//            var expanded by remember { mutableStateOf(false) }
//            val options = listOf(
//                "High",
//                "Medium",
//                "Low"
//            )
//            ExposedDropdownMenuBox(
//                modifier = Modifier.background(Color.White),
//                expanded = expanded,
//                onExpandedChange = { expanded = !expanded }
//            ) {
//                OutlinedTextField(
//
//                    value = SelectedItem,
//                    colors = TextFieldDefaults.colors(
//                        focusedTextColor = Color.Black,
//                        unfocusedContainerColor = Color.White,
//                        focusedContainerColor = Color.White,
//                        unfocusedTextColor = Color.Black,
//                        focusedIndicatorColor = Color.Black,
//                        unfocusedIndicatorColor = Color.Black,
//                        disabledPlaceholderColor = Color.Gray,
//                        errorIndicatorColor = Color.Red,
//                        focusedPlaceholderColor = Color.Black,
//                    ),
//                    onValueChange = {},
//                    readOnly = true,
//                    label = { Text("Select Priority") },
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//                    }, modifier = Modifier
//                        .menuAnchor()
//                        .fillMaxWidth()
//                )
//                ExposedDropdownMenu(
//                    modifier = Modifier.background(Color.White),
//                    expanded = expanded, onDismissRequest = { expanded = false }) {
//                    options.forEach { option ->
//                        DropdownMenuItem(
//                            modifier = Modifier.background(Color.White),
//                            text = { Text(text = option) }, onClick = {
//                                SelectedItem = option
//                                expanded = false
//                            })
//                    }
//
//                }
//            }
            var selected by remember { mutableStateOf("") }
            val options = listOf("High", "Medium", "Low")
            Text(
                text = "Select Priority :", style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )
            )
            options.forEach() { options ->
                RadioButton(selected = selected == options, onClick = { selected = options })
                Text(text = options)
            }


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
//               verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
        ) {
            var radius by remember { mutableStateOf(50f) }
            val minRadius = 0f
            val maxRadius = 100f
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = " Radius : ${radius.toInt()} m", style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    )
                )
                Slider(
                    modifier = Modifier.width(180.dp),
                    value = radius,
                    onValueChange = { newValue -> radius = (newValue / 10).toInt() * 10f },
                    valueRange = minRadius..maxRadius
                )

                OutlinedTextField(
                    value = radius.toInt().toString(),
                    onValueChange = {
                        val input = it.toFloatOrNull()
                        if (input != null && input in minRadius..maxRadius) {
                            radius = input
                        }
                    },
                    label = { Text("Radius(m)") },
                    modifier = Modifier
                        .width(150.dp)
                        .height(55.dp),
//
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()

                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        )

        {
            var selectImage: Uri? by remember { mutableStateOf(null) }
            val imagepicker =
                rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    selectImage = uri
                    //   selectImage = it
                }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {


                if (selectImage != null) {
                    val painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = selectImage).build()
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Image",
                        Modifier
                            .width(200.dp)
                            .height(400.dp)
                            .padding(0.dp)
                            .border(2.dp, Color.Black),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )
                }
            }


            Button(
                modifier = Modifier

                    .height(50.dp),

                onClick = {
                    imagepicker.launch(
                        PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )


                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xB5F44336),
                )
            ) {
                Text(text = "Upload Image")

            }
        }





        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE91E63),
//                    backgroundColor = Color.Black,
//                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Create Alert")
            }
        }

    }
}


