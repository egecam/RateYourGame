package com.example.rateyourgame.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rateyourgame.dataclasses.Game
import com.example.rateyourgame.instances.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun GameDetailsScreen(navController: NavController, gameId: Int) {
    var game by remember { mutableStateOf<Game?>(null) }
    val apiKey = "7e45a963d9924c2cb094208bddb962b3" // Replace with your actual API key

    LaunchedEffect(apiKey) {
        val fetchedGame = fetchDataById(gameId, apiKey)
        game = fetchedGame
    }

    game?.let { game ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(state = scrollState)
        )
        {
            Text(text = game.name, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(text = "Description", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = game.description_raw)
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Go Back")
            }
        }
    }
}

suspend fun fetchDataById(gameId: Int, apiKey: String): Game? {
    return withContext(Dispatchers.IO) {
        val response = RetrofitInstance.rawgApi.getGameDetails(gameId, apiKey)
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}