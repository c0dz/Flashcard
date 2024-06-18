package com.example.flashcard.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.flashcard.data.SessionInfo
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.dao.SessionDao
import com.example.flashcard.model.entities.CardEntity
import com.example.flashcard.model.entities.SessionEntity
import com.example.flashcard.model.entities.calculateDueDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.check


@OptIn(ExperimentalCoroutinesApi::class)
class StudyViewModelTest {
	private lateinit var viewModel: StudyViewModel
	private lateinit var mockCardDao: CardDao
	private lateinit var mockCollectionDao: CollectionDao
	private lateinit var mockSessionDao: SessionDao
	
	private val testDispatcher = UnconfinedTestDispatcher()
	private val testScope = TestScope(testDispatcher)
	
	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()
	
	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		mockCardDao = Mockito.mock(CardDao::class.java)
		mockCollectionDao = Mockito.mock(CollectionDao::class.java)
		mockSessionDao = Mockito.mock(SessionDao::class.java)
		viewModel = StudyViewModel(mockCardDao, mockCollectionDao, mockSessionDao)
	}
	
	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}
	
	@Test
	fun `moveToNextBox updates card correctly`() = testScope.runTest {
		val card = CardEntity(1, "Q1", "A1", 1, 0, 0, 0, 0)
		
		viewModel.moveToNextBox(card)
		
		verify(mockCardDao).upsertCard(check {
			Assertions.assertEquals(card.id, it.id)
			Assertions.assertEquals(card.question, it.question)
			Assertions.assertEquals(card.answer, it.answer)
			Assertions.assertEquals(card.collectionId, it.collectionId)
			Assertions.assertEquals(1, it.boxNumber)
			Assertions.assertEquals(0, it.isMastered)
		})
	}
	
	@Test
	fun `getProgress returns correct progress`() = testScope.runTest {
		val collectionId = 1L
		`when`(mockCardDao.getCollectionCardsCount(collectionId)).thenReturn(10)
		`when`(mockCardDao.getCollectionMasteredCardsCount(collectionId)).thenReturn(5)
		
		val progress = viewModel.getProgress(collectionId)
		
		Assertions.assertEquals(0.5f, progress)
	}
	
	@Test
	fun `getStreak calculates streak correctly`() = testScope.runTest {
		val sessions = listOf(
			SessionEntity(
				startTime = 1622505600000,
				endTime = 1622509200000,
				duration = 3600000,
				cardsReviewed = 10,
				cardsFailed = 2,
				score = 80
			),
			SessionEntity(
				startTime = 1622592000000,
				endTime = 1622595600000,
				duration = 3600000,
				cardsReviewed = 10,
				cardsFailed = 1,
				score = 90
			),
			SessionEntity(
				startTime = 1622678400000,
				endTime = 1622682000000,
				duration = 3600000,
				cardsReviewed = 10,
				cardsFailed = 0,
				score = 100
			)
		)
		`when`(mockSessionDao.getAllSessions()).thenReturn(sessions)
		
		val streak = viewModel.getStreak()
		
		Assertions.assertEquals(3, streak)
	}
	
	
	@Test
	fun `addNewSession calculates and inserts new session correctly`() = testScope.runTest {
		val startTime = 1622505600000
		val endTime = 1622509200000
		val cardsReviewed = 10L
		val cardsFailed = 2L
		val expectedScore = ((1 - (cardsFailed.toDouble() / cardsReviewed)) * 100).toInt()
		
		viewModel.sessionInfo = SessionInfo(
			collectionId = 1L,
			startTime = startTime,
			endTime = endTime,
			cardsNumber = cardsReviewed,
			failedCards = cardsFailed
		)
		
		viewModel.addNewSession()
		
		verify(mockSessionDao).insertSession(check {
			Assertions.assertEquals(startTime, it.startTime)
			Assertions.assertEquals(endTime, it.endTime)
			Assertions.assertEquals(endTime - startTime, it.duration)
			Assertions.assertEquals(cardsReviewed, it.cardsReviewed)
			Assertions.assertEquals(cardsFailed, it.cardsFailed)
			Assertions.assertEquals(expectedScore, it.score)
		})
	}
	
	@Test
	fun `getTotalCardCount returns correct total count`() = testScope.runTest {
		val totalCardCount = 100L
		
		`when`(mockCardDao.getTotalCardCount()).thenReturn(totalCardCount)
		
		val result = viewModel.getTotalCardCount()
		
		Assertions.assertEquals(totalCardCount, result)
		verify(mockCardDao).getTotalCardCount()
	}
	
	@Test
	fun `getMasteredCardsCount returns correct mastered count`() = testScope.runTest {
		val masteredCardCount = 40L
		
		`when`(mockCardDao.getMasteredCardCount()).thenReturn(masteredCardCount)
		
		val result = viewModel.getMasteredCardsCount()
		
		Assertions.assertEquals(masteredCardCount, result)
		verify(mockCardDao).getMasteredCardCount()
	}
	
	@Test
	fun `getSuccessRate calculates average success rate correctly`() = testScope.runTest {
		val successRate = 85.0
		
		`when`(mockSessionDao.getAverageScore()).thenReturn(successRate)
		
		val result = viewModel.getSuccessRate()
		
		Assertions.assertEquals(String.format("%.2f", successRate).toDouble(), result)
		verify(mockSessionDao).getAverageScore()
	}
	
	@Test
	fun `getTimeSpent calculates total duration correctly`() = testScope.runTest {
		val totalDuration = 7200000L // 2 hours in milliseconds
		
		`when`(mockSessionDao.getTotalDuration()).thenReturn(totalDuration)
		
		val result = viewModel.getTimeSpent()
		
		Assertions.assertEquals(2, result.hour)
		Assertions.assertEquals(0, result.minute)
		Assertions.assertEquals(0, result.second)
		verify(mockSessionDao).getTotalDuration()
	}
	
	@Test
	fun `moveToFirstBox resets card box number to 0 and updates database`() = testScope.runTest {
		val card = CardEntity(1, "Q1", "A1", 1, 3, 0, 0, 0)
		val lastReviewDate = System.currentTimeMillis()
		val dueDate = calculateDueDate(0, lastReviewDate)
		val updatedCard = card.copy(
			boxNumber = 0,
			lastReviewDate = lastReviewDate,
			dueDate = dueDate,
			isMastered = 0
		)
		
		viewModel.moveToFirstBox(card)
		
		verify(mockCardDao).upsertCard(check {
			Assertions.assertEquals(updatedCard.id, it.id)
			Assertions.assertEquals(updatedCard.question, it.question)
			Assertions.assertEquals(updatedCard.answer, it.answer)
			Assertions.assertEquals(updatedCard.collectionId, it.collectionId)
			Assertions.assertEquals(updatedCard.boxNumber, it.boxNumber)
			Assertions.assertEquals(updatedCard.lastReviewDate, it.lastReviewDate)
			Assertions.assertEquals(updatedCard.dueDate, it.dueDate)
			Assertions.assertEquals(updatedCard.isMastered, it.isMastered)
		})
	}
	
}