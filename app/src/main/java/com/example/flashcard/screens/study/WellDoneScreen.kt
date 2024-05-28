package com.example.flashcard.screens.study

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.R
import com.example.flashcard.Screen
import com.example.flashcard.data.SessionItem
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.buttonColorGreen
import com.example.flashcard.ui.theme.greenTextColor
import com.example.flashcard.ui.theme.homeBackgroundColor
import com.example.flashcard.ui.theme.robotoFont
import com.example.flashcard.viewModel.StudyViewModel


@Composable
fun WellDoneScreen(navController: NavHostController, studyViewModel: StudyViewModel) {
	val sessionCards: Array<SessionItem> = arrayOf(
		SessionItem(
			title = "Duration",
			value = ((studyViewModel.sessionInfo.endTime - studyViewModel.sessionInfo.startTime) / 1000)
				.toString()
		),
		SessionItem(
			title = "Total Cards",
			value = studyViewModel.sessionInfo.cardsNumber.toString()
		),
		SessionItem(
			title = "Moved to Next Box",
			value = (studyViewModel.sessionInfo.cardsNumber - studyViewModel.sessionInfo.failedCards).toString()
		),
		SessionItem(
			title = "Moved to First Box",
			value = studyViewModel.sessionInfo.failedCards.toString()
		),
	)
	
	Column(
		modifier = Modifier
			.fillMaxSize(),
		verticalArrangement = Arrangement.SpaceBetween,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(10.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Image(
				painter = painterResource(R.drawable.fanfare__2_),
				contentDescription = "WellDonePage",
			)
			Text(
				text = "Well Done!",
				color = greenTextColor,
				fontFamily = robotoFont,
				fontWeight = FontWeight.Medium,
				fontSize = 40.sp
			)
		}
		LazyVerticalGrid(
			modifier = Modifier
				.fillMaxWidth()
				.background(bottomTopAppBarColor),
			columns = GridCells.Adaptive(minSize = 150.dp),
			verticalArrangement = Arrangement.Top,
			horizontalArrangement = Arrangement.SpaceAround,
		) {
			items(sessionCards) {
				Card(
					title = it.title,
					value = it.value
				)
			}
			item(span = { GridItemSpan(maxLineSpan) }) {
				TextButton(
					modifier = Modifier
						.background(buttonColorGreen)
						.clip(shape = RoundedCornerShape(20.dp)),
					onClick = {
						navController.navigate(Screen.CollectionsScreen.route)
						studyViewModel.addNewSession()
					}
				) {
					Text(
						text = "Back to Collections",
						color = Color.White
					)
				}
			}
		}
	}
}

@Composable
private fun Card(
	title: String,
	value: String,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.height(150.dp)
			.padding(10.dp)
			.clip(shape = RoundedCornerShape(20.dp))
			.background(homeBackgroundColor)
			.clickable { }
			.padding(5.dp),
		verticalArrangement = Arrangement.SpaceAround,
	) {
		Text(
			modifier = Modifier.fillMaxWidth(),
			text = title,
			color = Color.White,
			textAlign = TextAlign.Left,
			fontFamily = robotoFont,
			fontWeight = FontWeight.Bold,
			fontSize = 25.sp
		)
		Text(
			modifier = Modifier.fillMaxWidth(),
			text = value,
			color = Color.White,
			textAlign = TextAlign.Right,
			fontWeight = FontWeight.Bold,
			fontSize = 30.sp,
		)
	}
}