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
import com.example.flashcard.viewmodel.CardViewModel

/**
 * A composable function that displays the top bar based on the current screen.
 *
 * This function dynamically chooses and displays the appropriate top bar based on the current screen route.
 * It checks the current destination and selects the corresponding top bar composable function to display.
 *
 * @param navController The navigation controller responsible for managing navigation within the application.
 * @param viewModel The [CardViewModel] instance used for data management.
 *
 * The function distinguishes between different screens and displays:
 * - HomeTopBar for the Collections screen.
 * - AddCollectionTopBar for the AddCardScreen and AddCollectionDetailScreen.
 * - StudyTopBar for the CardScreen.
 * - SettingsTopBar for the SettingsScreen.
 * - ProgressTopBar for the ProgressScreen.
 * - ReviewTopBar for the ReviewScreen.
 *
 * @sample
 * val navController = rememberNavController()
 * val viewModel = CardViewModel()
 * TopBarDisplay(navController = navController, viewModel = viewModel)
 */
@Composable
fun TopBarDisplay(navController: NavHostController, viewModel: CardViewModel) {
	val addCollectionScreens = listOf(
		Screen.AddCardScreen,
		Screen.AddCollectionDetailScreen
	)
	
	
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry?.destination
	
	//Check for the current route and choose the Top Bar
	val displayHomeTopBar = Screen.CollectionsScreen.route == currentDestination?.route
	val displayAddCollectionTopBar =
		addCollectionScreens.any { it.route == currentDestination?.route }
	val displayCardTopBar = Screen.CardScreen.route == currentDestination?.route
	val displaySettingsTopBar = Screen.SettingsScreen.route == currentDestination?.route
	val displayProgressTopBar = Screen.ProgressScreen.route == currentDestination?.route
	val displayReviewTopBar = Screen.ReviewScreen.route == currentDestination?.route
	
	if (displayHomeTopBar) {
		HomeTopBar(navController, viewModel)
	} else if (displayAddCollectionTopBar) {
		AddCollectionTopBar(navController)
	} else if (displayCardTopBar) {
		StudyTopBar(navController, viewModel)
	} else if (displaySettingsTopBar) {
		SettingsTopBar(navController, viewModel)
	} else if (displayProgressTopBar) {
		ProgressTopBar()
	} else if (displayReviewTopBar) {
		ReviewTopBar()
	}
}

/**
 * HomeTopBar is a composable function that creates the top bar for the Home screen.
 * It contains a settings icon, a title, and an add collection icon.
 *
 * @param navController The NavHostController used for navigation between screens.
 * @param viewModel The CardViewModel instance used to handle data-related actions.
 */
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
			modifier = Modifier.clickable {
				navController.navigate(Screen.SettingsScreen.route)
			})
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

/**
 * AddCollectionTopBar is a composable function that creates the top bar for the Add Collection screen.
 * It contains a close icon to navigate back to the Collections screen and tabs for navigating
 * between the details and cards sections of a new collection.
 *
 * @param navController The NavHostController used for navigation between screens.
 */
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

/**
 * StudyTopBar is a composable function that creates the top bar for the Study screen.
 * It contains a close icon to navigate back to the Collections screen.
 *
 * @param navController The NavHostController used for navigation between screens.
 * @param viewModel The CardViewModel instance used to handle data-related actions.
 */
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

/**
 * SettingsTopBar is a composable function that creates the top bar for the Settings screen.
 * It contains a close icon to navigate back to the Collections screen and displays the title "Settings".
 *
 * @param navController The NavHostController used for navigation between screens.
 * @param viewModel The CardViewModel instance used to handle data-related actions.
 */
@Composable
fun SettingsTopBar(navController: NavHostController, viewModel: CardViewModel) {
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
			text = "Settings",
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
}

/**
 * ProgressTopBar is a composable function that creates the top bar for the Progress screen.
 * It displays the title "Progress" at the center of the screen.
 */
@Composable
fun ProgressTopBar() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		horizontalArrangement = Arrangement.Center,
	) {
		Text(
			text = "Progress",
			textAlign = TextAlign.Center,
			color = Color.White,
			fontSize = 18.sp,
			fontStyle = FontStyle.Normal,
			fontWeight = FontWeight.Bold,
			modifier = Modifier
				.fillMaxWidth(1f)
		)
	}
}

/**
 * ReviewTopBar is a composable function that creates the top bar for the Review screen.
 * It displays the title "Review" at the center of the screen.
 */
@Composable
fun ReviewTopBar() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		horizontalArrangement = Arrangement.Center,
	) {
		Text(
			text = "Review",
			textAlign = TextAlign.Center,
			color = Color.White,
			fontSize = 18.sp,
			fontStyle = FontStyle.Normal,
			fontWeight = FontWeight.Bold,
			modifier = Modifier
				.fillMaxWidth(1f)
		)
	}
}
