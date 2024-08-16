package com.eiviayw.libprint.constant

enum class SDKMode(
    val value:Int,
    val label:String
) {
    GPrinter(0,"佳博"),
    EpsonESC(1,"爱普森小票"),
    BixolonTsc(2,"必胜龙标签"),
    NativeUsb(3,"原生USB(佳博无效时尝试此类型)"),
    NONE(-1,""),
}