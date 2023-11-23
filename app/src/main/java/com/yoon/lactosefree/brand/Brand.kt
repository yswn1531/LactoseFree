package com.yoon.lactosefree.brand

data class Brand(
    val name : String,
    val image : Int,
    var favorite : Boolean = false
    //val insteadMilk : List<String>
)
