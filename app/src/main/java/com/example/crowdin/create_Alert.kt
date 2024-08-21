package com.example.crowdin

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun CreateAlertPage(nav: NavController) {
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
                CreateAlert(it)
            }
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlert(padding: PaddingValues) {
    val selectedLocation = remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var SelectedItem by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
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
                .height(5.dp)
                .background(Color.White)
                .fillMaxWidth()
        )
        val Now_location = LatLng(9.531650, 76.820450)

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(Now_location, 10f)
        }

        val m = mutableStateOf("")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
        ) {
            GoogleMap(
                onMapClick = { latLng ->
                    selectedLocation.value = latLng
                    Thread {
                        m.value = resolveCoordinates(
                            selectedLocation.value.latitude,
                            selectedLocation.value.longitude
                        )
                    }.start()
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 10f)
                },
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isTrafficEnabled = true,
                    isBuildingEnabled = true,
                    isIndoorEnabled = true,
                    isMyLocationEnabled = true,
                    mapType = MapType.NORMAL,
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


                if (selectedLocation.value.latitude != 0.0) {
                    Marker(
                        state = MarkerState(position = selectedLocation.value),
                        title = m.value.split(",", limit = 2)[0],
                        snippet = m.value,


                        )
                }


            }


        }

        Spacer(
            modifier = Modifier
                .height(5.dp)
                .background(Color.White)
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

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
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black
                        ),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Category", fontWeight = FontWeight.Bold) },
                        trailingIcon = {
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
            val alertTitle = remember { mutableStateOf("") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {//alert title

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = alertTitle.value,
                    onValueChange = { alertTitle.value = it },
                    label = { Text("Alert Title", fontWeight = FontWeight.Bold) },
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
            val alertDesc = remember { mutableStateOf("") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically


            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(),
                    value = alertDesc.value,
                    onValueChange = { alertDesc.value = it },
                    label = { Text("Alert Description", fontWeight = FontWeight.Bold) },
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
            var priority by remember { mutableStateOf("") }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 5.dp),
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

                val options = listOf("High", "Medium", "Low")
                Text(
                    text = "Select Priority :", style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    )
                )
                options.forEach { options ->
                    RadioButton(selected = priority == options, onClick = { priority = options })
                    Text(text = options)


                }
            }


            var radius by remember { mutableStateOf(5f) }
            val minRadius = 0f
            val maxRadius = 50f
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
//               verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = " Radius : ${radius.toInt()} Km", style = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
//                            fontFamily = FontFamily.Monospace,
                        )
                    )
                    Slider(
                        modifier = Modifier.width(155.dp),
                        value = radius,
                        onValueChange = { newValue -> radius = (newValue / 1).toInt() * 1f },
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
                        label = { Text("Radius(Km)") },
                        modifier = Modifier
                            .width(150.dp)
                            .height(55.dp),
//
                    )
                }
            }
            var selectImage: Uri? by remember { mutableStateOf(null) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(16.dp, 5.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.CenterVertically
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            )

            {

                val imagepicker =
                    rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                        selectImage = uri
                        //   selectImage = it
                    }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    if (selectImage != null) {
                        val painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = selectImage)
                                .build()
                        )
                        Image(
                            painter = painter,
                            contentDescription = "Image",
                            Modifier
                                .width(200.dp)
                                .height(400.dp)
                                .padding(0.dp)
                                // .border(2.dp, Color.Black)
                                .clip(RoundedCornerShape(10.dp)),
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
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Upload Image")
                }
            }





            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // Handle the submit button press
                        println("Submit button pressed")
                        println("monna alert title: ${alertTitle.value} , alert desc: ${alertDesc.value} , priority: $priority , radius: $radius")
                        sseClient.AlertViewModel.sendAlert(
                            title = alertTitle.value,
                            type = SelectedItem,
                            message = alertDesc.value,
                            severity = priority,
                            radius = radius.toInt(),
                            lat = selectedLocation.value.latitude,
                            lon = selectedLocation.value.longitude,
                            user = userName.value,

//                        media = "selectImage"
                        )
                    },
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
}


