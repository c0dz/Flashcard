package com.example.flashcard.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.flashcard.screens.collections.NoCollectionsFound

@Composable
fun CollectionScreen(navController: NavHostController) {
	NoCollectionsFound(navController)
}