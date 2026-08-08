package com.example.mathquest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatisticsScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Top
    ) {

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "STATISTICS",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        StatisticCard(
            title = "Best Score",
            value = "0 / 5"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        StatisticCard(
            title = "Games Played",
            value = "0"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        StatisticCard(
            title = "Correct Answers",
            value = "0"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        StatisticCard(
            title = "Accuracy",
            value = "0%"
        )
    }
}

@Composable
private fun StatisticCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = value,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}