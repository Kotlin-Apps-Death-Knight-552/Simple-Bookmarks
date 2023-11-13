package com.knightshrestha.bookmarks.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.knightshrestha.bookmarks.viewmodel.MainViewModel

@Composable
fun LoginScreen(viewModel: MainViewModel) {
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        var email by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(value = email, onValueChange = {
                email = it
            },
                label = { Text(text = "Email Address") })
            OutlinedTextField(value = password, onValueChange = {
                password = it
            },
                label = { Text(text = "Password") })
            Button(onClick = {
                isLoading = true
                viewModel.login(
                email, password
            ) },
                enabled = !isLoading) {
                if (isLoading) {
                    Text(text = "Apple")
                } else {
                    Text(text = "Ball")
                }

            }
        }

    }
}