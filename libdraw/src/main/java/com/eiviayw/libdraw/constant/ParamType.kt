package com.eiviayw.libdraw.constant

enum class ParamType(
    val index:Int,
    val label:String
) {
    SINGLE_TEXT(0,"单列文本"),
    MULTI_TEXT(1,"多列文本"),
    IMAGE(2,"图片"),
    LINE(3,"分割线"),
    MULTI(4,"混排"),
    BITMAP(5,"图像参数"),
}