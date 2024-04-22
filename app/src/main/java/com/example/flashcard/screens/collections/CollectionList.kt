package com.example.flashcard.screens.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcard.model.entities.CollectionEntity
import com.example.flashcard.ui.theme.bottomTopAppBarColor

@Composable
fun CollectionList(collections: List<CollectionEntity>) {
	println("CollectionList: $collections")
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(10.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	
	) {
		items(collections) { collection: CollectionEntity ->
			CollectionComponent(title = collection.name, tag = collection.tags)
		}
	}
}

@Composable
fun CollectionComponent(
	title: String,
	tag: String
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(10.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.Start
	) {
		Text(
			text = title,
			modifier = Modifier.padding(5.dp),
			color = Color.White,
			fontSize = 22.sp
		)
		Text(
			text = tag,
			modifier = Modifier.padding(5.dp),
			color = Color.White,
			fontSize = 15.sp
		)
	}
}