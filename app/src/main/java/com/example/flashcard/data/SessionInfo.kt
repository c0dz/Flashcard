package com.example.flashcard.data

data class SessionInfo(
	var startTime: Long,
	var endTime: Long,
	var cardsNumber: Long,
	var failedCards: Long,
) {
	companion object {
		fun setToDefault(sessionInfo: SessionInfo) {
			sessionInfo.startTime = 0
			sessionInfo.endTime = 0
			sessionInfo.cardsNumber = 0
			sessionInfo.failedCards = 0
		}
	}
}