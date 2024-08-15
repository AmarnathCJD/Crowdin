package com.example.crowdin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val email = mutableStateOf("")

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
    val iconBackground = Color(0xFF1E1E1E)
    val iconTint = Color(0xFFBAC7BA)

    val redish = Color(0xFFE57373)
    val greenish = Color(0xFF81C784)
    val blueish = Color(0xFF64B5F6)
    val yellowish = Color(0xFFFFD54F)
}

@Composable
fun SignUpPage() {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ColorPalette.background, ColorPalette.surface, ColorPalette.secondary)))
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
                Image(painter = painterResource(id = R.drawable.alternate_email_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "Email",
                    modifier = Modifier.size(24.dp))
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
                Image(painter = painterResource(id = R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "Email",
                    modifier = Modifier.size(24.dp))
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
                Image(painter = painterResource(id = R.drawable.lock_24dp_e8eaed_fill0_wght400_grad0_opsz24), contentDescription = "Email",
                    modifier = Modifier.size(24.dp))
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
                onClick = { /*TODO*/ },
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
        SignUpFooter()
    }
}

@Composable
fun SignInPage() {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ColorPalette.background, ColorPalette.surface, ColorPalette.secondary)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(ColorPalette.background, ColorPalette.surface, ColorPalette.secondary)))
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

            // Repeat for other OutlinedTextFields...

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /*TODO*/ },
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

            SignInFooter()
        }
    }
}

@Composable
fun SignUpFooter() {
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

@Composable
fun SignInFooter() {
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

@Composable
fun EmailOTPVefication() {
    val otp = remember { mutableStateOf("") }

    val emailPattern = Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$")
    val sentOTP = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ColorPalette.background, ColorPalette.surface, ColorPalette.secondary)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(ColorPalette.background, ColorPalette.surface, ColorPalette.secondary)))
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