package com.example.flashcard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.flashcard.model.dao.CardDao
import com.example.flashcard.model.dao.CollectionDao
import com.example.flashcard.viewModel.CardViewModel
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CardViewModelTest {
	
	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()
	
	private lateinit var viewModel: CardViewModel
	private lateinit var cardDao: CardDao
	private lateinit var collectionDao: CollectionDao
	
	
	@Before
	fun setUp() {
		cardDao = mockk()
		collectionDao = mockk {
			every { getAllCollections() } returns flowOf(listOf()) // Return a flow of an empty list
		}
		viewModel = CardViewModel(cardDao, collectionDao)
	}
	
	@Test
	fun `test getCollectionName`() {
		// Given
		val collectionName = "Test Collection"
		viewModel.setCollectionName(collectionName)
		
		// When
		val result = viewModel.getCollectionName()
		
		// Then
		assertEquals(collectionName, result)
	}
}