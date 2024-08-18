package com.example.crowdin

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object ColorPalette {
    val primary = Color(0xFFD1D118)
    val secondary = Color(0xFF333333)
    val tertiary = Color(0xFF6A6B6A)
    val background = Color(0xFF121212)
    val surface = Color(0xFF1E1E1E)
    val onPrimary = Color(0xFFF9FBE7)
    val onSecondary = Color(0xFFBAC7BA)
    val accent = Color(0xFFE6D32C)
    val lightSurface = Color(0xFFF3E5F5)
    val redish = Color(0xFFE57373)
}

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")
val AUTH_KEY = stringPreferencesKey("auth-key")

suspend fun saveAuthKey(context: Context, authKey: String) {
    context.dataStore.edit { preferences ->
        preferences[AUTH_KEY] = authKey
    }
}

fun getAuthKey(context: Context): Flow<String?> {
    return context.dataStore.data.map { preferences ->
        preferences[AUTH_KEY]
    }
}

@Composable
fun SignUpPage(nav: NavController) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val openAlertDialog = remember { mutableStateOf(false) }

    val alertTitle = remember { mutableStateOf("Sign Up") }
    val alertText = remember { mutableStateOf("Are you sure you want to sign up?") }
    val shouldGoSignIn = remember { mutableStateOf(false) }

    when {
        shouldGoSignIn.value -> {
            nav.navigate("SignIn")
        }
    }


    when {
        openAlertDialog.value -> {
            AlertDialogMain(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    println("Confirmation registered") // Add logic here to handle confirmation.
                },
                dialogTitle = alertTitle.value,
                dialogText = alertText.value,
                icon = Icons.Default.Email
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        ColorPalette.background,
                        ColorPalette.surface,
                        ColorPalette.secondary
                    )
                )
            )
            .padding(16.dp)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to ${AppName}!!!",
            color = ColorPalette.primary,
            fontSize = 23.sp,
            modifier = Modifier.padding(bottom = 30.dp, top = 80.dp),
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Box(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Account Circle",
                    tint = ColorPalette.accent,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(32.dp)
                )
                Text(
                    text = "Create Account",
                    color = ColorPalette.lightSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    letterSpacing = 3.sp,
                )
            }
        }

        OutlinedTextField(
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.id_card_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Email",
                    modifier = Modifier.size(24.dp)
                )
            },
            value = username.value, onValueChange = { it ->
                username.value = it
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            placeholder = {
                Text(
                    "Enter your username",
                    color = ColorPalette.tertiary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ColorPalette.secondary,
                unfocusedContainerColor = Color.Transparent,
                unfocusedLeadingIconColor = ColorPalette.tertiary,
                focusedLeadingIconColor = ColorPalette.primary,
                focusedTextColor = ColorPalette.onPrimary,
                unfocusedTextColor = ColorPalette.onSecondary
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp)
        )

        OutlinedTextField(
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Email",
                    modifier = Modifier.size(24.dp)
                )
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            value = name.value, onValueChange = { it ->
                name.value = it
            },
            placeholder = {
                Text(
                    "Enter your Name",
                    color = ColorPalette.tertiary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ColorPalette.secondary,
                unfocusedContainerColor = Color.Transparent,
                unfocusedLeadingIconColor = ColorPalette.tertiary,
                focusedLeadingIconColor = ColorPalette.primary,
                focusedTextColor = ColorPalette.onPrimary,
                unfocusedTextColor = ColorPalette.onSecondary
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp)
        )

        OutlinedTextField(
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.alternate_email_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Email",
                    modifier = Modifier.size(24.dp)
                )
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            value = email.value, onValueChange = { it ->
                email.value = it
            },
            placeholder = {
                Text(
                    "Enter your email",
                    color = ColorPalette.tertiary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ColorPalette.secondary,
                unfocusedContainerColor = Color.Transparent,
                unfocusedLeadingIconColor = ColorPalette.tertiary,
                focusedLeadingIconColor = ColorPalette.primary,
                focusedTextColor = ColorPalette.onPrimary,
                unfocusedTextColor = ColorPalette.onSecondary
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp)
        )

        OutlinedTextField(
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Email",
                    modifier = Modifier.size(24.dp)
                )
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            value = password.value, onValueChange = { it ->
                password.value = it
            },
            placeholder = {
                Text(
                    "Enter your password",
                    color = ColorPalette.tertiary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ColorPalette.secondary,
                unfocusedContainerColor = Color.Transparent,
                unfocusedLeadingIconColor = ColorPalette.tertiary,
                focusedLeadingIconColor = ColorPalette.primary,
                focusedTextColor = ColorPalette.onPrimary,
                unfocusedTextColor = ColorPalette.onSecondary
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp)
        )

        OutlinedTextField(
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = "Email",
                    modifier = Modifier.size(24.dp)
                )
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ),
            value = confirmPassword.value, onValueChange = { it ->
                confirmPassword.value = it
            },
            placeholder = {
                Text(
                    "Confirm your password",
                    color = ColorPalette.tertiary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ColorPalette.secondary,
                unfocusedContainerColor = Color.Transparent,
                unfocusedLeadingIconColor = ColorPalette.tertiary,
                focusedLeadingIconColor = ColorPalette.primary,
                focusedTextColor = ColorPalette.onPrimary,
                unfocusedTextColor = ColorPalette.onSecondary
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val emailPattern = Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$")
                    if (password.value != confirmPassword.value) {
                        alertTitle.value = "Password Mismatch"
                        alertText.value = "Passwords do not match. Please try again."
                        openAlertDialog.value = true
                    } else if (username.value.isEmpty() || password.value.isEmpty() || confirmPassword.value.isEmpty() || email.value.isEmpty() || name.value.isEmpty()) {
                        alertTitle.value = "Empty Fields"
                        alertText.value = "Please fill in all the fields."
                        openAlertDialog.value = true
                    } else if (!email.value.matches(emailPattern)) {
                        alertTitle.value = "Invalid Email"
                        alertText.value = "Please enter a valid email."
                        openAlertDialog.value = true
                    } else {
                        val client = OkHttpClient()
                        val request = Request.Builder()
                            .url("$BASE_URL/user/sign_up")
                            .post(
                                JSONObject(
                                    mapOf(
                                        "username" to username.value,
                                        "password" to password.value,
                                        "email" to email.value,
                                        "name" to name.value
                                    )
                                ).toString()
                                    .toRequestBody("application/json".toMediaTypeOrNull())
                            )
                            .build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                println("Failed to send request")
                            }

                            override fun onResponse(call: Call, response: okhttp3.Response) {
                                if (response.isSuccessful) {
                                    shouldGoSignIn.value = true
                                }
                            }
                        })
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorPalette.primary,
                    contentColor = ColorPalette.secondary
                ),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, ColorPalette.primary),
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .width(180.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "Sign Up",
                    color = ColorPalette.secondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        SignUpFooter(nav)
    }
}

