package com.example.flashcard.screens.collections

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.Screen
import com.example.flashcard.model.entities.CollectionEntity
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.collectionBackgroundEnd
import com.example.flashcard.ui.theme.collectionBackgroundStart
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
			Log.d("Progress", "Progress for col ${collection.id}: ${collection.progress} ")
			CollectionComponent(
				id = collection.id,
				title = collection.name,
				tag = collection.tags,
				navController = navController,
				viewModel = studyViewModel,
				progress = collection.progress
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
	viewModel: StudyViewModel,
	progress: Float
) {
	Row(
		modifier = Modifier
			.fillMaxSize()
			.padding(25.dp)
			.clip(shape = RoundedCornerShape(15.dp))
			.border(5.dp, homeCardBorderColor, shape = RoundedCornerShape(15.dp))
			.background(
				brush = Brush.linearGradient(
					colors = listOf(
						collectionBackgroundStart, // Start color
						collectionBackgroundEnd // End color
					)
				),
			)
			.padding(10.dp)
			.clickable {
				viewModel.setStudyCollection(id)
				viewModel.sessionInfo.collectionId = id
				viewModel.sessionInfo.startTime =
					System.currentTimeMillis() // set session start time
				navController.navigate(Screen.CardScreen.route)
			},
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceAround
	) {
		CircularProgressIndicator(
			progress = progress,
			strokeWidth = 3.dp,
			color = Color.Magenta,
			trackColor = Color.White
		)
		
		Column(
			modifier = Modifier
				.fillMaxSize(),
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
}

