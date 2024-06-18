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
import com.example.flashcard.viewmodel.CardViewModel
import com.example.flashcard.viewmodel.StudyViewModel

/**
 * Composable function for the main screen of the application.
 *
 * This function defines the layout for the main screen, which includes a Scaffold with a top bar, bottom bar, and content area.
 * The top bar displays navigation options based on the current destination, while the bottom bar provides additional functionality.
 * The content area contains the main navigation flow of the application.
 *
 * @param navController The navigation controller used for navigating between screens.
 * @param cardViewModel The view model responsible for managing card-related data and operations.
 * @param studyViewModel The view model responsible for managing study-related data and operations.
 */
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

