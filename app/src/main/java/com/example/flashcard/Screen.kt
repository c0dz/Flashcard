package com.example.flashcard

sealed class Screen(val route: String) {
	data object CollectionScreen : Screen("collection_screen")
	data object ReviewScreen : Screen("review_screen")
	data object ProgressScreen : Screen("progress_screen")
}