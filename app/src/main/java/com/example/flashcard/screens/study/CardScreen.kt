package com.example.flashcard.screens.study

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.buttonColorGreen
import com.example.flashcard.ui.theme.homeCardBorderColor
import com.example.flashcard.viewModel.StudyViewModel


private enum class CardState(val stateValue: String) {
	Question("SEE ANSWER"),
	Answer("SEE QUESTION")
}

@Composable
fun CardScreen(viewModel: StudyViewModel) {
	val QuestionAnswers = viewModel.getCurrentStudyCards().collectAsState(
		initial = emptyList()
	)
	val cardState by remember {
		mutableStateOf(CardState.Question)
	}
	Scaffold(
		bottomBar = { CardButtonBar(cardState) },
		modifier = Modifier
			.fillMaxSize()
			.clip(shape = RoundedCornerShape(15.dp))
			.border(5.dp, color = homeCardBorderColor, shape = RoundedCornerShape(15.dp)),
	) { paddingValue ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(bottomTopAppBarColor)
				.padding(bottom = paddingValue.calculateBottomPadding())
		) { }
	}
}

@Composable
private fun CardButtonBar(cardState: CardState) {
	Divider(
		color = homeCardBorderColor,
		modifier = Modifier
			.height(3.dp)
			.fillMaxWidth()
	)
	Row(
		modifier = Modifier
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