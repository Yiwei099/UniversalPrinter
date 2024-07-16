package com.eiviayw.universalprinter.viewMode

import android.hardware.usb.UsbDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eiviayw.universalprinter.bean.BuildMode
import com.eiviayw.universalprinter.bean.ConnectMode
import com.eiviayw.universalprinter.bean.PrinterMode

class MainViewMode:ViewModel() {
    //打印机连接方式弹窗状态
    private val _openConnectModeChoseDialog = MutableLiveData(false)
    var openConnectModeChoseDialog:LiveData<Boolean> = _openConnectModeChoseDialog

    //打印机指令模式弹窗状态
    private val _openPrinterModeChoseDialog = MutableLiveData(false)
    var openPrinterModeChoseDialog:LiveData<Boolean> = _openPrinterModeChoseDialog

    //打印机设备列表弹窗状态
    private val _openPrinterChoseDialog = MutableLiveData(false)
    var openPrinterChoseDialog:LiveData<Boolean> = _openPrinterChoseDialog

    //指令构建方式弹窗状态
    private val _openBuildModeChoseDialog = MutableLiveData(false)
    var openBuildModeChoseDialog:LiveData<Boolean> = _openBuildModeChoseDialog

    //打印机连接方式
    private val _connectMode = MutableLiveData(ConnectMode.NONE)
    var connectMode:LiveData<ConnectMode> = _connectMode

    //打印机指令方式
    private val _printerMode = MutableLiveData(PrinterMode.NONE)
    var printerMode:LiveData<PrinterMode> = _printerMode

    //构建指令方式
    private val _buildMode = MutableLiveData(BuildMode.NONE)
    var buildMode:LiveData<BuildMode> = _buildMode

    private val _usbDevicesList = MutableLiveData(mutableListOf<UsbDevice>())
    var usbDevicesList:LiveData<MutableList<UsbDevice>> = _usbDevicesList

    private val _choseUSBPrinter = MutableLiveData<UsbDevice?>(null)
    var choseUSBPrinter:LiveData<UsbDevice?> = _choseUSBPrinter

    fun openPrinterChoseDialog(){
        _openPrinterChoseDialog.value = true
    }

    fun closePrinterChoseDialog(){
        _openPrinterChoseDialog.value = false
    }

    fun notifyUsbDevicesList(list:List<UsbDevice>){
        _usbDevicesList.value = list.toMutableList()
    }

    fun notifyChoseUSBDevice(usbDevice: UsbDevice) {
        _choseUSBPrinter.value = usbDevice
    }

    fun closeConnectModeChoseDialog(){
        _openConnectModeChoseDialog.value = false
    }

    fun openConnectModeChoseDialog(){
        _openConnectModeChoseDialog.value = true
    }

    fun closePrinterModeChoseDialog(){
        _openPrinterModeChoseDialog.value = false
    }

    fun openPrinterModeChoseDialog() {
        _openPrinterModeChoseDialog.value = true
    }

    fun notifyConnectMode(mode:ConnectMode){
        _connectMode.value = mode
    }

    fun notifyPrinterMode(mode: PrinterMode) {
        _printerMode.value = mode
    }

    fun openBuildModeChoseDialog() {
        _openBuildModeChoseDialog.value = true
    }

    fun closeBuildModeChoseDialog() {
        _openBuildModeChoseDialog.value = false
    }
}