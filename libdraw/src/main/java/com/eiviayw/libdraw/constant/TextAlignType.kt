package com.eiviayw.libdraw.constant

import com.eiviayw.library.Constant

enum class TextAlignType(
    val index:Int,
    val label:String
) {
    ALIGN_START(Constant.Companion.Align.ALIGN_START,"左对齐"),
    ALIGN_CENTER(Constant.Companion.Align.ALIGN_CENTER,"居中"),
    ALIGN_END(Constant.Companion.Align.ALIGN_END,"右对齐"),
}