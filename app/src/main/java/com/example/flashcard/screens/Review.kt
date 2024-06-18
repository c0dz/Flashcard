package com.example.flashcard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.Screen
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.buttonColorGreen
import com.example.flashcard.ui.theme.textColorPurple
import com.example.flashcard.viewmodel.StudyViewModel

/**
 * A composable function that displays the review screen for the user's flashcards.
 *
 * This function creates a UI with a title and a button to start reviewing all flashcards.
 * When the button is clicked, it initializes the study session and navigates to the card review screen.
 *
 * @param navController The [NavHostController] used for navigation between screens.
 * @param studyViewModel The [StudyViewModel] instance containing the study data and logic.
 *
 * The screen consists of a column layout that contains:
 * - A title text prompting the user to review their flashcards.
 * - A spacer for visual separation.
 * - A button that starts the review session by setting all cards and recording the session start time,
 *   then navigates to the card review screen.
 *
 * @sample
 * val navController = rememberNavController()
 * val studyViewModel = StudyViewModel()
 * ReviewScreen(navController = navController, studyViewModel = studyViewModel)
 */
@Composable
fun ReviewScreen(navController: NavHostController, studyViewModel: StudyViewModel) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(10.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	
	) {
		Text(
			text = "Review all your flashcards",
			fontWeight = FontWeight.Normal,
			fontStyle = FontStyle.Normal,
			color = textColorPurple,
			fontSize = 18.sp,
			textAlign = TextAlign.Center
		)
		Spacer(
			modifier = Modifier
				.fillMaxWidth()
				.height(65.dp)
		)
		Button(
			onClick = {
				studyViewModel.setAllCards()
				studyViewModel.sessionInfo.startTime =
					System.currentTimeMillis() // set session start time
				navController.navigate(Screen.CardScreen.route)
			},
			colors = ButtonDefaults.buttonColors(
				buttonColorGreen,
				contentColor = Color.White
			),
			shape = RoundedCornerShape(0)
		) {
			Text(
				text = "Start",
				textAlign = TextAlign.Center,
				color = Color.White,
				fontSize = 20.sp
			)
		}
	}
}