package com.alxdev.two.moneychanger.ui.changer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.alxdev.two.moneychanger.Constant
import com.alxdev.two.moneychanger.CoroutineTestRule
import com.alxdev.two.moneychanger.data.model.Currency
import com.alxdev.two.moneychanger.data.model.History
import com.alxdev.two.moneychanger.getOrAwaitValue
import com.alxdev.two.moneychanger.repo.ChangerRepository
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

@ExperimentalCoroutinesApi
class ChangerViewModelTest {

    @get:Rule
    var initRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var changerRepository: ChangerRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    private fun initViewModel() = ChangerViewModel(changerRepository)

    @Test
    fun `localCurrencyList WHEN local default value return list of Currency THEN list not empty `() {
        Mockito.`when`(changerRepository.getDefaultCurrency())
            .thenReturn(listOf(singleCurrency()))

        val vm = initViewModel()

        Assert.assertEquals(
            1,
            vm.localCurrencyList.size,
        )
    }

//    @Test
//    fun `localCurrencyList WHEN local default value return empty list of Currency THEN list have default value `() {
//        Mockito.`when`(changerRepository.getDefaultCurrency())
//            .thenReturn(listOf(singleCurrency()))
//
//        val vm = initViewModel()
//
//        Assert.assertEquals(
//            1,
//            vm.localCurrencyList.size,
//        )
//    }

//    @Test
//    fun `localCurrencyList WHEN returning default value list of Currency THEN list element default name USA `() {
//        Mockito.`when`(changerRepository.getDefaultCurrency())
//            .thenReturn(listOf(singleCurrency()))
//
//        val vm = initViewModel()
//
//        Assert.assertEquals(
//            "USA",
//            vm.localCurrencyList[0].shortName
//        )
//    }

    @Test
    fun `foreignCurrencyList WHEN DB return list of Currency THEN list not empty `() {
        val _repoCurrencyList =
            MutableLiveData<List<Currency>>().apply {
                value = currencyList()
            }

        Mockito.`when`(changerRepository.currencyList)
            .thenReturn(_repoCurrencyList)

        val vm = initViewModel()

        Assert.assertEquals(
            4,
            vm.foreignCurrencyList.getOrAwaitValue().size,
        )
    }

//    @Test
//    fun `foreignCurrencyList WHEN DB return empty list THEN list have default value `() {
//        val _repoCurrencyList =
//            MutableLiveData<List<Currency>>().apply {
//                value = listOf()
//            }
//
//        Mockito.`when`(changerRepository.currencyList)
//            .thenReturn(_repoCurrencyList)
//
//        val vm = initViewModel()
//
//        Assert.assertEquals(
//            1,
//            vm.foreignCurrencyList.getOrAwaitValue().size,
//        )
//    }

//    @Test
//    fun `foreignCurrencyList WHEN DB return empty list default value returned THEN list have default value USA `() {
//        val _repoCurrencyList =
//            MutableLiveData<List<Currency>>().apply {
//                value = listOf(singleCurrency())
//            }
//
//        Mockito.`when`(changerRepository.currencyList)
//            .thenReturn(_repoCurrencyList)
//
//        val vm = initViewModel()
//
//        Assert.assertEquals(
//            "USA",
//            vm.localCurrencyList[0].shortName
//        )
//    }

    @Test
    fun `historyChange WHEN DB return empty list THEN list size 0`() {
        val _repoHistory =
            MutableLiveData<List<History>>().apply {
                value = listOf()
            }

        Mockito.`when`(changerRepository.historyLiveData)
            .thenReturn(_repoHistory)

        val vm = initViewModel()
        Assert.assertEquals(
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

        Mockito.`when`(changerRepository.historyLiveData)
            .thenReturn(_repoHistory)

        val vm = initViewModel()
        Assert.assertEquals(
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