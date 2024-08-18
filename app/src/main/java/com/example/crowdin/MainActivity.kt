package com.example.crowdin

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleIn(
                            initialScale = 1.1f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeIn(animationSpec = tween(700))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeOut(animationSpec = tween(700))
                    },
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
                    "Settings",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleIn(
                            initialScale = 1.1f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeIn(animationSpec = tween(700))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeOut(animationSpec = tween(700))
                    },
                ) { }
                composable(
                    "Location",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleIn(
                            initialScale = 1.1f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeIn(animationSpec = tween(700))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeOut(animationSpec = tween(700))
                    },
                ) {
                    addNavEntry("Location")
                    MainLayout(nav)
                } // map
                composable(
                    "AddAlert",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleIn(
                            initialScale = 1.1f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeIn(animationSpec = tween(700))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeOut(animationSpec = tween(700))
                    },
                ) {
                    addNavEntry("AddAlert")
                    CreateAlert()
                }
                composable(
                    "ChatView",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleIn(
                            initialScale = 1.1f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeIn(animationSpec = tween(700))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeOut(animationSpec = tween(700))
                    },
                ) {
                    addNavEntry("ChatView")
                    ChatPage(nav)
                }
                composable(
                    "Chats",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 700),
                        ) + scaleIn(
                            initialScale = 1.1f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeIn(animationSpec = tween(700))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeOut(animationSpec = tween(700))
                    },
                ) { OpenChatsList(nav) }
                composable(
                    "SignIn",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleIn(
                            initialScale = 1.1f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeIn(animationSpec = tween(700))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeOut(animationSpec = tween(700))
                    },
                ) { SignInPage(nav) }
                composable(
                    "SignUp",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(durationMillis = 700),
                        ) + scaleIn(
                            initialScale = 1.1f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeIn(animationSpec = tween(700))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 1000 },
                            animationSpec = tween(durationMillis = 700)
                        ) + scaleOut(
                            targetScale = 0.8f,
                            animationSpec = tween(durationMillis = 700)
                        ) + fadeOut(animationSpec = tween(700))
                    },
                ) { SignUpPage(nav) }
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
