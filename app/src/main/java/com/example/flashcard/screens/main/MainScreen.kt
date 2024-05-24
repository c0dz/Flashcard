package com.example.flashcard.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.flashcard.Navigation
import com.example.flashcard.ui.theme.homeBackgroundColor
import com.example.flashcard.viewModel.CardViewModel
import com.example.flashcard.viewModel.StudyViewModel

@Composable
fun MainScreen(
	navController: NavHostController,
	cardViewModel: CardViewModel,
	studyViewModel: StudyViewModel,
) {
	Scaffold(
		bottomBar = { BottomBarDisplay(navController, cardViewModel) },
		topBar = { TopBarDisplay(navController, cardViewModel) },
	) { paddingValue ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(homeBackgroundColor)
				.padding(
					paddingValue,
				)
		) {
			Navigation(navController, cardViewModel, studyViewModel)
		}
	}
}

