package com.example.flashcard.screens.collections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flashcard.R
import com.example.flashcard.data.CardData
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.cardBackgroundColor
import com.example.flashcard.ui.theme.cardLabelColor
import com.example.flashcard.ui.theme.homeBackgroundColor
import com.example.flashcard.viewmodel.CardViewModel

/**
 * A composable function that provides a UI for adding multiple cards.
 *
 * This function displays a column layout with a list of cards and an option to add new cards. The cards are managed by the [CardViewModel].
 *
 * @param viewModel The [CardViewModel] instance containing the card data and logic.
 *
 * The screen consists of a column layout with:
 * - A [LazyRow] displaying a list of cards using [CardItem].
 * - An [AddCardItem] at the end of the row to add new cards.
 *
 * @sample
 * val cardViewModel = CardViewModel()
 * AddCards(viewModel = cardViewModel)
 */
@Composable
fun AddCards(
	viewModel: CardViewModel
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		
		LazyRow(
			modifier = Modifier
				.fillMaxSize()
				.padding(20.dp),
			horizontalArrangement = Arrangement.spacedBy(10.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			items(viewModel.cardList) { item ->
				CardItem(cardData = item, viewModel = viewModel)
			}
			item {
				AddCardItem {
					viewModel.addCard("", "")
				}
			}
		}
	}
}

/**
 * A composable function that displays a card with editable question and answer fields.
 *
 * This function renders a card item with fields to edit the question and answer. Changes to these fields are propagated to the [CardViewModel].
 *
 * @param cardData The data for the card, including the question and answer.
 * @param viewModel The [CardViewModel] instance that handles the logic for modifying the card's data.
 *
 * The card consists of:
 * - An image at the top.
 * - An [OutlinedTextField] for the question.
 * - A spacer.
 * - An [OutlinedTextField] for the answer.
 *
 * @sample
 * ```kotlin
 * val cardViewModel = CardViewModel()
 * val cardData = CardData(id = 1, question = "What is Kotlin?", answer = "A programming language")
 * CardItem(cardData = cardData, viewModel = cardViewModel)
 * ```
 */
@Composable
fun CardItem(
	cardData: CardData,
	viewModel: CardViewModel
) {
	var questionValue by remember {
		mutableStateOf(cardData.question)
	}
	var answerValue by remember {
		mutableStateOf(cardData.answer)
	}
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.width(300.dp)
			.clip(
				RoundedCornerShape(30.dp)
			)
			.background(cardBackgroundColor)
			.padding(20.dp),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(id = R.drawable.card_dots),
			contentDescription = "Card Dots"
		)
		// question
		OutlinedTextField(
			value = questionValue,
			onValueChange = {
				questionValue = it
				viewModel.modifyQuestion(questionValue, cardData.id)
			},
			label = {
				Text(
					"Question ${cardData.id}",
					color = cardLabelColor
				)
			},
			textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.SemiBold),
			modifier = Modifier
				.padding(20.dp)
				.fillMaxWidth()
		)
		Spacer(
			modifier = Modifier
				.fillMaxWidth()
				.height(10.dp)
		)
		// answer
		OutlinedTextField(
			value = answerValue,
			onValueChange = {
				answerValue = it
				viewModel.modifyAnswer(answerValue, cardData.id)
			},
			label = {
				Text(
					"Answer",
					color = cardLabelColor
				)
			},
			textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.SemiBold),
			modifier = Modifier
				.padding(20.dp)
				.fillMaxWidth()
		)
	}
}

/**
 * A composable function that displays a box for adding a new card.
 *
 * This function renders a clickable box with an icon representing the action to add a new card. When clicked, it triggers the provided [onAddCardClick] callback.
 *
 * @param onAddCardClick The callback to be invoked when the box is clicked.
 *
 * The box is defined within a [Box] composable and consists of:
 * - An image centered within the box, representing the action to add a new card.
 *
 * @sample
 * AddCardItem(onAddCardClick = { /* Perform action to add a new card */ })
 */
@Composable
fun AddCardItem(
	onAddCardClick: () -> Unit,
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.width(300.dp)
			.clip(
				RoundedCornerShape(30.dp)
			)
			.clickable(
				onClick = onAddCardClick
			)
			.background(homeBackgroundColor),
		contentAlignment = Alignment.Center
	) {
		Image(
			painter = painterResource(id = R.drawable.newcardicon),
			contentDescription = "Add New Card"
		)
	}
	
}