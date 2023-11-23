package com.yoon.lactosefree.brand

data class BrandBeverage(
    val brandName: String = "",
    val brandImage: String = "",
    val beverageName: String = "",
    val beverageImage: String = "",
    val beverageCategory: String = "",
    val beverageKcal: Float = 0.0f,
    val beverageSugar: Float = 0.0f,
    val beverageProtein: Float = 0.0f,
    val beverageSodium: Float = 0.0f,
    val beverageFat: Float = 0.0f,
    val beverageCaffeine: Float = 0.0f,
    val favorite : Boolean,
    val includeMilk : Boolean
)

