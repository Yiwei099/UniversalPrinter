package com.eiviayw.libdraw.constant

import android.graphics.Typeface


enum class TextTypeFace(
    val face: Typeface,
    val label:String
) {
    DEFAULT(Typeface.DEFAULT, "默认"),
    BOLD(Typeface.DEFAULT_BOLD, "加粗"),
}