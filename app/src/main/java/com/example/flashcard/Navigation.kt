package com.example.flashcard

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flashcard.screens.CollectionScreen
import com.example.flashcard.screens.ProgressScreen
import com.example.flashcard.screens.ReviewScreen
import com.example.flashcard.screens.collections.AddCards
import com.example.flashcard.screens.collections.AddCollection
import com.example.flashcard.viewModel.CardViewModel


@Composable
fun Navigation(
	navController: NavHostController,
	cardViewModel: CardViewModel
) {
	
	
	NavHost(
		navController = navController,
		route = Graph.HOME,
		startDestination = Screen.CollectionsScreen.route
	) {
		
		composable(Screen.CollectionsScreen.route) {
			CollectionScreen(
				navController
			)
		}
		// Main Page
		composable(Screen.ReviewScreen.route) { ReviewScreen() }
		composable(Screen.ProgressScreen.route) { ProgressScreen() }
		
		//////////////
		// Add Collection
		
		composable(Screen.AddCollectionDetailScreen.route) { AddCollection(viewModel = cardViewModel) }
		composable(Screen.AddCardScreen.route) { AddCards(viewModel = cardViewModel) }
	}
}

object Graph {
	const val HOME = "home_graph"
}