@Composable
fun AlertDialogMain(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        },
        containerColor = ColorPalette.secondary,
        textContentColor = ColorPalette.onSecondary,
        titleContentColor = ColorPalette.onPrimary,
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignInPage(nav: NavController) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val alertTitle = remember { mutableStateOf("Sign Up") }
    val alertText = remember { mutableStateOf("Are you sure you want to sign up?") }
    val openAlertDialog = remember { mutableStateOf(false) }

    val isAuthed = remember { mutableStateOf(false)}
    val authKey = remember { mutableStateOf("") }
    when {
        isAuthed.value -> {
            LocalContext.current.getSharedPreferences("auth_prefs", 0).edit().putString("auth-key", authKey.value).apply()
            nav.navigate("Home")
        }
    }

    when {
        openAlertDialog.value -> {
            AlertDialogMain(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = alertTitle.value,
                dialogText = alertText.value,
                icon = Icons.Default.Email
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        ColorPalette.background,
                        ColorPalette.surface,
                        ColorPalette.secondary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            ColorPalette.background,
                            ColorPalette.surface,
                            ColorPalette.secondary
                        )
                    )
                )
                .padding(16.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to ${AppName}!!!",
                color = ColorPalette.primary,
                fontSize = 23.sp,
                modifier = Modifier.padding(bottom = 30.dp, top = 80.dp),
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Box(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Account Circle",
                        tint = ColorPalette.accent,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(32.dp)
                    )
                    Text(
                        text = "Sign In",
                        color = ColorPalette.lightSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 3.sp,
                    )
                }
            }

            OutlinedTextField(
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.id_card_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Email",
                        modifier = Modifier.size(24.dp)
                    )
                },
                value = username.value, onValueChange = { it ->
                    username.value = it
                },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ),
                placeholder = {
                    Text(
                        "Enter your username",
                        color = ColorPalette.tertiary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ColorPalette.secondary,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedLeadingIconColor = ColorPalette.tertiary,
                    focusedLeadingIconColor = ColorPalette.primary,
                    focusedTextColor = ColorPalette.onPrimary,
                    unfocusedTextColor = ColorPalette.onSecondary,
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 10.dp)
            )

            OutlinedTextField(
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Email",
                        modifier = Modifier.size(24.dp)
                    )
                },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ),
                visualTransformation = PasswordVisualTransformation(),
                value = password.value, onValueChange = { it ->
                    password.value = it
                },
                placeholder = {
                    Text(
                        "Enter your password",
                        color = ColorPalette.tertiary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ColorPalette.secondary,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedLeadingIconColor = ColorPalette.tertiary,
                    focusedLeadingIconColor = ColorPalette.primary,
                    focusedTextColor = ColorPalette.onPrimary,
                    unfocusedTextColor = ColorPalette.onSecondary,
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (username.value.isEmpty() || password.value.isEmpty()) {
                            alertTitle.value = "Empty Fields"
                            alertText.value = "Please fill in all the fields."
                            openAlertDialog.value = true
                        } else {
                            val client = OkHttpClient()
                            val request = Request.Builder()
                                .url("$BASE_URL/user/login")
                                .post(
                                    JSONObject(
                                        mapOf(
                                            "username" to username.value,
                                            "password" to password.value
                                        )
                                    ).toString()
                                        .toRequestBody("application/json".toMediaTypeOrNull())
                                )
                                .build()

                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    println("Failed to send request")
                                }

                                override fun onResponse(call: Call, response: okhttp3.Response) {
                                    println("Response received: ${response.body?.string()}")
                                    if (response.isSuccessful) {
                                        authKey.value = username.value
                                        isAuthed.value = true
                                    }
                                }
                            })
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorPalette.primary,
                        contentColor = ColorPalette.secondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, ColorPalette.primary),
                    modifier = Modifier
                        .padding(vertical = 14.dp)
                        .width(180.dp)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Sign In",
                        color = ColorPalette.secondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            SignInFooter(nav)
        }
    }
}

