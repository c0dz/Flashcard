package com.example.flashcard.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcard.data.ProgressItem
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.homeBackgroundColor
import com.example.flashcard.viewModel.StudyViewModel


@Composable
fun ProgressScreen(studyViewModel: StudyViewModel) {
	val (hours, minutes, seconds) = studyViewModel.getTimeSpent()
	val progressCards: Array<ProgressItem> = arrayOf(
		ProgressItem(
			title = "Total Cards",
			value = studyViewModel.getTotalCardCount().toString()
		),
		ProgressItem(
			title = "Mastered Cards",
			value = studyViewModel.getMasteredCardsCount().toString()
		),
		ProgressItem(
			title = "Success Rate",
			value = "${studyViewModel.getSuccessRate()}%"
		),
		ProgressItem(
			title = "Streak",
			value = "4"
		),
		ProgressItem(
			title = "Time Spent",
			value = "${hours}h ${minutes}m ${seconds}s"
		)
	)
	
	
	LazyVerticalGrid(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor),
		columns = GridCells.Adaptive(minSize = 150.dp),
		verticalArrangement = Arrangement.Top,
		horizontalArrangement = Arrangement.SpaceAround,
	) {
		items(progressCards.dropLast(1)) {
			Card(
				title = it.title,
				value = it.value
			)
		}
		item(span = { GridItemSpan(maxLineSpan) }) { // last item, span multiple columns
			Card(
				title = progressCards.last().title,
				value = progressCards.last().value
			)
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
			.clip(shape = RoundedCornerShape(topEnd = 30.dp, bottomStart = 30.dp))
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
			fontStyle = FontStyle.Italic,
			fontSize = 20.sp
		)
		Text(
			modifier = Modifier.fillMaxWidth(),
			text = value,
			color = Color.White,
			textAlign = TextAlign.Right,
			fontWeight = FontWeight.Bold,
			fontSize = 50.sp,
		)
	}
}