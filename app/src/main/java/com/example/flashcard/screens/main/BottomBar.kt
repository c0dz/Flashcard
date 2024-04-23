package com.example.flashcard.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flashcard.R
import com.example.flashcard.Screen
import com.example.flashcard.data.BottomNavigationItemData
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.buttonColorGreen
import com.example.flashcard.viewModel.CardViewModel

val bottomNavItems = listOf(
	BottomNavigationItemData(
		title = "Collections",
		icon = R.drawable.collections_icon
	),
	BottomNavigationItemData(
		title = "Review",
		icon = R.drawable.review_icon
	),
	BottomNavigationItemData(
		title = "Progress",
		icon = R.drawable.progress_icon
	),
)

@Composable
fun BottomBarDisplay(navController: NavHostController, viewModel: CardViewModel) {
	val homeScreens = listOf(
		Screen.CollectionsScreen,
		Screen.ReviewScreen,
		Screen.ProgressScreen
	)
	
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry?.destination
	
	//Check for the current route and choose the Bottom Bar
	val displayHomeBottomBar = homeScreens.any { it.route == currentDestination?.route }
	val displayAddCollectionDetailBottomBar =
		Screen.AddCollectionDetailScreen.route == currentDestination?.route
	val displayAddCardBottomBar = Screen.AddCardScreen.route == currentDestination?.route
	val displayCardBottomBar =
		Screen.CardScreen.route == currentDestination?.route
	//val displayStudyAnswerBottomBar = Screen.StudyAnswerScreen.route == currentDestination?.route
	
	if (displayHomeBottomBar) {
		HomeNavigationBar(navController)
	} else if (displayAddCollectionDetailBottomBar) {
		AddCollectionBottomBar(navController, viewModel)
	} else if (displayAddCardBottomBar) {
		AddCardsBottomBar(navController, viewModel)
	} else if (displayCardBottomBar) {
		Spacer(modifier = Modifier.height(0.dp))
		//StudyQuestionBottomBar(navController, viewModel)
	}
}

@Composable
fun HomeNavigationBar(navController: NavHostController) {
	var selectedItem by rememberSaveable {
		mutableIntStateOf(0)
	}
	NavigationBar {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceAround,
			modifier = Modifier
				.background(bottomTopAppBarColor)
				.fillMaxWidth()
		) {
			bottomNavItems.forEachIndexed { index, item ->
				NavigationBarItem(
					selected = selectedItem == index,
					onClick = {
						selectedItem = index
						when (index) {
							0 -> navController.navigate(Screen.CollectionsScreen.route)
							1 -> navController.navigate(Screen.ReviewScreen.route)
							2 -> navController.navigate(Screen.ProgressScreen.route)
						}
					},
					icon = {
						Icon(
							imageVector = ImageVector.vectorResource(item.icon),
							contentDescription = item.title,
							tint = Color.White
						)
					},
					label = {
						Text(
							text = item.title,
							color = Color.White,
						)
					},
				)
			}
		}
	}
}

@Composable
fun AddCollectionBottomBar(navController: NavHostController, viewModel: CardViewModel) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(bottomTopAppBarColor),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Button(
			modifier = Modifier
				.fillMaxWidth(),
			onClick = {
				viewModel.insertCollectionToDB()
				navController.navigate(Screen.AddCardScreen.route)
			},
			colors = ButtonDefaults.buttonColors(
				buttonColorGreen,
				contentColor = Color.White
			),
			shape = RoundedCornerShape(20)
		) {
			Text(
				text = "Next",
				textAlign = TextAlign.Center,
				color = Color.White
			)
		}
	}
}

@Composable
fun AddCardsBottomBar(navController: NavHostController, viewModel: CardViewModel) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(bottomTopAppBarColor),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Button(
			modifier = Modifier
				.fillMaxWidth(),
			onClick = {
				viewModel.insertCardToDB()
				navController.navigate(Screen.CollectionsScreen.route)
			},
			colors = ButtonDefaults.buttonColors(
				buttonColorGreen,
				contentColor = Color.White
			),
			shape = RoundedCornerShape(20)
		) {
			Text(
				text = "Save Collection",
				textAlign = TextAlign.Center,
				color = Color.White
			)
		}
	}
}

//@Composable
//fun StudyQuestionBottomBar(navController: NavHostController, viewModel: CardViewModel) {
//	Row(
//		modifier = Modifier
//			.fillMaxWidth()
//			.background(bottomTopAppBarColor),
//		verticalAlignment = Alignment.CenterVertically,
//		horizontalArrangement = Arrangement.Center
//	) {
//		Button(
//			modifier = Modifier
//				.fillMaxWidth(),
//			onClick = {
//			},
//			colors = ButtonDefaults.buttonColors(
//				buttonColorGreen,
//				contentColor = Color.White
//			),
//			shape = RoundedCornerShape(20)
//		) {
//			Text(
//				text = "See Answer",
//				textAlign = TextAlign.Center,
//				color = Color.White
//			)
//		}
//	}
//}
//
//@Composable
//fun StudyAnswerBottomBar(
//	navController: NavHostController,
//	viewModel: CardViewModel
//) {
//	Row(
//		modifier = Modifier
//			.fillMaxWidth()
//			.background(bottomTopAppBarColor),
//		verticalAlignment = Alignment.CenterVertically,
//		horizontalArrangement = Arrangement.Center
//	) {
//		Button(
//			modifier = Modifier
//				.fillMaxWidth(),
//			onClick = {
//			},
//			colors = ButtonDefaults.buttonColors(
//				buttonColorGreen,
//				contentColor = Color.White
//			),
//			shape = RoundedCornerShape(20)
//		) {
//			Text(
//				text = "See Question",
//				textAlign = TextAlign.Center,
//				color = Color.White
//			)
//		}
//	}
//}