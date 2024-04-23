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