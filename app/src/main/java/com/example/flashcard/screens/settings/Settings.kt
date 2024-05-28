package com.example.flashcard.screens.settings

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.CollectionEntity
import com.example.flashcard.model.entities.SessionEntity
import com.example.flashcard.ui.theme.bottomTopAppBarColor
import com.example.flashcard.ui.theme.homeCardBorderColor
import com.example.flashcard.viewModel.StudyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

//private val setting_items: Array<SettingItem> = arrayOf(
//	SettingItem(
//		name = "Profile",
//		route = Screen.ProfileScreen.route,
//	),
//	SettingItem(
//		name = "Privacy Policy",
//		route = Screen.PrivacyPolicyScreen.route,
//	),
//	SettingItem(
//		name = "Terms Of Use",
//		route = Screen.TermsOfUseScreen.route,
//	),
//)

/**
 * A composable function that displays the settings screen with options to set a reminder,
 * export data, and clear data.
 *
 * This function uses LazyColumn to arrange setting items vertically and handles user interactions
 * with dialogs for confirming actions like setting a reminder, exporting data, and clearing data.
 *
 * @param navController The navigation controller responsible for managing navigation within the application.
 * @param studyViewModel The [StudyViewModel] instance used for data management.
 *
 * The function uses mutable state variables to manage dialog visibility and selected time.
 *
 * @sample
 * ```kotlin
 * val navController = rememberNavController()
 * val studyViewModel = StudyViewModel()
 * SettingsScreen(navController = navController, studyViewModel = studyViewModel)
 */
