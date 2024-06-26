package com.example.flashcard.screens.collections

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.Screen
import com.example.flashcard.model.entities.CollectionEntity
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.collectionBackgroundEnd
import com.example.flashcard.ui.theme.collectionBackgroundStart
import com.example.flashcard.ui.theme.homeCardBorderColor
import com.example.flashcard.ui.theme.robotoFont
import com.example.flashcard.viewmodel.StudyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A composable function that displays a list of collections.
 *
 * This function creates a UI that shows a list of collections using a [LazyColumn]. Each collection is represented by a [CollectionComponent].
 * If the list of collections is empty, a message indicating no collections are available will be displayed.
 *
 * @param collections The list of [CollectionEntity] objects representing the collections.
 * @param navController The [NavHostController] used for navigation between screens.
 * @param studyViewModel The [StudyViewModel] instance containing the study data and logic.
 *
 * The screen consists of a column layout that:
 * - Displays each collection using the [CollectionComponent].
 *
 * @sample
 * val navController = rememberNavController()
 * val cardViewModel = CardViewModel()
 * val studyViewModel = StudyViewModel()
 * val collections = listOf(CollectionEntity(1, "Collection 1", "tag1", 0.5f))
 * CollectionList(navController = navController, cardViewModel = cardViewModel, studyViewModel = studyViewModel, collections = collections)
 */
@Composable
fun CollectionList(
	collections: List<CollectionEntity>,
	navController: NavHostController,
	studyViewModel: StudyViewModel
) {
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(bottomTopAppBarColor)
			.padding(10.dp),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	
	) {
		
		items(collections) { collection: CollectionEntity ->
			Log.d("Progress", "Progress for col ${collection.id}: ${collection.progress} ")
			CollectionComponent(
				id = collection.id,
				title = collection.name,
				tag = collection.tags,
				navController = navController,
				viewModel = studyViewModel,
				progress = collection.progress
			)
		}
	}
}

/**
 * A composable function that represents an individual collection item in the list.
 *
 * This function creates a UI that shows the details of a single collection, including its title, tag, and progress.
 * It provides interactions such as tapping to review the collection or long-pressing to delete it.
 *
 * @param id The ID of the collection.
 * @param title The title of the collection.
 * @param tag The tag associated with the collection.
 * @param navController The [NavHostController] used for navigation between screens.
 * @param viewModel The [StudyViewModel] instance containing the study data and logic.
 * @param progress The progress of the collection as a float value.
 *
 * The component consists of:
 * - A row layout that includes a circular progress indicator and a column with the collection's title and tag.
 * - Dialogs for confirming review or deletion actions.
 *
 * @sample
 * val navController = rememberNavController()
 * val studyViewModel = StudyViewModel()
 * CollectionComponent(id = 1, title = "Collection 1", tag = "tag1", navController = navController, viewModel = studyViewModel, progress = 0.5f)
 */
@Composable
fun CollectionComponent(
	id: Long,
	title: String,
	tag: String,
	navController: NavHostController,
	viewModel: StudyViewModel,
	progress: Float
) {
	val coroutineScope = rememberCoroutineScope()
	
	// Dialogs
	val openAlertDialog1 = remember { mutableStateOf(false) }
	val openAlertDialog2 = remember { mutableStateOf(false) }
	
	when {
		openAlertDialog1.value -> {
			AlertDialog(
				onDismissRequest = { openAlertDialog1.value = false },
				text = {
					Text(text = "You have mastered all the cards in this collection!")
				},
				confirmButton = {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.End
					) {
						TextButton(
							onClick = {
								// Review the collection
								openAlertDialog1.value = false
								coroutineScope.launch {
									withContext(Dispatchers.IO) {
										viewModel.setMasteredStudySession(id)
										viewModel.sessionInfo.collectionId = id
										viewModel.sessionInfo.startTime =
											System.currentTimeMillis()
									}
									navController.navigate(Screen.CardScreen.route)
								}
							}
						) {
							Text(text = "Review")
						}
						TextButton(
							onClick = {
								// Delete the collection
								coroutineScope.launch(Dispatchers.IO) {
									viewModel.deleteCollection(id)
								}
								openAlertDialog1.value = false
							}
						) {
							Text(text = "Delete Collection", color = Color.Red)
						}
					}
				},
			)
		}
		
		openAlertDialog2.value -> {
			AlertDialog(
				onDismissRequest = { openAlertDialog2.value = false },
				title = { Text(text = "Delete Collection") },
				text = { Text("Are you sure you want to delete this collection? This action cannot be undone.") },
				confirmButton = {
					TextButton(
						onClick = {
							openAlertDialog2.value = false
							coroutineScope.launch(Dispatchers.IO) {
								viewModel.deleteCollection(id)
							}
						}
					) {
						Text("Delete")
					}
				},
				dismissButton = {
					TextButton(
						onClick = { openAlertDialog2.value = false }
					) {
						Text("Cancel")
					}
				}
			)
		}
	}
	
	Row(
		modifier = Modifier
			.fillMaxSize()
			.padding(25.dp)
			.clip(shape = RoundedCornerShape(15.dp))
			.border(5.dp, homeCardBorderColor, shape = RoundedCornerShape(15.dp))
			.background(
				brush = Brush.linearGradient(
					colors = listOf(
						collectionBackgroundStart, // Start color
						collectionBackgroundEnd // End color
					)
				),
			)
			.padding(10.dp)
			.pointerInput(Unit) {
				detectTapGestures(
					onLongPress = {
						openAlertDialog2.value = true
					},
					onTap = {
						coroutineScope.launch {
							val fetchedProgress =
								withContext(Dispatchers.IO) { viewModel.getProgress(id) }
							if (fetchedProgress == 1f) {
								openAlertDialog1.value = true
							} else {
								withContext(Dispatchers.IO) {
									viewModel.setStudySession(id)
									viewModel.sessionInfo.collectionId = id
									viewModel.sessionInfo.startTime =
										System.currentTimeMillis()
								}
								navController.navigate(Screen.CardScreen.route)
							}
						}
					}
				)
			},
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceAround
	) {
		CircularProgressIndicator(
			progress = progress,
			strokeWidth = 3.dp,
			color = Color.Magenta,
			trackColor = Color.White
		)
		
		Column(
			modifier = Modifier
				.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.Start,
		) {
			Text(
				text = title,
				modifier = Modifier.padding(5.dp),
				color = Color.White,
				fontSize = 30.sp,
				fontFamily = robotoFont,
				fontWeight = FontWeight.Bold
			)
			Box(
				modifier = Modifier
					.clip(shape = RoundedCornerShape(10.dp))
					.background(Color(0xFF846BCD)),
			) {
				Text(
					modifier = Modifier
						.padding(7.dp),
					text = tag,
					color = Color.White,
					fontSize = 15.sp,
					fontFamily = robotoFont,
					fontWeight = FontWeight.Normal
				)
			}
		}
	}
}

