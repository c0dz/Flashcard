package com.example.flashcard.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.flashcard.Navigation
import com.example.flashcard.ui.theme.homeBackgroundColor

@Preview
@Composable
fun MainScreen() {
	val navController = rememberNavController()
	Scaffold(
		bottomBar = { BottomBarDisplay(navController) },
		topBar = { TopBarDisplay(navController) },
	) { paddingValue ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValue)
				.background(homeBackgroundColor)
		) {
			Navigation(navController)
		}
	}
}

