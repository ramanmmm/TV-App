package com.example.tv_app.Signup

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tv_app.View_Model.AuthViewModel

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequesterEmail = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequesterEmail.requestFocus()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Sign Up", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .focusRequester(focusRequesterEmail)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyUp &&
                            keyEvent.key == Key.Enter
                        ) {
                            keyboardController?.show()
                            true
                        } else false
                    }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyUp &&
                            keyEvent.key == Key.Enter
                        ) {
                            keyboardController?.show()
                            true
                        } else false
                    }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                viewModel.signup(email, password) { signupSuccess ->
                    if (signupSuccess) {
                        successMessage = "Account created successfully!"
                        navController.navigate("home")
                    } else {
                        Toast.makeText(context, "Email already registered!", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Sign Up")
            }


            Spacer(modifier = Modifier.height(10.dp))
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Log in")
            }

            successMessage?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}