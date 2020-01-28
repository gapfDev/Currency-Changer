package com.alxdev.two.moneychanger

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.alxdev.two.moneychanger.data.local.MoneyChangerDataBase
import com.alxdev.two.moneychanger.data.local.dao.CurrencyDao
import com.alxdev.two.moneychanger.data.local.entity.Currency
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MoneyChangerDataBaseTest {

    private lateinit var currencyDAO: CurrencyDao
    private lateinit var moneyChangerDataBase: MoneyChangerDataBase

    @Before
    fun createDB() {

        val context = InstrumentationRegistry.getInstrumentation().context
        moneyChangerDataBase =
            Room.inMemoryDatabaseBuilder(context, MoneyChangerDataBase::class.java)
                .allowMainThreadQueries()
                .build()

        currencyDAO = moneyChangerDataBase.currencyDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        moneyChangerDataBase.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGet() {
        val currency = Currency()
        currencyDAO.insert(currency)
        val coin: Currency? = currencyDAO.getCurrency()
        assertEquals(coin?.value, 0.0)
    }
}