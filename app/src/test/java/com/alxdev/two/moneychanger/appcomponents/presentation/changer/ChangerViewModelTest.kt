package com.alxdev.two.moneychanger.appcomponents.presentation.changer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.alxdev.two.moneychanger.Constant
import com.alxdev.two.moneychanger.CoroutineTestRule
import com.alxdev.two.moneychanger.domainx.model.Currency
import com.alxdev.two.moneychanger.domainx.model.History
import com.alxdev.two.moneychanger.domainx.usecasecontract.*
import com.alxdev.two.moneychanger.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class ChangerViewModelTest {

    @get:Rule
    var initRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var currencyListUseCase: CurrencyListUseCase

    @Mock
    private lateinit var historyUseCase: HistoryUseCase

    @Mock
    private lateinit var defaultCurrencyUseCase: DefaultCurrencyUseCase

    @Mock
    private lateinit var syncCountryWithCurrencyUseCase: SyncCountryWithCurrencyUseCase

    @Mock
    private lateinit var saveHistoryUseCase: SaveHistoryUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    private fun initViewModel() = ChangerViewModel(
        currencyListUseCase,
        historyUseCase,
        defaultCurrencyUseCase,
        syncCountryWithCurrencyUseCase,
        saveHistoryUseCase,
    )

    @Test
    fun `localCurrencyList WHEN local default value return list of Currency THEN list not empty `() {
        Mockito.`when`(defaultCurrencyUseCase.execute())
            .thenReturn(listOf(singleCurrency()))

        val vm = initViewModel()

        assertEquals(
            1,
            vm.localCurrencyList.size,
        )
    }

    @Test
    fun `localCurrencyList WHEN returning default value list of Currency THEN list element default name USA `() {
        Mockito.`when`(defaultCurrencyUseCase.execute())
            .thenReturn(listOf(singleCurrency()))

        val vm = initViewModel()

        assertEquals(
            "USA",
            vm.localCurrencyList[0].shortName
        )
    }

    @Test
    fun `foreignCurrencyList WHEN DB return list of Currency THEN list not empty `() {
        val _repoCurrencyList =
            MutableLiveData<List<Currency>>().apply {
                value = currencyList()
            }

        Mockito.`when`(currencyListUseCase.execute())
            .thenReturn(_repoCurrencyList)

        val vm = initViewModel()

        assertEquals(
            4,
            vm.foreignCurrencyList.getOrAwaitValue().size,
        )
    }

    @Test
    fun `historyChange WHEN DB return empty list THEN list size 0`() {
        val _repoHistory =
            MutableLiveData<List<History>>().apply {
                value = listOf()
            }

        Mockito.`when`(historyUseCase.execute())
            .thenReturn(_repoHistory)

        val vm = initViewModel()
        assertEquals(
            0,
            vm.historyChange.getOrAwaitValue().size
        )
    }

    @Test
    fun `historyChange WHEN DB return list THEN list not empty`() {
        val _repoHistory =
            MutableLiveData<List<History>>().apply {
                value = changeHistory()
            }

        Mockito.`when`(historyUseCase.execute())
            .thenReturn(_repoHistory)

        val vm = initViewModel()
        assertEquals(
            4,
            vm.historyChange.getOrAwaitValue().size
        )
    }

    private fun singleCurrency(): Currency {
        return Constant.ModelDefault.currencyModel
    }

    private fun currencyList(): List<Currency> {
        val singleCurrency = Constant.ModelDefault.currencyModel
        return listOf(
            singleCurrency,
            singleCurrency,
            singleCurrency,
            singleCurrency
        )
    }

    private fun changeHistory(): List<History> {
        val singleHistory = Constant.ModelDefault.changeHistory
        return listOf(
            singleHistory,
            singleHistory,
            singleHistory,
            singleHistory,
        )
    }
}