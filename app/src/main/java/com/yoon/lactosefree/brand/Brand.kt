package com.yoon.lactosefree.brand

import android.net.Uri

data class Brand(
    val brandName : String = "",
    val insteadMilk : List<String> = emptyList(),
    val brandLogoImage: Uri = Uri.EMPTY,
    val brandMarkerImage : Uri = Uri.EMPTY
)

