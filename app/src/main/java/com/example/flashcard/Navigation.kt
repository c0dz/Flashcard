package com.example.flashcard

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flashcard.NavBarComposables.CollectionScreen
import com.example.flashcard.NavBarComposables.ProgressScreen
import com.example.flashcard.NavBarComposables.ReviewScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
	NavHost(navController = navController, startDestination = Screen.CollectionScreen.route) {
		composable(Screen.CollectionScreen.route) { CollectionScreen() }
		composable(Screen.ReviewScreen.route) { ReviewScreen() }
		composable(Screen.ProgressScreen.route) { ProgressScreen() }
	}
}