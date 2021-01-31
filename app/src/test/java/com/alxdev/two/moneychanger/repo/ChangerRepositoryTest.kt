  package com.alxdev.two.moneychanger.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alxdev.two.moneychanger.CoroutineTestRule
import com.alxdev.two.moneychanger.core.data.external.CurrencyAPIAction
import com.alxdev.two.moneychanger.core.data.external.CurrencyCountryAPIAction
import com.alxdev.two.moneychanger.core.data.local.CurrencyDAOAction
import com.alxdev.two.moneychanger.core.data.local.HistoryDAOAction
import com.alxdev.two.moneychanger.data.local.entity.Currency
import com.alxdev.two.moneychanger.data.local.entity.History
import com.alxdev.two.moneychanger.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
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
class ChangerRepositoryTest {

    @get:Rule
    var initRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var historyDAOAction: HistoryDAOAction

    @Mock
    lateinit var currencyDAOAction: CurrencyDAOAction

    @Mock
    lateinit var currencyAPIAction: CurrencyAPIAction

    @Mock
    lateinit var currencyCountryAPIAction: CurrencyCountryAPIAction

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    private fun initRepo(): ChangerRepository = ChangerRepository(
        historyDAOAction,
        currencyDAOAction,
        currencyAPIAction,
        currencyCountryAPIAction,
    )

    @Test
    fun `historyLiveData WHEN calling DB THEN get list of History elements = 4`() {
        Mockito.`when`(historyDAOAction.getHistoryByDesc())
            .thenReturn(getHistoryListLive())

        val repo = initRepo()
        Assert.assertEquals(4, repo.historyLiveData.getOrAwaitValue().size)

    }

    @Test(expected = TimeoutException::class)
    fun `historyLiveData WHEN calling DB THEN get list of History elements = empty THEN LiveData_value is not called`() {
        Mockito.`when`(historyDAOAction.getHistoryByDesc())
            .thenReturn(getHistoryListLiveEmpty())

        val repo = initRepo()
        Assert.assertEquals(0, repo.historyLiveData.getOrAwaitValue().size)
    }

    @Test
    fun `getCurrencyListLive WHEN THEN`() {
        Mockito.`when`(currencyDAOAction.getAllOrderByAsc())
            .thenReturn(getCurrencyListLive())

        val repo = initRepo()
        Assert.assertEquals(4, repo.currencyList.getOrAwaitValue().size)
    }

    @Test
    fun `getCurrencyListLive WHEN empty THEN return Default value`() {
        Mockito.`when`(currencyDAOAction.getAllOrderByAsc())
            .thenReturn(getCurrencyListLiveEmpty())

        val repo = initRepo()
        Assert.assertEquals(1, repo.currencyList.getOrAwaitValue().size)
    }

    @Test
    fun `getRawCurrencyListLive WHEN empty THEN return 0`() {
        Mockito.`when`(currencyDAOAction.getAllOrderByAsc())
            .thenReturn(getCurrencyListLiveEmpty())

        val repo = initRepo()
        Assert.assertEquals(0, repo.rawCurrencyList.getOrAwaitValue().size)
    }

//    @Test
//    fun `getRawDefaultCurrency WHEN empty THEN return 0`() {
//        Mockito.`when`(currencyDAOAction.getAllOrderByAsc())
//            .thenReturn(getCurrencyListLiveEmpty())
//
//        val repo = initRepo()
//        Assert.assertEquals(0, repo.getRawDefaultCurrency().size)
//    }

    @Test
    fun `getDefaultCurrency WHEN empty THEN return 1`() {
        Mockito.`when`(currencyDAOAction.getAllOrderByAsc())
            .thenReturn(getCurrencyListLiveEmpty())

        val repo = initRepo()
        Assert.assertEquals(1, repo.getDefaultCurrency().size)
    }

    @Test
    fun `getDefaultCurrency WHEN empty THEN return shortName USA`() {
        Mockito.`when`(currencyDAOAction.getAllOrderByAsc())
            .thenReturn(getCurrencyListLiveEmpty())

        val repo = initRepo()
        Assert.assertEquals("USA", repo.getDefaultCurrency()[0].shortName)
    }

//    @ExperimentalCoroutinesApi
//    @Test
//    fun `getResult WHEN THEN`() = runBlockingTest {
//
//        Mockito.`when`(currencyAPIAction.getCurrencyResult())
//            .thenReturn(Result.Ok(mock(), mock()))
//
//        val repo = initRepo()
//        repo.syncCurrencyAPI()
//
//        verify(repo.currencyDAOImp, times(1))
//            .clear()
//    }

//    Test tools

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