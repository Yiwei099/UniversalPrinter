package com.eiviayw.libprint.constant

enum class PrinterMode(
    val value:Int,
    val label:String,
) {
    ESC(0,"收据"),
    TSC(1,"标签"),
    NONE(-1,""),
}