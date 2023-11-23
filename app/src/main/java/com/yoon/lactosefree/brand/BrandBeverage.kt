package com.yoon.lactosefree.brand

data class BrandBeverage(
    val brandName: String,
    val brandImage: String,
    val beverageName: String,
    val beverageImage: String,
    val beverageCategory: String,
    val beverageKcal: Float,
    val beverageSugar: Float,
    val beverageProtein: Float,
    val beverageSodium: Float,
    val beverageFat: Float,
    val beverageCaffeine: Float,
    val favorite : Boolean
)