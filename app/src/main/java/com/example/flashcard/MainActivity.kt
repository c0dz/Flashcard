package com.example.flashcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.dao.SessionDao
import com.example.flashcard.model.database.CardDatabase
import com.example.flashcard.screens.main.MainScreen
import com.example.flashcard.viewModel.CardViewModel
import com.example.flashcard.viewModel.StudyViewModel

class MainActivity : ComponentActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		val splashScreen = installSplashScreen()
		val startTime = System.currentTimeMillis()
		// 4 seconds delay in the splash screen
		splashScreen.setKeepOnScreenCondition {
			System.currentTimeMillis() - startTime < 3000
		}
		super.onCreate(savedInstanceState)
		enableEdgeToEdge() // TODO: edge to edge not working
		setContent {
			val navController = rememberNavController()
			val collectionDao: CollectionDao = CardDatabase
				.getInstance(navController.context).collectionDao
			val cardDoa: CardDao = CardDatabase.getInstance(navController.context).cardDao
			val sessionDao: SessionDao = CardDatabase
				.getInstance(navController.context).sessionDao
			
			// Card ViewModel
			val cardViewModel: CardViewModel = viewModel<CardViewModel>(
				factory = object : ViewModelProvider.Factory {
					override fun <T : ViewModel> create(modelClass: Class<T>): T {
						return CardViewModel(
							cardDao = cardDoa,
							collectionDao = collectionDao
						) as T
					}
				}
			)
			
			// Study ViewModel
			val studyViewModel: StudyViewModel = viewModel<StudyViewModel>(
				factory = object : ViewModelProvider.Factory {
					override fun <T : ViewModel> create(modelClass: Class<T>): T {
						return StudyViewModel(
							cardDao = cardDoa,
							collectionDao = collectionDao,
							sessionDao = sessionDao
						) as T
					}
				}
			)
			
			Box(
				Modifier
					.safeDrawingPadding()
			) {
				MainScreen(
					navController = navController,
					cardViewModel = cardViewModel,
					studyViewModel = studyViewModel
				)
			}
		}
	}
}

