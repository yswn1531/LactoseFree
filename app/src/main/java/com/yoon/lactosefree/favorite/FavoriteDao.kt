package com.yoon.lactosefree.favorite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteDao {

    @Insert
    fun insertFavorite(favorite: Favorite)

    @Update
    fun updateFavorite(vararg favorite: Favorite)

    @Query("DELETE FROM favorite WHERE brandBeverageName = :beverageName")
    fun deleteFavorite(beverageName : String)

    @Query("SELECT * FROM favorite WHERE brandBeverageName = :beverageName")
    fun findBeverage(beverageName: String): List<Favorite>

    @Query("SELECT * FROM favorite")
    fun getFavorite() : LiveData<List<Favorite>>



}