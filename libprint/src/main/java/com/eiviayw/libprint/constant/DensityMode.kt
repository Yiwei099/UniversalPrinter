package com.eiviayw.libprint.constant

import com.gprinter.command.LabelCommand

enum class DensityMode(
    val value:LabelCommand.DENSITY,
    val label:String
) {
    DENSITY_0(LabelCommand.DENSITY.DNESITY0, "一级"),
    DENSITY_1(LabelCommand.DENSITY.DNESITY1, "二级"),
    DENSITY_2(LabelCommand.DENSITY.DNESITY2, "三级"),
    DENSITY_3(LabelCommand.DENSITY.DNESITY3, "四级"),
    DENSITY_4(LabelCommand.DENSITY.DNESITY4, "五级"),
    DENSITY_5(LabelCommand.DENSITY.DNESITY5, "六级"),
    DENSITY_6(LabelCommand.DENSITY.DNESITY6, "七级"),
    DENSITY_7(LabelCommand.DENSITY.DNESITY7, "八级"),
    DENSITY_8(LabelCommand.DENSITY.DNESITY8, "九级"),
    DENSITY_9(LabelCommand.DENSITY.DNESITY9, "十级"),
    DENSITY_10(LabelCommand.DENSITY.DNESITY10, "十一级"),
    DENSITY_11(LabelCommand.DENSITY.DNESITY11, "十二级"),
    DENSITY_12(LabelCommand.DENSITY.DNESITY12, "十三级"),
    DENSITY_13(LabelCommand.DENSITY.DNESITY13, "十四级"),
    DENSITY_14(LabelCommand.DENSITY.DNESITY14, "十五级"),
    DENSITY_15(LabelCommand.DENSITY.DNESITY15, "十六级"),
}