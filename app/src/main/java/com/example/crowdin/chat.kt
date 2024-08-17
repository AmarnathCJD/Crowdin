package com.example.crowdin

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

@SuppressLint("SecretInSource")
val generativeModel = GenerativeModel(
    modelName = "gemini-1.5-flash",
    apiKey = "AIzaSyAb7QIx6a0PSLTVZbcjBZW7C51MHbrW8sE",
    systemInstruction = Content(
        role = "system",
        parts = listOf(
            TextPart(
                text = "You are Crowdin AI, an InApp assistant for the app named 'Crowdin', The app is a Crowd Alert App which alerts the users about nearby traffic related issues, and animal attacks reported, the primary source of data is the users itself, so be like the ai for tha pp and assist users needs, dont answer out of scope questions, also provide disaster relief information, and help users with their account related issues, also no support for markdown formattings, so dont add formattings to the response, also help if the query is related to latlon etc, which deals with locating, alerts, etc., dont add ** in message for formatting"
            )
        )
    ),
    generationConfig = generationConfig {
        temperature = 0.75f
        topP = 1.0f
        topK = 300
        maxOutputTokens = 4096
    },
)


@Composable
fun ChatPage(nav: NavController) {
//    BackHandler {
//        nav.navigate(popNavEntry())
//    }

    val scrollState = rememberScrollState()
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
                Column {
                    ChatInput()
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
                                color = ColorPalette.redish,
                                name = "Home"
                            )
                            BottomIconItem(
                                imageRes = R.drawable.my_location_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                                color = ColorPalette.secondary,
                                name = "Location"
                            )
                            BottomIconItem(
                                imageRes = R.drawable.admin_panel_settings_24dp_e8eaed_fill0_wght400_grad0_opsz24,
                                color = ColorPalette.secondary,
                                name = "Account"
                            )
                        }
                    }
                }
            },
            containerColor = Color.Transparent,
            content = {
                ChatPageMain(it, scrollState)
            }
        )
    }
}

@Composable
fun ChatPageMain(paddingValues: PaddingValues, scrollState: ScrollState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding() - 30.dp,
                bottom = paddingValues.calculateBottomPadding(),
            )
    ) {
        ChatTitle()
        ChatList(scrollState)
    }
}

var chatText by mutableStateOf("")
var chatTitle by mutableStateOf("Crowdin AI Chat")
var isAiChat by mutableStateOf(true)
var scrollStatePosition by mutableIntStateOf(0)
var convoId by mutableStateOf(1)
var myName by mutableStateOf("User")
var aiChat = generativeModel.startChat(listOf(
    Content(
        role = "user",
        parts = listOf(
            TextPart(
                text = "Username: Jenna M Ortega"
            )
        )
    )
))

suspend fun prompt(prompt: String) {
    val response = aiChat.sendMessage(
        prompt
    )
    if (response.text != null) {
        sseClient.ChatViewModel.sendMessage("1", response.text!!.trimIndent(), "AI")
    }
}

@Composable
fun ChatInput() {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ColorPalette.lightSurface)
                .border(1.dp, ColorPalette.onPrimary, RoundedCornerShape(20.dp)),
        ) {
            OutlinedTextField(
                value = chatText,
                onValueChange = { chatText = it },
                placeholder = { Text("Type a message", fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(10.dp, Color.Transparent, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.send_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Send",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                if (chatText.isEmpty()) return@clickable
                                val prompt = chatText
                                sseClient.ChatViewModel.sendMessage(convoId.toString(), chatText, myName)
                                chatText = ""
                                if (convoId == 1) {
                                    coroutineScope.launch {
                                        typingAnimation = 1
                                        prompt(prompt)
                                        typingAnimation = 0
                                        return@launch
                                    }
                                }
                            },
                        alignment = Alignment.Center,
                        colorFilter = ColorFilter.tint(ColorPalette.secondary)
                    )
                }
            )
        }
    }
}

var typingAnimation by mutableIntStateOf(0)
var typingPerson by mutableStateOf("AI")

