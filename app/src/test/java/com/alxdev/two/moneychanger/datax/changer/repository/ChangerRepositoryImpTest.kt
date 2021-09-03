package com.alxdev.two.moneychanger.datax.changer.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alxdev.two.moneychanger.CoroutineTestRule
import com.alxdev.two.moneychanger.datax.datasource.entity.Currency
import com.alxdev.two.moneychanger.datax.datasource.entity.History
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerExternalDataSource
import com.alxdev.two.moneychanger.domainx.repocontract.ChangerLocalDataSource
import com.alxdev.two.moneychanger.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.concurrent.TimeoutException


@ExperimentalCoroutinesApi
class ChangerRepositoryImpTest {

    @get:Rule
    var initRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var changerLocalDataSource: ChangerLocalDataSource

    @Mock
    lateinit var changerExternalDataSource: ChangerExternalDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    private fun initRepo(): ChangerRepositoryImp = ChangerRepositoryImp(
        changerLocalDataSource,
        changerExternalDataSource,
    )

    @Test
    fun `historyLiveData WHEN calling DB THEN get list of History elements = 4`() {
        Mockito.`when`(changerLocalDataSource.getHistoryByDesc())
            .thenReturn(getHistoryListLive())

        val repo = initRepo()
        assertEquals(4, repo.historyLiveData.getOrAwaitValue().size)

    }

    @Test(expected = TimeoutException::class)
    fun `historyLiveData WHEN calling DB THEN get list of History elements = empty THEN LiveData_value is not called`() {
        Mockito.`when`(changerLocalDataSource.getHistoryByDesc())
            .thenReturn(getHistoryListLiveEmpty())

        val repo = initRepo()
        val list = repo.historyLiveData.getOrAwaitValue()
        assertEquals(0, list.size)
    }

    @Test
    fun `getCurrencyListLive WHEN THEN`() {
        Mockito.`when`(changerLocalDataSource.getAllOrderByAsc())
            .thenReturn(getCurrencyListLive())

        val repo = initRepo()
        assertEquals(4, repo.currencyList.getOrAwaitValue().size)
    }

    @Test
    fun `getCurrencyListLive WHEN empty THEN return Default value`() {
        Mockito.`when`(changerLocalDataSource.getAllOrderByAsc())
            .thenReturn(getCurrencyListLiveEmpty())

        val repo = initRepo()
        assertEquals(1, repo.currencyList.getOrAwaitValue().size)
    }

    @Test
    fun `getRawCurrencyListLive WHEN empty THEN return 1 Default value USA`() {
        Mockito.`when`(changerLocalDataSource.getAllOrderByAsc())
            .thenReturn(getCurrencyListLiveEmpty())

        val repo = initRepo()
        val list = repo.currencyList.getOrAwaitValue()
        assertEquals(1, list.size)
        assertEquals("USA", list[0].shortName)
    }

    @Test
    fun `getDefaultCurrency WHEN empty THEN return 1`() {
        Mockito.`when`(changerLocalDataSource.getAllOrderByAsc())
            .thenReturn(getCurrencyListLiveEmpty())

        val repo = initRepo()
        assertEquals(1, repo.getDefaultCurrency().size)
    }

    @Test
    fun `getDefaultCurrency WHEN empty THEN return shortName USA`() {
        Mockito.`when`(changerLocalDataSource.getAllOrderByAsc())
            .thenReturn(getCurrencyListLiveEmpty())

        val repo = initRepo()
        assertEquals("USA", repo.getDefaultCurrency()[0].shortName)
    }

    private fun getHistoryListLive(): LiveData<List<History>?> {
        val singleHistory = History(0, "Local_country", "Foreign_Country", 1.0, 1.0)

        return MutableLiveData<List<History>>().apply {
            value = listOf(singleHistory, singleHistory, singleHistory, singleHistory)
        }
    }

    private fun getHistoryListLiveEmpty(): LiveData<List<History>?> {
        return MutableLiveData<List<History>>().apply {
            value = listOf()
        }
    }

    private fun getCurrencyListLive(): LiveData<List<Currency>?> {
        val singleCurrency = Currency()
        return MutableLiveData<List<Currency>>().apply {
            value = listOf(
                singleCurrency,
                singleCurrency,
                singleCurrency,
                singleCurrency,
            )
        }
    }

    private fun getCurrencyListLiveEmpty(): LiveData<List<Currency>?> {
        return MutableLiveData<List<Currency>>().apply {
            value = listOf()
        }
    }
}