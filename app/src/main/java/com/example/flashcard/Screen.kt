package com.example.flashcard

sealed class Screen(val route: String) {
	// HOME SCREEN
	data object CollectionsScreen : Screen("collections_screen")
	data object ReviewScreen : Screen("review_screen")
	data object ProgressScreen : Screen("progress_screen")
	
	// ADD COLLECTION SCREEN
	data object AddCollectionDetailScreen : Screen("add_collection_detail_screen")
	data object AddCardScreen : Screen("add_card_screen")
	
	// STUDY SCREEN
	data object CardScreen : Screen("card_screen")
	
	// SETTINGS SCREEN
	data object SettingsScreen : Screen("settings_screen")
	data object PrivacyPolicyScreen : Screen("privacy_policy_screen")
	data object TermsOfUseScreen : Screen("terms_of_use_screen")
	data object ProfileScreen : Screen("profile_screen")
}