package com.eiviayw.universalprinter.bean

enum class BuildMode(
    val value:Int,
    val label: String,
) {
    Graphic(0,"图像"),
    Text(1,"文本"),
    NONE(-1,""),
}