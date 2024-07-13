package com.eiviayw.universalprinter.viewMode

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewMode:ViewModel() {
    //打印机连接方式弹窗状态
    private val openConnectModeChoseDialog by lazy { MutableLiveData(false) }
    //打印机指令模式弹窗状态
    private val openPrinterModeChoseDialog by lazy { MutableLiveData(false) }
    //打印机设备列表弹窗状态
    private val openPrinterChoseDialog by lazy { MutableLiveData(false) }

    /**
     * 修改打印机连接方式弹窗状态
     */
    fun notifyConnectModeDialogState(state: Boolean) {
        openConnectModeChoseDialog.postValue(state)
    }

    /**
     * 修改打印机指令模式弹窗状态
     */
    fun notifyPrinterModeDialogState(state: Boolean) {
        openPrinterModeChoseDialog.postValue(state)
    }

    /**
     * 修改打印机设备列表弹窗状态
     */
    fun notifyPrinterDeviceDialogState(state: Boolean) {
        openPrinterChoseDialog.postValue(state)
    }
}