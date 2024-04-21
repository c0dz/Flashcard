package com.example.flashcard.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.flashcard.Navigation
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.database.CardDatabase
import com.example.flashcard.ui.theme.homeBackgroundColor
import com.example.flashcard.viewModel.CardViewModel

@Preview
@Composable
fun MainScreen() {
	val navController = rememberNavController()
	
	//val collectionDao: CollectionDao =
	//	CollectionDatabase.getInstance(navController.context).collectionDao
	val collectionDao: CollectionDao = CardDatabase
		.getInstance(navController.context).collectionDao
	val cardDoa: CardDao = CardDatabase.getInstance(navController.context).cardDao
	
	
	val cardViewModel: CardViewModel = viewModel(factory = object : ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			return CardViewModel(cardDoa, collectionDao) as T
		}
	})
	
	Scaffold(
		bottomBar = { BottomBarDisplay(navController, cardViewModel) },
		topBar = { TopBarDisplay(navController, cardViewModel) },
	) { paddingValue ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValue)
				.background(homeBackgroundColor)
		) {
			Navigation(navController, cardViewModel)
		}
	}
}