@Composable
fun SettingsScreen(
	navController: NavHostController,
	studyViewModel: StudyViewModel,
) {
	val openAlertDialog = remember { mutableStateOf(false) }
	val openClockDialog = remember { mutableStateOf(false) }
	val openExportDialog = remember { mutableStateOf(false) }
	val selectedTime = remember { mutableStateOf("") }
	val context = LocalContext.current
	val coroutineScope = rememberCoroutineScope()
	
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
		
		openClockDialog.value -> {
			showTimePickerDialog(context, onTimeSelected = { time ->
				selectedTime.value = time
				openClockDialog.value = false
				
				// set Alarm
				val (hour, minute) = time.split(":").map { it.toInt() }
				createAlarm(context, "flashcard reminder", hour, minute)
			}, openClockDialog)
		}
		
		openExportDialog.value -> {
			AlertDialog(
				onDismissRequest = { openExportDialog.value = false },
				title = {
					Text(text = "Export Data?")
				},
				confirmButton = {
					TextButton(
						onClick = {
							var fileName: String
							coroutineScope.launch {
								withContext(Dispatchers.IO) {
									fileName = exportDataToCSV(
										context,
										studyViewModel,
									)
								}
								
								Toast.makeText(context, "File Name: $fileName", Toast.LENGTH_SHORT)
									.show()
								Log.d("SettingsScreen", "Exported data.")
							}
							openExportDialog.value = false
							
						}
					) {
						Text(text = "Confirm", color = Color.Green)
					}
				},
				dismissButton = {
					TextButton(
						onClick = { openExportDialog.value = false }
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
//		items(setting_items) {
//			Row(
//				modifier = Modifier
//					.clickable {
//						navController.navigate(it.route)
//					}
//					.fillMaxWidth()
//					.clip(shape = RoundedCornerShape(5.dp))
//					.background(homeCardBorderColor)
//					.padding(bottom = 15.dp, top = 15.dp, start = 20.dp, end = 25.dp),
//				verticalAlignment = Alignment.CenterVertically,
//				horizontalArrangement = Arrangement.SpaceBetween
//			) {
//				Text(
//					text = it.name,
//					color = Color.White,
//					fontWeight = FontWeight.Bold,
//					fontStyle = FontStyle.Normal,
//					fontSize = 20.sp
//				)
//				Image(
//					painter = painterResource(id = R.drawable.settings_items_icon),
//					contentDescription = "settings_items_icon"
//				)
//			}
//		}
		
		// Create an Alarm
		item {
			Row(
				modifier = Modifier
					.clickable {
						openClockDialog.value = true
					}
					.fillMaxWidth()
					.clip(shape = RoundedCornerShape(5.dp))
					.background(homeCardBorderColor)
					.padding(bottom = 15.dp, top = 15.dp, start = 20.dp, end = 25.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Center
			) {
				Text(
					text = "Set Reminder",
					color = Color.White,
					fontWeight = FontWeight.Bold,
					fontStyle = FontStyle.Normal,
					fontSize = 20.sp
				)
			}
		}
		// Export
		item {
			Row(
				modifier = Modifier
					.clickable {
						openExportDialog.value = true
					}
					.fillMaxWidth()
					.clip(shape = RoundedCornerShape(5.dp))
					.background(homeCardBorderColor)
					.padding(bottom = 15.dp, top = 15.dp, start = 20.dp, end = 25.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Center
			) {
				Text(
					text = "Export",
					color = Color.White,
					fontWeight = FontWeight.Bold,
					fontStyle = FontStyle.Normal,
					fontSize = 20.sp
				)
			}
		}
		// Clear Data
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
				horizontalArrangement = Arrangement.Center
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

/**
 * Shows a time picker dialog to the user.
 *
 * @param context The context in which the dialog should be displayed.
 * @param onTimeSelected A callback function to be called when the user selects a time.
 * This function receives the selected time as a string in the format "HH:mm".
 * @param openClockDialog A mutable state to control the visibility of the clock dialog.
 *
 * This function initializes the time picker dialog with the current time as the default value.
 * When the user selects a time, it formats the selected time as "HH:mm" and invokes the
 * [onTimeSelected] callback with the formatted time. The dialog is dismissed after the selection.
 *
 * @sample
 * val openClockDialog = remember { mutableStateOf(false) }
 * showTimePickerDialog(
 *     context = LocalContext.current,
 *     onTimeSelected = { time -> /* Handle selected time */ },
 *     openClockDialog = openClockDialog
 * )
 */
fun showTimePickerDialog(
	context: Context,
	onTimeSelected: (String) -> Unit,
	openClockDialog: MutableState<Boolean>
) {
	val calendar = Calendar.getInstance()
	val hour = calendar.get(Calendar.HOUR_OF_DAY)
	val minute = calendar.get(Calendar.MINUTE)
	
	val timePickerDialog = TimePickerDialog(
		context,
		{ _, selectedHour, selectedMinute ->
			val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
			onTimeSelected(selectedTime)
		},
		hour,
		minute,
		true
	)
	
	timePickerDialog.setOnDismissListener {
		openClockDialog.value = false
	}
	timePickerDialog.show()
}

/**
 * Creates an alarm using the system's alarm clock application.
 *
 * @param context The context in which to start the alarm clock activity.
 * @param message The message to be displayed with the alarm.
 * @param hour The hour at which the alarm should be set.
 * @param minutes The minutes at which the alarm should be set.
 *
 * This function creates an alarm with the specified [message], [hour], and [minutes].
 * It uses an implicit intent with the action `AlarmClock.ACTION_SET_ALARM` to request
 * the system's alarm clock application to set the alarm. If there is an application
 * that can handle this intent, it starts the activity with the provided details.
 *
 * @sample
 * ```kotlin
 * createAlarm(
 *     context = applicationContext,
 *     message = "Wake up",
 *     hour = 7,
 *     minutes = 30
 * )
 * ```
 */
fun createAlarm(context: Context, message: String, hour: Int, minutes: Int) {
	val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
		putExtra(AlarmClock.EXTRA_MESSAGE, message)
		putExtra(AlarmClock.EXTRA_HOUR, hour)
		putExtra(AlarmClock.EXTRA_MINUTES, minutes)
	}
	if (intent.resolveActivity(context.packageManager) != null) {
		context.startActivity(intent)
	}
}


suspend fun exportDataToCSV(
	context: Context,
	studyViewModel: StudyViewModel,
): String {
	// Create a directory to store the CSV files
	val directory = File(context.filesDir, "CSV")
	if (!directory.exists()) {
		directory.mkdirs()
	}
	
	val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
	
	// Export data from CardEntity table
	val cardsFile = File(directory, "cards.csv")
	exportTableToCSV(
		studyViewModel.getAllCards(),
		cardsFile,
		"id,question,answer,collectionId,boxNumber,lastReviewDate,dueDate,isMastered\n"
	)
	
	// Export data from CollectionEntity table
	val collectionsFile = File(directory, "collections.csv")
	exportTableToCSV(
		studyViewModel.getAllCollections(),
		collectionsFile,
		"id,name,tags,description,progress\n"
	)
	
	// Export data from SessionEntity table
	val sessionsFile = File(directory, "sessions.csv")
	exportTableToCSV(
		studyViewModel.getAllSessions(),
		sessionsFile,
		"id,startTime,endTime,duration,cardsReviewed,cardsFailed,score\n"
	)
	
	// Zip the CSV files
	val zipFile = File(directory, "export_$timestamp.zip")
	zipFiles(zipFile, cardsFile, collectionsFile, sessionsFile)
	
	// Delete CSV files
	cardsFile.delete()
	collectionsFile.delete()
	sessionsFile.delete()
	
	return "TEST"
}

/**
 * Exports a list of data entities to a CSV file with specified headers.
 *
 * This function writes the provided data to a CSV file. It supports various entity types, including `CardEntity`,
 * `CollectionEntity`, and `SessionEntity`. The function writes the CSV header first and then iterates over the data list,
 * writing each item's attributes in a comma-separated format. This function runs within an IO-optimized coroutine context.
 *
 * @param T The type of the data entities being exported. It can be `CardEntity`, `CollectionEntity`, or `SessionEntity`.
 * @param data The list of data entities to be exported.
 * @param file The file where the CSV data will be written.
 * @param headers A string representing the CSV headers, with each header separated by a comma.
 * @throws IOException If an I/O error occurs during file writing.
 *
 */
private suspend fun <T> exportTableToCSV(data: List<T>, file: File, headers: String) {
	withContext(Dispatchers.IO) {
		val fileWriter = FileWriter(file)
		try {
			// Write CSV header
			fileWriter.append(headers)
			
			// Write data rows to CSV
			for (item in data) {
				when (item) {
					is CardEntity -> {
						fileWriter.append("${item.id},${item.question},${item.answer},${item.collectionId},${item.boxNumber},${item.lastReviewDate},${item.dueDate},${item.isMastered}\n")
					}
					
					is CollectionEntity -> {
						fileWriter.append("${item.id},${item.name},${item.tags},${item.description},${item.progress}\n")
					}
					
					is SessionEntity -> {
						fileWriter.append("${item.id},${item.startTime},${item.endTime},${item.duration},${item.cardsReviewed},${item.cardsFailed},${item.score}\n")
					}
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			fileWriter.flush()
			fileWriter.close()
		}
	}
}

/**
 * Creates a ZIP file containing the specified files.
 *
 * This function compresses the provided files into a single ZIP archive. It reads the contents of each file
 * and writes them to the output ZIP file using a buffer to optimize performance. If an I/O error occurs during
 * the process, the stack trace is printed to the standard error stream.
 *
 * @param zipFile The output ZIP file that will contain the compressed files.
 * @param files The files to be included in the ZIP file.
 *
 * @throws IOException If an I/O error occurs during file reading or writing.
 */
private fun zipFiles(zipFile: File, vararg files: File) {
	val buffer = ByteArray(1024)
	try {
		ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { zipOutputStream ->
			for (file in files) {
				FileInputStream(file).use { fileInputStream ->
					zipOutputStream.putNextEntry(ZipEntry(file.name))
					var length: Int
					while (fileInputStream.read(buffer).also { length = it } > 0) {
						zipOutputStream.write(buffer, 0, length)
					}
					zipOutputStream.closeEntry()
				}
			}
		}
	} catch (e: IOException) {
		e.printStackTrace()
	}
}