package com.example.flashcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcard.ui.theme.bottomTopAppBarColor

@Preview
@Composable
fun CollectionTopBar() {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(color = bottomTopAppBarColor)
			.padding(16.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		Image(painter = painterResource(R.drawable.setting_icon),
			contentDescription = "settings",
			modifier = Modifier.clickable { })
		Text(
			text = "Collections",
			color = Color.White,
			fontSize = 18.sp,
			fontStyle = FontStyle.Normal,
			fontWeight = FontWeight.Bold
		)
		Image(
			painter = painterResource(R.drawable.add_collection_icon),
			contentDescription = "add_collection",
			modifier = Modifier.clickable { }
		)
		
	}
}