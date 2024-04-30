package com.example.flashcard.screens.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.Screen
import com.example.flashcard.model.entities.CollectionEntity
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.homeBackgroundColor
import com.example.flashcard.ui.theme.homeCardBorderColor
import com.example.flashcard.viewModel.StudyViewModel

@Composable
fun CollectionList(
	collections: List<CollectionEntity>,
	navController: NavHostController,
	studyViewModel: StudyViewModel
) {
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(10.dp),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	
	) {
		items(collections) { collection: CollectionEntity ->
			CollectionComponent(
				id = collection.id,
				title = collection.name,
				tag = collection.tags,
				navController = navController,
				viewModel = studyViewModel
			)
		}
	}
}

@Composable
fun CollectionComponent(
	id: Long,
	title: String,
	tag: String,
	navController: NavHostController,
	viewModel: StudyViewModel
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(25.dp)
			.clip(shape = RoundedCornerShape(15.dp))
			.border(5.dp, homeCardBorderColor, shape = RoundedCornerShape(15.dp))
			.background(homeBackgroundColor)
			.clickable {
				viewModel.setStudyCollection(id)
				navController.navigate(Screen.CardScreen.route)
			},
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.Start,
	) {
		Text(
			text = title,
			modifier = Modifier.padding(5.dp),
			color = Color.White,
			fontSize = 30.sp
		)
		Text(
			text = tag,
			modifier = Modifier.padding(5.dp),
			color = Color.White,
			fontSize = 15.sp
		)
	}
}