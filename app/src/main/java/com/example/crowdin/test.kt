package com.example.crowdin

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Define colors
val white = Color.White
val black = Color.Black
val gray = Color.LightGray
val lightGray = Color.LightGray
val green = Color(0xFF64C694)
val red = Color(0xFFF57C82)
val blue = Color(0xFF59A7E7)
val yellow = Color(0xFFFFD468)
val purple = Color(0xFFA287D3)
val brown = Color(0xFFDC939E)

// Define data for news items (replace with your actual data)
val newsItems = listOf(
    NewsItem(
        "Pokémon Rumble Rush Arrives Soon",
        "15 May 2019",
        R.drawable.prototype_presentation_view_in_the_figma_mobile_app
    ),
    NewsItem(
        "Pokémon Rumble Rush Arrives Soon",
        "15 May 2019",
        R.drawable.prototype_presentation_view_in_the_figma_mobile_app
    )
)

data class NewsItem(
    val title: String,
    val date: String,
    val imageRes: Int
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PokemonApp() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("10:00", color = white, fontSize = 16.sp) },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Signal",
                            tint = white,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LTE",
                            color = white,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Battery",
                            tint = white,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "100%",
                            color = white,
                            fontSize = 12.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = black
                )
            )
        },
        bottomBar = {

        },
        containerColor = white
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "What Pokémon\nare you looking for?",
                color = black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // Search bar
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Search Pokemon, Move, Ability, etc", color = gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = lightGray,
                    focusedBorderColor = lightGray,
                    unfocusedTextColor = black
                )
            )

            // Buttons
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionableButton(text = "Pokedex", backgroundColor = green)
                ActionableButton(text = "Moves", backgroundColor = red)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionableButton(text = "Abilities", backgroundColor = blue)
                ActionableButton(text = "Items", backgroundColor = yellow)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionableButton(text = "Locations", backgroundColor = purple)
                ActionableButton(text = "Type charts", backgroundColor = brown)
            }

            // News Section
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pokémon News",
                color = black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "View All",
                    color = blue,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(newsItems) { item ->
                    NewsItemCard(item)
                }
            }
        }
    }
}


@Composable
fun ActionableButton(text: String, backgroundColor: Color) {
    Button(
        onClick = { /* TODO */ },
        modifier = Modifier
            .width(160.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = white
        )
    ) {
        Text(
            text = text,
            color = white,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NewsItemCard(item: NewsItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    color = black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.date,
                    color = gray,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = "News Item Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}