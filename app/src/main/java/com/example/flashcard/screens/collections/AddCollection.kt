package com.example.flashcard.screens.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.viewmodel.CardViewModel

/**
 * A composable function that provides a UI for adding a new collection.
 *
 * This function creates a UI for entering the details of a new collection, including the collection name, tags, and description. The user inputs are managed by the [CardViewModel].
 *
 * @param viewModel The [CardViewModel] instance containing the collection data and logic.
 *
 * The screen consists of a column layout that includes:
 * - An [OutlinedTextField] for entering the collection name.
 * - An [OutlinedTextField] for entering tags associated with the collection.
 * - An [OutlinedTextField] for entering a description of the collection.
 *
 * @sample
 * val cardViewModel = CardViewModel()
 * AddCollection(viewModel = cardViewModel)
 */
@Composable
fun AddCollection(viewModel: CardViewModel) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(1.dp),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		
		OutlinedTextField(
			value = viewModel.getCollectionName(),
			onValueChange = {
				viewModel.setCollectionName(it)
			},
			label = {
				Text(
					"Collection Name",
					color = Color.White,
					fontWeight = FontWeight.W300
				)
			},
			maxLines = 2,
			textStyle = TextStyle(
				color = Color.White,
				fontWeight = FontWeight.SemiBold
			),
			modifier = Modifier
				.padding(20.dp)
				.fillMaxWidth()
		)
		
		OutlinedTextField(
			value = viewModel.getTags(),
			onValueChange = {
				viewModel.setTags(it)
			},
			label = {
				Text(
					"Tags",
					color = Color.White,
					fontWeight = FontWeight.W300
				)
			},
			maxLines = 2,
			textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.SemiBold),
			modifier = Modifier
				.padding(20.dp)
				.fillMaxWidth()
		)
		
		OutlinedTextField(
			value = viewModel.getDescription(),
			onValueChange = {
				viewModel.setDescription(it)
			},
			label = {
				Text(
					"Description",
					color = Color.White,
					fontWeight = FontWeight.W300
				)
			},
			maxLines = 2,
			textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.SemiBold),
			modifier = Modifier
				.padding(20.dp)
				.fillMaxWidth()
		)
		
	}
}