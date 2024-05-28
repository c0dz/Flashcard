package com.example.flashcard.screens.collections

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.R
import com.example.flashcard.Screen
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.buttonColorGreen
import com.example.flashcard.ui.theme.robotoFont
import com.example.flashcard.ui.theme.textColorPurple
import com.example.flashcard.viewModel.CardViewModel

/**
 * A composable function that displays a message indicating no collections were found and provides an option to create a new collection.
 *
 * This function creates a UI that shows an image, a message, and a button. When the button is clicked,
 * it navigates to the screen where the user can add a new collection.
 *
 * @param navController The [NavHostController] used for navigation between screens.
 * @param viewModel The [CardViewModel] instance used for handling card-related data and logic.
 *
 * The screen consists of a column layout that:
 * - Displays an image indicating no collections are available.
 * - Shows a bold text message stating "No collection created".
 * - Provides additional instructions for creating a collection.
 * - Contains a button that, when clicked, clears temporary data in the [CardViewModel] and navigates to the add collection detail screen.
 *
 * @sample
 * val navController = rememberNavController()
 * val cardViewModel = CardViewModel()
 * NoCollectionsFound(navController = navController, viewModel = cardViewModel)
 */
@Composable
fun NoCollectionsFound(navController: NavHostController, viewModel: CardViewModel) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(10.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	
	) {
		Image(
			painter = painterResource(R.drawable.folder_icon),
			contentDescription = "No Collection"
		)
		Text(
			text = "No collection created",
			fontFamily = robotoFont,
			fontWeight = FontWeight.Bold,
			color = Color.White,
			fontSize = 22.sp
		)
		Spacer(
			modifier = Modifier
				.fillMaxWidth()
				.height(15.dp)
		)
		Text(
			text = "Build your first collection by adding questions and answers",
			fontFamily = robotoFont,
			fontWeight = FontWeight.Normal,
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
				viewModel.clearTempData()
				navController.navigate(Screen.AddCollectionDetailScreen.route)
			},
			colors = ButtonDefaults.buttonColors(
				buttonColorGreen,
				contentColor = Color.White
			),
			shape = RoundedCornerShape(0)
		) {
			Text(
				text = "Create a Collection",
				textAlign = TextAlign.Center,
				color = Color.White,
				fontSize = 20.sp
			)
		}
	}
}