package com.example.flashcard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flashcard.screens.CollectionScreen
import com.example.flashcard.screens.ProgressScreen
import com.example.flashcard.screens.ReviewScreen
import com.example.flashcard.screens.collections.AddCards
import com.example.flashcard.screens.collections.AddCollection
import com.example.flashcard.screens.settings.SettingsScreen
import com.example.flashcard.screens.study.CardScreen
import com.example.flashcard.viewModel.CardViewModel
import com.example.flashcard.viewModel.StudyViewModel


@Composable
fun Navigation(
	navController: NavHostController,
	cardViewModel: CardViewModel,
	studyViewModel: StudyViewModel
) {
	NavHost(
		navController = navController,
		route = Graph.HOME,
		startDestination = Screen.CollectionsScreen.route
	) {
		
		//////////////
		// Main Screen
		composable(
			Screen.CollectionsScreen.route,
//			enterTransition = {
//				slideIntoContainer(
//					AnimatedContentTransitionScope.SlideDirection.Left,
//					animationSpec = tween(100)
//				)
//			},
//			exitTransition = {
//				slideOutOfContainer(
//					AnimatedContentTransitionScope.SlideDirection.Left,
//					animationSpec = tween(100)
//				)
//			},
		) {
			CollectionScreen(
				navController, cardViewModel = cardViewModel, studyViewModel = studyViewModel
			)
		}
		composable(Screen.ReviewScreen.route) { ReviewScreen(navController, studyViewModel) }
		composable(Screen.ProgressScreen.route) { ProgressScreen(studyViewModel) }
		
		//////////////
		// Add Collection
		
		composable(Screen.AddCollectionDetailScreen.route) {
			EnterAnimation { AddCollection(viewModel = cardViewModel) }
		}
		composable(Screen.AddCardScreen.route) { AddCards(viewModel = cardViewModel) }
		
		//////////////
		// Study
		
		composable(Screen.CardScreen.route) {
			CardScreen(
				viewModel = studyViewModel,
				navController = navController
			)
		}
		
		//////////////
		// Settings
		composable(Screen.SettingsScreen.route) {
			SettingsScreen(
				navController,
				studyViewModel
			)
		}
	}
}

object Graph {
	const val HOME = "home_graph"
}

@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
	AnimatedVisibility(
		visibleState = MutableTransitionState(
			initialState = false
		).apply { targetState = true },
		modifier = Modifier,
		enter = slideInVertically(
			initialOffsetY = { -40 }
		) + expandVertically(
			expandFrom = Alignment.Top
		) + fadeIn(initialAlpha = 0.3f),
		exit = slideOutVertically() + shrinkVertically() + fadeOut(),
	) {
		content()
	}
}