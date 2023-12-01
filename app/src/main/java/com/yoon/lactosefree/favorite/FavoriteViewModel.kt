package com.yoon.lactosefree.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yoon.lactosefree.common.DefaultApplication

class FavoriteViewModel : ViewModel() {
    private val repository : FavoriteRepository = FavoriteRepository(DefaultApplication.applicationContext())
    private val allFavorites : LiveData<List<Favorite>>? = repository.allFavorite
    private val searchResults : MutableLiveData<List<Favorite>> = repository.searchResults


    fun updateFavorite(favorite: Favorite){
        repository.updateFavorite(favorite)
    }

    fun findFavorite(beverageName : String){
        repository.findFavorite(beverageName)
    }

    fun getSearchResults() : MutableLiveData<List<Favorite>>{
        return searchResults
    }

    fun getAllResult() : LiveData<List<Favorite>>?{
        return allFavorites
    }


}