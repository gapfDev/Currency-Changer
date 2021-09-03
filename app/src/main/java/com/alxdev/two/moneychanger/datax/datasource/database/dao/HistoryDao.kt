package com.alxdev.two.moneychanger.datax.datasource.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alxdev.two.moneychanger.datax.datasource.entity.History

@Dao
interface HistoryDao {

    @Insert
    fun insert(history: History)

    @Update
    fun update(vararg history: History)

    @Query("SELECT * FROM " + History.TABLE_NAME + " WHERE id = :id")
    fun findById(id: Int): History

    @Delete
    fun delete(vararg history: History)

    @Query("SELECT * FROM " + History.TABLE_NAME + " ORDER BY id DESC")
    fun getAllLiveData(): LiveData<List<History>?>
}