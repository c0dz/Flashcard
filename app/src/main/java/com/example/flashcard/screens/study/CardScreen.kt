package com.example.flashcard.screens.study

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.R
import com.example.flashcard.Screen
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.buttonColorGreen
import com.example.flashcard.ui.theme.homeCardBorderColor
import com.example.flashcard.viewModel.StudyViewModel


enum class CardState(val stateValue: String) {
	Question("SEE ANSWER"),
	Answer("SEE QUESTION")
}


@Composable
fun CardScreen(
	viewModel: StudyViewModel,
	navController: NavHostController
) {
	// set the initial state of the card
	val cardsList = remember {
		mutableStateOf(viewModel.cards)
	}
	
	val (cardState, updateCardState) = remember { mutableStateOf(CardState.Question) }
	
	Surface(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(bottom = 20.dp, start = 10.dp, end = 10.dp, top = 5.dp)
	) {
		Scaffold(
			bottomBar = { CardButtonBar(updateCardState, cardState) },
			modifier = Modifier
				.background(bottomTopAppBarColor)
				.clip(RoundedCornerShape(15.dp))
				.border(5.dp, color = homeCardBorderColor, shape = RoundedCornerShape(15.dp))
				.fillMaxSize()
				.background(color = Color.White),
			containerColor = bottomTopAppBarColor,
		) { paddingValue ->
			if (cardsList.value.isNotEmpty()) {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.background(Color.Transparent)
						.padding(bottom = paddingValue.calculateBottomPadding())
						.padding(35.dp)
				) {
					if (cardState === CardState.Question) {
						Column(
							modifier = Modifier
								.fillMaxSize(),
							verticalArrangement = Arrangement.SpaceBetween,
							horizontalAlignment = Alignment.CenterHorizontally
						) {
							Text(text = cardsList.value.first().question, color = Color.White)
						}
					} else if (cardState === CardState.Answer) {
						Column(
							modifier = Modifier
								.fillMaxSize(),
							verticalArrangement = Arrangement.SpaceBetween,
							horizontalAlignment = Alignment.CenterHorizontally
						) {
							Text(text = cardsList.value.first().answer, color = Color.White)
							MarkCard(
								cardsList = cardsList,
								viewModel = viewModel,
								updateCardState = updateCardState,
								navController = navController
							)
						}
						
					}
					
				}
			} else {
				Text(text = "Empty")
				Log.d("CardScreen", "Couldn't load cards from database")
			}
			
		}
	}
	
}

@Composable
private fun CardButtonBar(updateCardState: (CardState) -> Unit, cardState: CardState) {
	Divider(
		color = homeCardBorderColor,
		modifier = Modifier
			.height(3.dp)
			.fillMaxWidth(),
	)
	Row(
		modifier = Modifier
			.clickable {
				if (cardState === CardState.Question) {
					updateCardState(CardState.Answer)
				} else if (cardState === CardState.Answer) {
					updateCardState(CardState.Question)
				}
			}
			.fillMaxWidth()
			.padding(20.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Text(
			text = cardState.stateValue,
			color = buttonColorGreen,
			fontWeight = FontWeight.Bold,
			fontSize = 15.sp
		)
	}
}


@Composable
private fun MarkCard(
	cardsList: MutableState<List<CardEntity>>,
	viewModel: StudyViewModel,
	updateCardState: (CardState) -> Unit,
	navController: NavHostController
) {
	Row(
		modifier = Modifier
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceAround
	) {
		// Easy
		Column(
			modifier = Modifier
				.clickable {
					viewModel.sessionInfo.cardsNumber++
					viewModel.moveToNextBox(
						card = cardsList.value.first()
					)
					if (cardsList.value.size == 1) { // Last Card
						viewModel.sessionInfo.endTime = System.currentTimeMillis()
						navController.navigate(Screen.CollectionsScreen.route)
						viewModel.addNewSession()
					}
					viewModel.removeCurrentCardFromList(
						cardsList,
						updateCardState,
						CardState.Question
					)
				},
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Image(
				painter = painterResource(id = R.drawable.easy_card),
				contentDescription = "Easy Card"
			)
			Text(
				text = "Easy",
				color = Color.White,
				fontWeight = FontWeight.Bold,
				fontSize = 35.sp,
				fontFamily = FontFamily.SansSerif
			)
		}
		
		// Hard
		Column(
			modifier = Modifier
				.clickable {
					viewModel.sessionInfo.cardsNumber++
					viewModel.sessionInfo.failedCards++
					viewModel.moveToFirstBox(
						card = cardsList.value.first()
					)
					if (cardsList.value.size == 1) { // Last Card
						viewModel.sessionInfo.endTime = System.currentTimeMillis()
						navController.navigate(Screen.CollectionsScreen.route)
						viewModel.addNewSession()
					}
					viewModel.removeCurrentCardFromList(
						cardsList,
						updateCardState,
						CardState.Question
					)
				},
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Image(
				painter = painterResource(id = R.drawable.hard_card),
				contentDescription = "Hard Card"
			)
			Text(
				text = "Hard",
				color = Color.White,
				fontWeight = FontWeight.Bold,
				fontSize = 35.sp,
				fontFamily = FontFamily.SansSerif
			)
		}
		
		
	}
}