@Composable
fun SignUpFooter(nav: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Already have an account?",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sign In",
                color = ColorPalette.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        nav.navigate("SignIn")
                    }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Privacy Policy",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Terms of Service",
                color = ColorPalette.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Forgot Password?",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Reset Password",
                color = ColorPalette.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "© 2024 ${AppName}. All rights reserved.",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SignInFooter(nav: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Don't have an account?",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sign Up",
                color = ColorPalette.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        nav.navigate("SignUp")
                    }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Privacy Policy",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Terms of Service",
                color = ColorPalette.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Forgot Password?",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Reset Password",
                color = ColorPalette.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "© 2024 ${AppName}. All rights reserved.",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EmailOTPVefication() {
    val otp = remember { mutableStateOf("") }

    val emailPattern = Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$")
    val sentOTP = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        ColorPalette.background,
                        ColorPalette.surface,
                        ColorPalette.secondary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            ColorPalette.background,
                            ColorPalette.surface,
                            ColorPalette.secondary
                        )
                    )
                )
                .padding(16.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to ${AppName}!!!",
                color = ColorPalette.primary,
                fontSize = 23.sp,
                modifier = Modifier.padding(bottom = 30.dp, top = 80.dp),
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Box(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Account Circle",
                        tint = ColorPalette.accent,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(32.dp)
                    )
                    Text(
                        text = "Email OTP Verification",
                        color = ColorPalette.lightSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 3.sp,
                    )
                }
            }

            OutlinedTextField(
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.alternate_email_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Email",
                        modifier = Modifier.size(24.dp)
                    )
                },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ),
                value = "", onValueChange = { },
                placeholder = {
                    Text(
                        "Enter your email",
                        color = ColorPalette.tertiary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ColorPalette.secondary,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedLeadingIconColor = ColorPalette.tertiary,
                    focusedLeadingIconColor = ColorPalette.primary,
                    focusedTextColor = ColorPalette.onPrimary,
                    unfocusedTextColor = ColorPalette.onSecondary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 10.dp)
            )

            OutlinedTextField(
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                        contentDescription = "Email",
                        modifier = Modifier.size(24.dp)
                    )
                },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ),
                visualTransformation = PasswordVisualTransformation(),
                value = "", onValueChange = { },
                placeholder = {
                    Text(
                        "Enter your OTP",
                        color = ColorPalette.tertiary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ColorPalette.secondary,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedLeadingIconColor = ColorPalette.tertiary,
                    focusedLeadingIconColor = ColorPalette.primary,
                    focusedTextColor = ColorPalette.onPrimary,
                    unfocusedTextColor = ColorPalette.onSecondary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        sentOTP.value = !sentOTP.value
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorPalette.primary,
                        contentColor = ColorPalette.secondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, ColorPalette.primary),
                    modifier = Modifier
                        .padding(vertical = 14.dp)
                        .width(180.dp)
                        .height(48.dp)
                ) {
                    Text(
                        text = if (sentOTP.value) "Verify OTP" else "Send OTP",
                        color = ColorPalette.secondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            EmailOTPFooter()
        }
    }
}

@Composable
fun EmailOTPFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Didn't receive the email?",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Resend Email",
                color = ColorPalette.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Privacy Policy",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Terms of Service",
                color = ColorPalette.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Forgot Password?",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Reset Password",
                color = ColorPalette.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "© 2024 ${AppName}. All rights reserved.",
                color = ColorPalette.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// TODO: ForgotPasswordPage