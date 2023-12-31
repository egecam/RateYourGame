package com.example.rateyourgame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.rateyourgame.ViewModels.AuthViewModel
import com.example.rateyourgame.ViewModels.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController,  authViewModel: AuthViewModel, sharedViewModel: SharedViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var infoMessage by remember { mutableStateOf("") }

    // Retrieve success message from SharedViewModel
    val successMessage = sharedViewModel.successMessage.collectAsState().value

    // Update the success message when username or password is changed
    DisposableEffect(username, password) {
        onDispose {
            sharedViewModel.setSuccessMessage("") // Clear the success message
        }
    }

    // Show AlertDialog when successMessage is not null or empty
    if (successMessage?.isNotEmpty() == true) {
        AlertDialog(
            onDismissRequest = {
                sharedViewModel.setSuccessMessage("") // Clear the success message
            },
            title = {
                Text(text = "Success")
            },
            text = {
                Text(text = successMessage)
            },
            confirmButton = {
                Button(
                    onClick = {
                        sharedViewModel.setSuccessMessage("") // Clear the success message
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        if (showError) {
            AlertDialog(
                onDismissRequest = {
                    showError = false // Clear the success message
                },
                title = {
                    Text(text = "Couldn't log in")
                },
                text = {
                    Text("Username or Password is wrong. Try again.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showError = false // Clear the success message
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
        Button(
            onClick = {
                // Perform login logic
                authViewModel.viewModelScope.launch {
                    val user = authViewModel.login(username, password)
                    if (user != null) {
                        navController.navigate("game_list_screen")
                    } else {
                        showError = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Don't have an account?")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                navController.navigate("signup_screen")
            }
        ) {
            Text("Sign up")
        }
    }
}