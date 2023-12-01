package com.yoon.lactosefree.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FavoriteRepository(application: Application) {

    private var favoriteDao : FavoriteDao
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val allFavorite: LiveData<List<Favorite>>?
    var searchResults = MutableLiveData<List<Favorite>>()



    init {
        val database : FavoriteRoomDatabase = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = database.favoriteDao()
        allFavorite = favoriteDao.getFavorite()
    }

    //이미지 가져와야함. 브랜드 이름도 가져와야함

    fun insertFavorite(favorite: Favorite){
        coroutineScope.launch {
            favoriteDao.insertFavorite(favorite)
        }
    }

    fun deleteFavorite(beverageName: String){
        coroutineScope.launch {
            favoriteDao.deleteFavorite(beverageName)
        }
    }

    fun updateFavorite(favorite: Favorite){
        coroutineScope.launch {
            favoriteDao.updateFavorite(favorite)
        }

    }

    fun findFavorite(beverageName: String){
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = coroutineScope.async(Dispatchers.IO) {
                return@async favoriteDao.findBeverage(beverageName)
            }.await()
        }
    }

}