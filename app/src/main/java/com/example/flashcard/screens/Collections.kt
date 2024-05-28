package com.example.flashcard.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.flashcard.screens.collections.CollectionList
import com.example.flashcard.screens.collections.NoCollectionsFound
import com.example.flashcard.viewModel.CardViewModel
import com.example.flashcard.viewModel.StudyViewModel

/**
 * A composable function that displays the collection screen for the user's flashcards.
 *
 * This function creates a UI that displays a list of collections retrieved from the [CardViewModel].
 * If no collections are found, it displays a message indicating no collections are available.
 * Otherwise, it displays the list of collections.
 *
 * @param navController The [NavHostController] used for navigation between screens.
 * @param cardViewModel The [CardViewModel] instance containing the card collections data and logics.
 * @param studyViewModel The [StudyViewModel] instance containing the study data and logic.
 *
 * The screen consists of a column layout that:
 * - Collects the collections from the [CardViewModel].
 * - Displays a message if no collections are found.
 * - Displays a list of collections if available.
 *
 * @sample
 * val navController = rememberNavController()
 * val cardViewModel = CardViewModel()
 * val studyViewModel = StudyViewModel()
 * CollectionScreen(navController = navController, cardViewModel = cardViewModel, studyViewModel = studyViewModel)
 */
@Composable
fun CollectionScreen(
	navController: NavHostController,
	cardViewModel: CardViewModel,
	studyViewModel: StudyViewModel
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		val collections by cardViewModel.collections.collectAsState(
			initial = emptyList()
		)
		
		if (collections.isEmpty()) {
			NoCollectionsFound(navController, cardViewModel)
		} else {
			CollectionList(
				collections = collections,
				navController = navController,
				studyViewModel = studyViewModel
			)
		}
	}
}