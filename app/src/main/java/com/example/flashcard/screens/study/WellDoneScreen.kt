package com.example.flashcard.screens.study

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.Screen
import com.example.flashcard.ui.theme.buttonColorGreen
import com.example.flashcard.ui.theme.greenTextColor

@Composable
fun WellDoneScreen(navController: NavHostController) {
	Column(
		modifier = Modifier
			.fillMaxSize(),
		verticalArrangement = Arrangement.SpaceBetween,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Well Done",
			color = greenTextColor,
			fontWeight = FontWeight.Medium,
			fontSize = 25.sp
		)
		TextButton(
			modifier = Modifier
				.background(buttonColorGreen),
			onClick = {
				navController.navigate(Screen.CollectionsScreen.route)
				
			}
		) {
			Text(
				text = "Back to Collections",
				color = Color.White
			)
		}
	}
}