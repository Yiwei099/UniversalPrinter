package com.eiviayw.libprint.constant

enum class PaperMode(
    val value:Int,
    val label: String,
    val width:Int,
    val heigth:Int
) {
    NONE(-1,"自适应",0,0),
    ESC_80(0,"80mm",576,0),
    ESC_58(1,"58mm",384,0),
    TSC_3020(2,"30*20",240,160),
    TSC_4030(3,"40*30",320,240),
    TSC_4060(4,"40*60",320,480),
    TSC_4080(5,"40*80",320,640),
    TSC_6040(6,"60*40",480,320),
    TSC_6080(7,"60*80",480,640),
}