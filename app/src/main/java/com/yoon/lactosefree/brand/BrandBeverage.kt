package com.yoon.lactosefree.brand

import android.net.Uri

data class BrandBeverage(
    val brandName: String = "",
    val beverageName: String = "",
    val beverageImage: Uri = Uri.EMPTY,
    val beverageCategory: String = "",
    val beverageSize : Int = 0,
    val beverageKcal: Float = 0.0f,
    val beverageProtein: Float = 0.0f,
    val beverageSugar: Float = 0.0f,
    val beverageSodium: Float = 0.0f,
    val beverageFat: Float = 0.0f,
    val beverageCaffeine: Float = 0.0f,
    val includeMilk : Boolean = false,
    var favorite : Boolean = false
)

