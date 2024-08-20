package com.example.crowdin

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AnimalPage(nav: NavController) {
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
                AnimalRepel(nav, it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalRepel(nav: NavController, padding: PaddingValues) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(
                top = padding.calculateTopPadding() - 30.dp,
                bottom = padding.calculateBottomPadding(),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFCE4EC))
                .clip(shape = RoundedCornerShape(10.dp))
                .padding(16.dp),

            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top
        ) {
            Text(
                modifier = Modifier.padding(16.dp, 20.dp, 16.dp, 0.dp),
                text = "Animal Repel Mode", style = TextStyle(
                    color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold,
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp),

            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "This mode will help you to repel animals", style = TextStyle(
                    color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Our app’s Animal Repellent Mode uses sound frequencies to keep unwanted animals away. The high-pitched sounds are usually inaudible to humans but are effective at scaring off animals . You can choose different sound settings depending on which animal you want to repel.",
                style = TextStyle(
                    color = Color.Black, fontSize = 15.sp
                )
            )
        }
        Row(
            modifier = Modifier
                .width(700.dp)

                //  .wrapContentWidth( Alignment.CenterHorizontally)
                .background(color = Color.Transparent)
                .clip(shape = RoundedCornerShape(60.dp))
                .padding(16.dp, 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = R.drawable.animalreppel),
                contentDescription = "Animal Repel Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(Color(0xFFB82858))
            )

        }

        val animals = listOf(
            "Dog  ",
            "Cat ",
            "Rat",
            "Snake",
            "Elephant",
            "Lion",
            "Tiger",
            "Bear"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selectedItem, onValueChange = {},
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
                    readOnly = true,
                    label = { Text("Select Animal", fontWeight = FontWeight.Bold) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)

                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()

                )
                ExposedDropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    animals.forEach { option ->
                        DropdownMenuItem(
                            modifier = Modifier.background(Color.White),
                            text = { Text(text = option) }, onClick = {
                                selectedItem = option
                                expanded = false
                            })
                    }

                }

            }


        }
        val context = LocalContext.current
        val mediaPlayer = remember { MediaPlayer() }
        val scope = rememberCoroutineScope()
        var playing by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp, 0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    println("blahhh$selectedItem")
                    if (selectedItem != "") {
                        playing = true
//                         try {
//                             val fileDescriptor = context.resources.openRawResourceFd(R.raw.humba)
//                             mediaPlayer.setDataSource(
//                                 fileDescriptor.fileDescriptor,
//                                 fileDescriptor.startOffset,
//                                 fileDescriptor.length
//                             )
//                             fileDescriptor.close()
//
//                             mediaPlayer.prepare()
//                             mediaPlayer.isLooping = false
//                             mediaPlayer.start()
//
//
//                         } catch (e: Exception) {
//                             e.printStackTrace()
//                         }

                    } else {
                        playing = false
                    }


                }
            ) {
                if (!playing) {
                    println("blahhh$playing")
                    Text(

                        text = "Start", style = TextStyle(
                            color = Color.White, fontSize = 18.sp
                        )
                    )
                    //playing = true
                } else {
                    println("blahhh$playing")
                    Text(
                        text = "Stop", style = TextStyle(
                            color = Color.White, fontSize = 18.sp
                        )
                    )

                }
            }

        }

    }

}