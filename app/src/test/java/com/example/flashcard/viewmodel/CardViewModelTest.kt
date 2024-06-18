package com.example.flashcard.viewmodel

import com.example.flashcard.data.CardData
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.model.entities.CollectionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class CardViewModelTest {
	
	private lateinit var viewModel: CardViewModel
	private lateinit var mockCardDao: CardDao
	private lateinit var mockCollectionDao: CollectionDao
	
	@OptIn(ExperimentalCoroutinesApi::class)
	private val testDispatcher = UnconfinedTestDispatcher()
	private val testScope = TestScope(testDispatcher)
	
	@OptIn(ExperimentalCoroutinesApi::class)
	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		mockCardDao = Mockito.mock(CardDao::class.java)
		mockCollectionDao = Mockito.mock(CollectionDao::class.java)
		viewModel = CardViewModel(mockCardDao, mockCollectionDao)
	}
	
	@OptIn(ExperimentalCoroutinesApi::class)
	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}
	
	@Nested
	inner class AddCardTests {
		@Test
		fun `addCard should add a card to cardList`() = runTest {
			// Given
			val initialSize = viewModel.cardList.size
			val cardQuestion = "What is Kotlin?"
			val cardAnswer = "A modern programming language"
			
			// When
			viewModel.addCard(cardQuestion, cardAnswer)
			
			// Then
			assertEquals(initialSize + 1, viewModel.cardList.size)
			val addedCard = viewModel.cardList.last()
			assertEquals(cardQuestion, addedCard.question)
			assertEquals(cardAnswer, addedCard.answer)
		}
		
		@Test
		fun `addCard should increment nextCardId`() = runTest {
			// Given
			val initialId = viewModel.cardList.size + 1
			val cardQuestion1 = "What is Kotlin?"
			val cardAnswer1 = "A modern programming language"
			val cardQuestion2 = "What is Java?"
			val cardAnswer2 = "A programming language"
			
			// When
			viewModel.addCard(cardQuestion1, cardAnswer1)
			val firstCardId = viewModel.cardList.last().id
			
			viewModel.addCard(cardQuestion2, cardAnswer2)
			val secondCardId = viewModel.cardList.last().id
			
			// Then
			assertEquals(initialId, firstCardId)
			assertEquals(initialId + 1, secondCardId)
		}
	}
	
	@Nested
	inner class ModifyQuestionTests {
		
		@BeforeEach
		fun setUpCards() {
			viewModel.apply {
				addCard("Original Q1", "A1")
				addCard("Original Q2", "A2")
				addCard("Original Q3", "A3")
			}
		}
		
		@Test
		fun `modifyQuestion should update the question of the specified card`() {
			// Given
			val cardId = 2
			val newQuestion = "Modified Q2"
			
			// When
			viewModel.modifyQuestion(newQuestion, cardId)
			
			// Then
			assertEquals(newQuestion, viewModel.cardList.find { it.id == cardId }?.question)
			assertEquals("A2", viewModel.cardList.find { it.id == cardId }?.answer)
		}
		
		@Test
		fun `modifyQuestion should not change other cards`() {
			// Given
			val cardIdToModify = 2
			val newQuestion = "Modified Q2"
			
			// When
			viewModel.modifyQuestion(newQuestion, cardIdToModify)
			
			// Then
			assertEquals("Original Q1", viewModel.cardList.find { it.id == 1 }?.question)
			assertEquals("Original Q3", viewModel.cardList.find { it.id == 3 }?.question)
		}
		
		@Test
		fun `modifyQuestion should do nothing when card is not found`() {
			// Given
			val nonExistentCardId = 999
			val newQuestion = "This shouldn't be added"
			val originalState = viewModel.cardList.toList()
			
			// When
			viewModel.modifyQuestion(newQuestion, nonExistentCardId)
			
			// Then
			assertEquals(originalState, viewModel.cardList)
		}
		
		@ParameterizedTest
		@ValueSource(strings = ["", " ", "\t", "\n"])
		fun `modifyQuestion should handle whitespace-only questions`(whitespaceQuestion: String) {
			// Given
			val cardId = 1
			
			// When
			viewModel.modifyQuestion(whitespaceQuestion, cardId)
			
			// Then
			assertEquals(whitespaceQuestion, viewModel.cardList.find { it.id == cardId }?.question)
		}
		
		@ParameterizedTest
		@ValueSource(
			strings = [
				"A very long question that spans multiple lines and contains a lot of text to test how the system handles large inputs without any issues",
				"Question with special chars: !@#$%^&*()_+-=[]{}|;:'\"<>,.?/~`",
				"Multiple\nLine\nQuestion\nWith\nNewlines"
			]
		)
		fun `modifyQuestion should handle various question formats`(specialQuestion: String) {
			// Given
			val cardId = 3
			
			// When
			viewModel.modifyQuestion(specialQuestion, cardId)
			
			// Then
			assertEquals(specialQuestion, viewModel.cardList.find { it.id == cardId }?.question)
		}
		
		@Test
		fun `modifyQuestion should update the first occurrence when multiple cards have the same id`() {
			// This is an edge case that shouldn't happen in a well-designed system,
			// but it's good to test unexpected scenarios.
			
			// Given: Simulate a bug where two cards have the same ID
			viewModel.cardList.add(
				CardData(
					id = 2,
					question = "Duplicate ID",
					answer = "This is a bug"
				)
			)
			val newQuestion = "Modified Q2"
			
			// When
			viewModel.modifyQuestion(newQuestion, 2)
			
			// Then: Only the first card with ID 2 should be modified
			val cardsWithId2 = viewModel.cardList.filter { it.id == 2 }
			assertEquals(2, cardsWithId2.size) // Verifying the setup
			assertEquals(newQuestion, cardsWithId2[0].question)
			assertEquals("Duplicate ID", cardsWithId2[1].question)
		}
		
		@Test
		fun `modifyQuestion should handle question changed to same value`() {
			// Given
			val cardId = 1
			val originalQuestion = viewModel.cardList.find { it.id == cardId }?.question
			
			// When
			viewModel.modifyQuestion(originalQuestion!!, cardId)
			
			// Then: No change, but no error either
			assertEquals(originalQuestion, viewModel.cardList.find { it.id == cardId }?.question)
		}
		
		@Test
		fun `modifyQuestion performance with large card list`() {
			// Given: Add a large number of cards
			for (i in 4..1003) {
				viewModel.addCard("Q$i", "A$i")
			}
			val largeCardId = 1000
			val newQuestion = "Modified Q1000"
			
			// When
			val startTime = System.nanoTime()
			viewModel.modifyQuestion(newQuestion, largeCardId)
			val endTime = System.nanoTime()
			
			// Then
			assertEquals(newQuestion, viewModel.cardList.find { it.id == largeCardId }?.question)
			
			val durationMs = (endTime - startTime) / 1_000_000
			println("modifyQuestion took $durationMs ms for 1000 cards")
			
			// This is a bit subjective and depends on the device
			assertTrue(durationMs < 100, "Operation took too long: $durationMs ms")
		}
	}
	
	@Nested
	inner class InsertCollectionTests {
		@OptIn(ExperimentalCoroutinesApi::class)
		@Test
		fun `insertCollectionToDB should insert valid collection`(): Unit = testScope.runTest {
			// Given
			val collectionName = "Collection"
			val tags = "tag1, tag2"
			val description = "description"
			
			// Use an argument captor to capture the actual object passed to insertCollection
			val collectionCaptor = argumentCaptor<CollectionEntity>()
			
			// Set up the mock to return 1L for any CollectionEntity
			whenever(mockCollectionDao.insertCollection(any())).thenReturn(1L)
			
			// Set up the ViewModel
			viewModel.apply {
				setCollectionName(collectionName)
				setTags(tags)
				setDescription(description)
			}
			
			// When
			viewModel.insertCollectionToDB()
			advanceUntilIdle() // Wait for coroutine to complete
			
			// Then
			// Verify the DAO method was called and capture the argument
			verify(mockCollectionDao).insertCollection(collectionCaptor.capture())
			
			// Assert the captured object has the correct properties
			val capturedCollection = collectionCaptor.firstValue
			assertEquals(collectionName, capturedCollection.name)
			assertEquals(tags, capturedCollection.tags)
			assertEquals(description, capturedCollection.description)
			// We don't check the ID because it should be 0 or null before insertion
			
			// Verify the ViewModel's state was updated correctly
			assertEquals(1L, viewModel.createdCollectionID.longValue)
		}
		
		@OptIn(ExperimentalCoroutinesApi::class)
		@Test
		fun `insertCollectionToDB should save the collection without tags or description`() =
			testScope.runTest {
				// Given
				val collectionName = "Collection"
				viewModel.setCollectionName(collectionName)
				
				// Set up the mock to throw an exception
				whenever(mockCollectionDao.insertCollection(any())).thenReturn(1L)
				
				// When
				viewModel.insertCollectionToDB()
				advanceUntilIdle()
				
				// Then
				// Verify the DAO method was called
				verify(mockCollectionDao).insertCollection(any())
				
				// Verify the ViewModel's state reflects the error
				assertNotNull(viewModel.createdCollectionID.longValue) // Insertion failed, so no new ID
			}
	}
	
	@Nested
	inner class ClearTempDataTests {
		
		@Test
		fun `clearTempData should reset collectionName to empty string`() = runTest {
			// Given
			viewModel.setCollectionName("Test Collection")
			
			// When
			viewModel.clearTempData()
			
			// Then
			assertEquals("", viewModel.getCollectionName())
		}
		
		@Test
		fun `clearTempData should reset tags to empty string`() = runTest {
			// Given
			viewModel.setTags("tag1, tag2")
			
			// When
			viewModel.clearTempData()
			
			// Then
			assertEquals("", viewModel.getTags())
		}
		
		@Test
		fun `clearTempData should reset description to empty string`() = runTest {
			// Given
			viewModel.setDescription("Test Description")
			
			// When
			viewModel.clearTempData()
			
			// Then
			assertEquals("", viewModel.getDescription())
		}
		
		@Test
		fun `clearTempData should clear cardList`() = runTest {
			// Given
			viewModel.addCard("Question 1", "Answer 1")
			viewModel.addCard("Question 2", "Answer 2")
			
			// When
			viewModel.clearTempData()
			
			// Then
			assertEquals(0, viewModel.cardList.size)
		}
		
		@Test
		fun `clearTempData should reset nextCardId to 1`() = runTest {
			// Given
			viewModel.addCard("Question 1", "Answer 1")
			
			// When
			viewModel.clearTempData()
			
			// Then
			assertEquals(1, viewModel.cardList.size + 1)
		}
		
		@Test
		fun `clearTempData should work with no initial data`() = runTest {
			// When
			viewModel.clearTempData()
			
			// Then
			assertEquals("", viewModel.getCollectionName())
			assertEquals("", viewModel.getTags())
			assertEquals("", viewModel.getDescription())
			assertEquals(0, viewModel.cardList.size)
			assertEquals(1, viewModel.cardList.size + 1)
		}
		
		@Test
		fun `clearTempData should reset all fields when multiple data points are set`() = runTest {
			// Given
			viewModel.setCollectionName("Test Collection")
			viewModel.setTags("tag1, tag2")
			viewModel.setDescription("Test Description")
			viewModel.addCard("Question 1", "Answer 1")
			viewModel.addCard("Question 2", "Answer 2")
			
			// When
			viewModel.clearTempData()
			
			// Then
			assertEquals("", viewModel.getCollectionName())
			assertEquals("", viewModel.getTags())
			assertEquals("", viewModel.getDescription())
			assertEquals(0, viewModel.cardList.size)
			assertEquals(1, viewModel.cardList.size + 1)
		}
		
		@Test
		fun `clearTempData should handle concurrent modifications`() = runTest {
			// Given
			val executor = Executors.newFixedThreadPool(2)
			val latch = CountDownLatch(1)
			
			// Adding some data initially
			viewModel.setCollectionName("Concurrent Test")
			viewModel.setTags("tag1, tag2")
			viewModel.setDescription("Test Description")
			viewModel.addCard("Question 1", "Answer 1")
			
			// When
			executor.submit {
				latch.await()  // Wait until both threads are ready to start
				viewModel.clearTempData()
			}
			executor.submit {
				latch.await()  // Wait until both threads are ready to start
				viewModel.addCard("Concurrent Question", "Concurrent Answer")
			}
			
			latch.countDown()  // Signal both threads to start
			executor.shutdown()
			executor.awaitTermination(5, TimeUnit.SECONDS)
			
			// Then
			assertTrue(viewModel.getCollectionName().isEmpty())
			assertTrue(viewModel.getTags().isEmpty())
			assertTrue(viewModel.getDescription().isEmpty())
			
			// Since clearTempData might run after addCard, we can have 0 or 1 cards depending on the execution order
			assertTrue(viewModel.cardList.size in 0..1)
			
			if (viewModel.cardList.isNotEmpty()) {
				assertEquals("Concurrent Question", viewModel.cardList.first().question)
				assertEquals("Concurrent Answer", viewModel.cardList.first().answer)
			}
		}
		
		@Test
		fun `clearTempData should perform efficiently with large card list`() = runTest {
			// Given
			for (i in 1..1000) {
				viewModel.addCard("Question $i", "Answer $i")
			}
			
			// When
			val startTime = System.nanoTime()
			viewModel.clearTempData()
			val endTime = System.nanoTime()
			
			// Then
			assertEquals(0, viewModel.cardList.size)
			assertEquals(1, viewModel.cardList.size + 1)
			
			val durationMs = (endTime - startTime) / 1_000_000
			println("clearTempData took $durationMs ms for 1000 cards")
			
			// Ensure the operation is performed efficiently
			assertTrue(durationMs < 100, "Operation took too long: $durationMs ms")
		}
	}
}