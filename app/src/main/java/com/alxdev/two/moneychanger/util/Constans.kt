package com.alxdev.two.moneychanger

import com.alxdev.two.moneychanger.domainx.model.Currency
import com.alxdev.two.moneychanger.domainx.model.History

object Constant {
    object ModelDefault {
        val currencyModel: Currency = Currency(
            "United States Of America",
            "Dollar",
            "USA",
            1.00,
            ""
        )

        val changeHistory: History = History(
            "Demo Text"
        )
    }
}