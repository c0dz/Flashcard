package com.example.flashcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.flashcard.ui.theme.homeBackgroundColor

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		
		super.onCreate(savedInstanceState)
		installSplashScreen()
		enableEdgeToEdge()
		setContent {
			Box(Modifier.safeDrawingPadding()) {
				HomeScreen()
			}
		}
	}
}

@Preview
@Composable
fun HomeScreen() {
	val navController = rememberNavController()
	Scaffold(
		bottomBar = { BottomNavigationBar(navController) },
		topBar = { CollectionTopBar() },
	) { paddingValue ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValue)
				.background(homeBackgroundColor)
		) {
			NavigationGraph(navController = navController)
		}
	}
}

