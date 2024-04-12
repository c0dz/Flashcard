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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcard.ui.theme.bottomTopAppBarColor

@Preview
@Composable
fun AddCollection() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(1.dp),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		var collectionName by remember { mutableStateOf("") }
		var tags by remember { mutableStateOf("") }
		var description by remember { mutableStateOf("") }
		
		OutlinedTextField(
			value = collectionName,
			onValueChange = { collectionName = it },
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
			value = tags,
			onValueChange = { tags = it },
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
			value = description,
			onValueChange = { description = it },
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