package com.example.rateyourgame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.rateyourgame.R
import com.example.rateyourgame.dataclasses.Game
import com.example.rateyourgame.instances.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun GameDetailsScreen(navController: NavController, gameId: Int) {
    var game by remember { mutableStateOf<Game?>(null) }
    val apiKey = "7e45a963d9924c2cb094208bddb962b3" // Replace with your actual API key
    val customRoundShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp, // Rounds Bottom left corner
        bottomEnd = 16.dp   // Rounds Bottom right corner
    )

    LaunchedEffect(apiKey) {
        val fetchedGame = fetchDataById(gameId, apiKey)
        game = fetchedGame
    }

    game?.let { game ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .fillMaxWidth()

        )
        {
            Box(modifier = Modifier, contentAlignment = Alignment.BottomEnd){
                Image(
                    painterResource(id = R.drawable.placeholder), //Replace with actual image
                    contentDescription = "Game Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(customRoundShape),
                    contentScale = ContentScale.FillWidth
                )

            }
            Spacer(modifier = Modifier.height(3.dp))
            Column(
                modifier = Modifier
                    .padding(20.dp)
            )
            {
                Text(text = game.name, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Description", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = game.description_raw)
                Spacer(modifier = Modifier.height(20.dp))
                var currentRating by remember { mutableStateOf(0) }

                RatingBar(
                    maxRating = 5,
                    initialRating = currentRating,
                    onRatingChanged = { newRating ->
                        currentRating = newRating
                    }
                )


            }

        }
    }
}
@Composable
fun RatingBar(
    maxRating: Int = 5,
    initialRating: Int = 0,
    onRatingChanged: (Int) -> Unit
) {
    var rating by remember { mutableStateOf(initialRating) }
    var filledstar= painterResource(id =R.drawable.filled_star )
    var outlinedstar= painterResource(id = R.drawable.empty_star)

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,) {
        for (i in 1..maxRating) {
            Icon(
                painter = if (i <= rating) filledstar else outlinedstar,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(3.dp)
                    .clickable {
                        rating = i
                        onRatingChanged(i)
                    }
            )
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