package com.example.flashcard.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flashcard.R
import com.example.flashcard.Screen
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.viewModel.CardViewModel


@Composable
fun TopBarDisplay(navController: NavHostController, viewModel: CardViewModel) {
	val homeScreens = listOf(
		Screen.CollectionsScreen,
		Screen.ReviewScreen,
		Screen.ProgressScreen
	)
	
	val addCollectionScreens = listOf(
		Screen.AddCardScreen,
		Screen.AddCollectionDetailScreen
	)
	
	val studyScreens = listOf(
		Screen.StudyQuestionScreen,
		Screen.StudyAnswerScreen
	)
	
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry?.destination
	
	//Check for the current route and choose the Top Bar
	val displayHomeTopBar = homeScreens.any { it.route == currentDestination?.route }
	val displayAddCollectionTopBar =
		addCollectionScreens.any { it.route == currentDestination?.route }
	val displayStudyTopBar = studyScreens.any { it.route == currentDestination?.route }
	
	if (displayHomeTopBar) {
		HomeTopBar(navController, viewModel)
	} else if (displayAddCollectionTopBar) {
		AddCollectionTopBar(navController)
	} else if (displayStudyTopBar) {
		StudyTopBar(navController, viewModel)
		
	}
}

@Composable
fun HomeTopBar(navController: NavHostController, viewModel: CardViewModel) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(color = bottomTopAppBarColor)
			.padding(16.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		Image(painter = painterResource(R.drawable.setting_icon),
			contentDescription = "settings",
			modifier = Modifier.clickable { })
		Text(
			text = "Collections",
			color = Color.White,
			fontSize = 18.sp,
			fontStyle = FontStyle.Normal,
			fontWeight = FontWeight.Bold
		)
		Image(
			painter = painterResource(R.drawable.add_collection_icon),
			contentDescription = "add_collection",
			modifier = Modifier.clickable {
				viewModel.clearTempData()
				navController.navigate(Screen.AddCollectionDetailScreen.route)
			}
		)
	}
}

@Composable
fun AddCollectionTopBar(navController: NavHostController) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentScreen = navBackStackEntry?.destination?.route
	
	var selectedItem by rememberSaveable {
		mutableIntStateOf(0)
	}
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(color = bottomTopAppBarColor)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			Image(painter = painterResource(R.drawable.close_icon),
				contentDescription = "close",
				modifier = Modifier.clickable {
					navController.navigate(Screen.CollectionsScreen.route)
				})
			Text(
				text = "New Collection",
				textAlign = TextAlign.Center,
				color = Color.White,
				fontSize = 18.sp,
				fontStyle = FontStyle.Normal,
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.fillMaxWidth(1f)
			)
			Spacer(
				modifier = Modifier
					.width(5.dp)
			)
		}
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
				.background(bottomTopAppBarColor),
			horizontalArrangement = Arrangement.SpaceAround
		) {
			Text(
				text = "Details",
				modifier = Modifier
					.padding(horizontal = 10.dp, vertical = 5.dp),
				fontWeight = if (currentScreen == Screen.AddCollectionDetailScreen.route) FontWeight.Bold else FontWeight.Normal,
				fontSize = if (currentScreen == Screen.AddCollectionDetailScreen.route) 15.sp else 11.sp,
				color = Color.White
			)
			
			Text(
				text = "Cards",
				modifier = Modifier
					.padding(horizontal = 10.dp, vertical = 5.dp),
				fontWeight = if (currentScreen == Screen.AddCardScreen.route) FontWeight.Bold else FontWeight.Normal,
				fontSize = if (currentScreen == Screen.AddCardScreen.route) 15.sp else 11.sp,
				color = Color.White
			)
		}
	}
}

@Composable
fun StudyTopBar(navController: NavHostController, viewModel: CardViewModel) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(color = bottomTopAppBarColor)
			.padding(16.dp),
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically
	) {
		Image(painter = painterResource(R.drawable.close_icon),
			contentDescription = "close",
			modifier = Modifier.clickable {
				navController.navigate(Screen.CollectionsScreen.route)
			})
	}
}