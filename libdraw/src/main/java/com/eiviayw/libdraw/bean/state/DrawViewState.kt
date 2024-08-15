package com.eiviayw.libdraw.bean.state

data class DrawViewState(
    val showPopupMenu: Boolean = false,
    val showBitmapOptionView:Boolean = false,
    val showSingleTextDialog: Boolean = false,
    val showMultiElementDialog: Boolean = false,
)
