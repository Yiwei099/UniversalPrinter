package com.eiviayw.libprint.bean.state

data class ViewState(
    val showModifyPrinterView: Boolean = false,
    val showBindPrinterView:Boolean = false,
    val showConnectModeListDialog:Boolean = false,
    val showPrinterModeListDialog:Boolean = false,
    val showSDKModeListDialog:Boolean = false,
    val showDeviceListDialog:Boolean = false,
    val showPaperListDialog:Boolean = false,
    val showBuildListDialog:Boolean = false,
    val showForWordListDialog:Boolean = false,
    val showDensityListDialog:Boolean = false,
)
