package com.yoon.lactosefree.favorite

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite")
data class Favorite(

    @ColumnInfo(name = "brandName")
    var brandName : String = "",

    @ColumnInfo(name = "rating")
    var rating : Float = 0.0f,

    @ColumnInfo(name = "content")
    var content : String = "",

    @ColumnInfo(name = "imageUri")
    var imageUri: Uri = Uri.EMPTY,

    @PrimaryKey
    @ColumnInfo(name = "brandBeverageName")
    var brandBeverageName : String = ""

)
