package com.yoon.lactosefree.brand

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BrandViewModel : ViewModel() {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "artist_collection"

    fun getBrandBeverage(){

    }


}