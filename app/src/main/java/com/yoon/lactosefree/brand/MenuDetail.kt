package com.yoon.lactosefree.brand

data class MenuDetail(
    val size : Int,
    val kcal : Float,
    val sodium : Float,
    val sugar : Float,
    val fat : Float,
    val protein : Float,
    val caffeine : Float,
    val includeMilk : Boolean
)