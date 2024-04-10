package com.example.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.flashcard.data.BottomNavigationItemData
import com.example.flashcard.ui.theme.bottomTopAppBarColor

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

@Preview
@Composable
fun BottomNavigationBar(navController: NavHostController) {
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
							0 -> navController.navigate(Screen.CollectionScreen.route)
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