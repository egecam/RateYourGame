package com.example.rateyourgame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.example.rateyourgame.dataclasses.User
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController, authViewModel: AuthViewModel, sharedViewModel: SharedViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showEmptyCredentialsError by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }

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
                    showError = false
                },
                title = {
                    Text(text = "Couldn't Sign Up")
                },
                text = {
                    Text("Account with the same username or password already exists.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showError = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        if (showEmptyCredentialsError) {
            AlertDialog(
                onDismissRequest = {
                    showEmptyCredentialsError = false
                },
                title = {
                    Text(text = "Couldn't Sign Up")
                },
                text = {
                    Text("Username or password can't be empty.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showEmptyCredentialsError = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        if (showSuccessMessage) {
            navController.previousBackStackEntry?.arguments?.putString("success_message", "Account created successfully!")
        }
        Button(
            onClick = {
                authViewModel.viewModelScope.launch {
                    val existingUser = authViewModel.isUserExists(username, password)
                    if (existingUser) {
                        showError = true
                    } else if(username == "" || password == "") {
                        showEmptyCredentialsError = true
                    }
                    else {
                        val newUser = User(username = username, password = password)
                        authViewModel.signUp(newUser)
                        sharedViewModel.setSuccessMessage("Account created successfully!")

                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Sign Up")
        }
    }
}