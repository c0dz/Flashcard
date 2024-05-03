package com.example.flashcard.screens.settings

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.R
import com.example.flashcard.Screen
import com.example.flashcard.data.SettingItem
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.homeCardBorderColor
import com.example.flashcard.viewModel.StudyViewModel


private val setting_items: Array<SettingItem> = arrayOf(
	SettingItem(
		name = "Profile",
		route = Screen.ProfileScreen.route,
	),
	SettingItem(
		name = "Privacy Policy",
		route = Screen.PrivacyPolicyScreen.route,
	),
	SettingItem(
		name = "Terms Of Use",
		route = Screen.TermsOfUseScreen.route,
	),
)


@Composable
fun SettingsScreen(navController: NavHostController, studyViewModel: StudyViewModel) {
	val openAlertDialog = remember { mutableStateOf(false) }
	when {
		openAlertDialog.value -> {
			AlertDialog(
				onDismissRequest = { openAlertDialog.value = false },
				title = {
					Text(text = "Clear Data")
				},
				text = {
					Text(text = "Are you sure you want to clear all data? This action cannot be undone.")
				},
				confirmButton = {
					TextButton(
						onClick = {
							studyViewModel.clearDatabase()
							openAlertDialog.value = false
							Log.d("SettingsScreen", "Cleared Database.")
						}
					) {
						Text(text = "Clear Data", color = Color.Red)
					}
				},
				dismissButton = {
					TextButton(
						onClick = { openAlertDialog.value = false }
					) {
						Text(text = "Cancel")
					}
				}
			)
		}
	}
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(15.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		items(setting_items) {
			Row(
				modifier = Modifier
					.clickable {
						navController.navigate(it.route)
					}
					.fillMaxWidth()
					.clip(shape = RoundedCornerShape(5.dp))
					.background(homeCardBorderColor)
					.padding(bottom = 15.dp, top = 15.dp, start = 20.dp, end = 25.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = it.name,
					color = Color.White,
					fontWeight = FontWeight.Bold,
					fontStyle = FontStyle.Normal,
					fontSize = 20.sp
				)
				Image(
					painter = painterResource(id = R.drawable.settings_items_icon),
					contentDescription = "settings_items_icon"
				)
			}
		}
		item {
			Row(
				modifier = Modifier
					.clickable {
						openAlertDialog.value = true
					}
					.fillMaxWidth()
					.clip(shape = RoundedCornerShape(5.dp))
					.background(homeCardBorderColor)
					.padding(bottom = 15.dp, top = 15.dp, start = 20.dp, end = 25.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "Clear Data",
					color = Color.Red,
					fontWeight = FontWeight.Bold,
					fontStyle = FontStyle.Normal,
					fontSize = 20.sp
				)
			}
		}
	}
}