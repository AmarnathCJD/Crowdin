package com.example.crowdin

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

const val AppName = "Crowdin"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sseClient.initSse(sseClient.handler)
        setContent {
            Notify()
            val nav = rememberNavController()
            NavHost(navController = nav, startDestination = "Home") {
                composable(
                    "Home",
                ) {
                    val authKey = LocalContext.current.getSharedPreferences("auth_prefs", 0)
                        .getString("auth-key", null)
                    if (authKey == null || authKey == "123456") {
                        nav.navigate("SignIn")
                    } else {
                        userName.value = authKey
                        Home(nav)
                    }
                }
                composable(
                    "Account",
                ) { AccountMain(nav) }
                composable(
                    "Animal",
                ) { AnimalPage(nav) }
                composable(
                    "Location",
                ) { MainLayout(nav) }
                composable(
                    "Emergency",
                ) { EmergencyVehiclePage(nav) }
                composable(
                    "AddAlert",
                ) { CreateAlertPage(nav) }
                composable(
                    "ChatView",
                ) { ChatPage(nav) }
                composable(
                    "Chats",
                ) { OpenChatsList(nav) }
                composable(
                    "SignIn",
                ) { SignInPage(nav) }
                composable(
                    "SignUp",
                ) { SignUpPage(nav) }
                composable(
                    "Alerts",
                ) { AlertsPage(nav) }
                composable(
                    "Nearby",
                ) { NearbyUsersMain(nav) }
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}
