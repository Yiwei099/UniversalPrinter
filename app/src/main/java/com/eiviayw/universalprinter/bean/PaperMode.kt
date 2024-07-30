package com.eiviayw.universalprinter.bean

enum class PaperMode(
    val value:Int,
    val label: String,
) {
    NONE(-1,"自适应"),
    ESC_80(0,"80mm"),
    ESC_58(1,"58mm"),
    TSC_3020(2,"30*20"),
    TSC_4030(3,"40*30"),
    TSC_4060(4,"40*60"),
    TSC_4080(5,"40*80"),
    TSC_6040(6,"60*40"),
    TSC_6080(7,"60*80"),
}