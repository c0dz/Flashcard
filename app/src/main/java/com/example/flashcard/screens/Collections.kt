package com.example.flashcard.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.flashcard.screens.collections.CollectionList
import com.example.flashcard.screens.collections.NoCollectionsFound
import com.example.flashcard.viewModel.CardViewModel

@Composable
fun CollectionScreen(navController: NavHostController, viewModel: CardViewModel) {
	val collections by viewModel.collections.collectAsState(
		initial = emptyList()
	)
	
	if (collections.isEmpty()) {
		NoCollectionsFound(navController, viewModel)
	} else {
		CollectionList(collections = collections)
	}
}