@Composable
fun ChatTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(ColorPalette.lightSurface)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chatTitle,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color(0xFF9575CD),
                        fontWeight = FontWeight.Bold
                    )
                }
                if (typingAnimation != 0) {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$typingPerson is typing...",
                            fontSize = 12.sp,
                            color = Color(0xFFA7A4AC),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 36.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatList(scrollState: ScrollState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(scrollState)
    ) {
        LaunchedEffect(scrollStatePosition) {
            scrollState.scrollTo(scrollStatePosition)
        }
        if (sseClient.ChatViewModel.getMessages(convoId.toString()).isNotEmpty()) {
            scrollStatePosition = scrollState.maxValue
        }
        sseClient.ChatViewModel.getMessages(convoId.toString()).forEach {
            ChatItem(it)
        }
        if (typingAnimation != 0) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$typingPerson is typing...",
                    fontSize = 12.sp,
                    color = Color(0xFFA7A4AC),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 36.dp)
                )
            }
        }
    }
}

@Composable
fun ChatItem(chatData: ChatMessage) {
    val isUser = chatData.sender == myName
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .padding(
                start = if (isUser) 80.dp else 10.dp,
                end = if (isUser) 10.dp else 80.dp
            )
    ) {
        if (isUser) {
            Spacer(modifier = Modifier.weight(1f))
        }
        Column(
            modifier = Modifier
                .background(
                    color = if (isUser) Color(0xFFF3E5F5)
                    else Color(0xFFE8EAF6),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
                .padding(end = 20.dp)
                .wrapContentSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = chatData.sender,
                    fontSize = 12.sp,
                    color = Color(0xFF3F51B5),
                    fontWeight = FontWeight.Bold
                )

                if (chatData.sender == "AI") {
                    Image(
                        painter = painterResource(id = R.drawable.smart_toy_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "AI",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(
                                bottom = 6.dp,
                                start = 6.dp
                            ),
                        colorFilter = ColorFilter.tint(Color(0xFF3F51B5))
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = chatData.message,
                fontSize = 16.sp,
                color = Color(0xFF424444),
                modifier = Modifier.padding(top = 5.dp),
            )
            Row(
                horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
            ) {
                Text(
                    text = timeMsToTimeString(chatData.timestamp),
                    fontSize = 9.sp,
                    color = Color(0xFF424444),
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        if (!isUser) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun OpenChatsList(nav: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                Column {
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
                                name = "Animals",
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
                }
            },
            containerColor = Color.Transparent,
            content = {
                OpenChatsListMain(paddingValues = it)
            }
        )
    }
}

@Composable
fun OpenChatsListMain(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding() - 30.dp,
                bottom = paddingValues.calculateBottomPadding(),
            )
    ) {
        AllChatTitle()
        AllChatList(rememberScrollState())
    }
}

@Composable
fun AllChatTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(ColorPalette.lightSurface)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Click on a chat to open",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color(0xFF9575CD),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

class Chat(
    val chatId: Int,
    val name: String,
    val description: String = ""
)

@Composable
fun AllChatList(scrollState: ScrollState) {
    val chatList = remember { mutableStateOf(listOf(Chat(1, "Crowdin AI", "Your InApp Assistant")))}
    LaunchedEffect(Unit) {
        sseClient.ChatViewModel.getChatsList(chatList)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(scrollState)
    ) {
        chatList.value.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .padding(
                        start = 10.dp,
                        end = 10.dp
                    )
                    .clickable {
                        convoId = it.chatId
                        chatTitle = it.name
                        isAiChat = it.name == "Crowdin AI"
                        sseClient.ChatViewModel.clearMessages()
                    }
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFE8EAF6),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(15.dp)
                        .padding(end = 20.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = if (it.name == "Crowdin AI") R.drawable.star_24dp_e8eaed_fill0_wght400_grad0_opsz24 else R.drawable.forum_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                            contentDescription = "AI",
                            modifier = Modifier
                                .size(36.dp)
                                .padding(
                                    end = 12.dp
                                ),
                            colorFilter = ColorFilter.tint(Color(0xFFEF6C00))
                        )
                        Text(
                            text = it.name,
                            fontSize = 16.sp,
                            color = Color(0xFF3F51B5),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "(?)",
                            fontSize = 12.sp,
                            color = Color(0xFF558B2F),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}