package com.eiviayw.universalprinter.viewMode

import android.bluetooth.BluetoothDevice
import android.hardware.usb.UsbDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eiviayw.print.base.BasePrinter
import com.eiviayw.print.gprinter.EscUsbGPrinter
import com.eiviayw.print.gprinter.TscUsbGPrinter
import com.eiviayw.universalprinter.BaseApplication
import com.eiviayw.universalprinter.bean.BuildMode
import com.eiviayw.universalprinter.bean.ConnectMode
import com.eiviayw.universalprinter.bean.PrinterMode
import com.eiviayw.universalprinter.bean.SDKMode

class MainViewMode:ViewModel() {
    //<editor-fold desc="弹窗状态控制">
    private val _openCreatePrinterView = MutableLiveData(false)
    var openCreatePrinterView:LiveData<Boolean> = _openCreatePrinterView

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
    private val _openSDKModeChoseDialog = MutableLiveData(false)
    var openSDKModeChoseDialog:LiveData<Boolean> = _openSDKModeChoseDialog
    //</editor-fold desc="弹窗状态控制">

    //<editor-fold desc="打印机参数">
    //打印机连接方式
    private val _connectMode = MutableLiveData(ConnectMode.NONE)
    var connectMode:LiveData<ConnectMode> = _connectMode

    //打印机指令方式
    private val _printerMode = MutableLiveData(PrinterMode.NONE)
    var printerMode:LiveData<PrinterMode> = _printerMode

    //构建指令方式
    private val _buildMode = MutableLiveData(BuildMode.NONE)
    var buildMode:LiveData<BuildMode> = _buildMode

    //SDK策略
    private val _sdkMode = MutableLiveData<SDKMode>(SDKMode.NONE)
    var sdkMode:LiveData<SDKMode> = _sdkMode
    //</editor-fold desc="打印机参数">

    //<editor-fold desc="USB相关">
    //Usb设备列表
    private val _usbDevicesList = MutableLiveData(mutableListOf<UsbDevice>())
    var usbDevicesList:LiveData<MutableList<UsbDevice>> = _usbDevicesList

    //选择的Usb设备
    private val _choseUSBPrinter = MutableLiveData<UsbDevice?>(null)
    var choseUSBPrinter:LiveData<UsbDevice?> = _choseUSBPrinter

    //</editor-fold desc="USB相关">

    //<editor-fold desc="蓝牙相关">
    private val _bleDevicesSet = MutableLiveData<MutableSet<BluetoothDevice>>(mutableSetOf())
    var bleDeviceSet:LiveData<MutableSet<BluetoothDevice>> = _bleDevicesSet
    //</editor-fold desc="蓝牙相关">

    //打印机列表
    private val _printerList = MutableLiveData<MutableList<BasePrinter>>(mutableListOf())
    var printerList:LiveData<MutableList<BasePrinter>> = _printerList

    //<editor-fold desc="视图控制">
    /**
     * 显示创建打印机视图
     */
    fun openCreatePrinterView(){
        _openCreatePrinterView.value = true
    }

    /**
     * 隐藏打印机视图
     */
    fun closeCreatePrinterView(){
        _openCreatePrinterView.value = false
    }

    /**
     * 打开SDK选择策略弹窗
     */
    fun openSDKModeChoseDialog(){
        _openSDKModeChoseDialog.value = true
    }

    /**
     * 关闭SDK选择策略弹窗
     */
    fun closeSDKModeChoseDialog(){
        _openSDKModeChoseDialog.value = false
    }

    /**
     * 打开打印机选择弹窗
     */
    fun openPrinterChoseDialog(){
        _openPrinterChoseDialog.value = true
    }

    /**
     * 关闭打印机选择弹窗
     */
    fun closePrinterChoseDialog(){
        _openPrinterChoseDialog.value = false
    }

    /**
     * 关闭连接模式弹窗
     */
    fun closeConnectModeChoseDialog(){
        _openConnectModeChoseDialog.value = false
    }

    /**
     * 打开连接模式弹窗
     */
    fun openConnectModeChoseDialog(){
        _openConnectModeChoseDialog.value = true
    }

    /**
     * 关闭打印模式弹窗
     */
    fun closePrinterModeChoseDialog(){
        _openPrinterModeChoseDialog.value = false
    }

    /**
     * 打开打印模式弹窗
     */
    fun openPrinterModeChoseDialog() {
        _openPrinterModeChoseDialog.value = true
    }
    //</editor-fold desc="视图控制">

    //<editor-fold desc="参数设置">
    /**
     * 获取USB设备列表
     */
    fun notifyUsbDevicesList(list:List<UsbDevice>){
        _usbDevicesList.value = list.toMutableList()
    }

    /**
     * 设置已选中的USB设备
     */
    fun notifyChoseUSBDevice(usbDevice: UsbDevice) {
        _choseUSBPrinter.value = usbDevice
    }

    /**
     * 设置连接模式
     */
    fun notifyConnectMode(mode:ConnectMode){
        _connectMode.value = mode
    }

    /**
     * 设置打印模式
     */
    fun notifyPrinterMode(mode: PrinterMode) {
        _printerMode.value = mode
    }

    /**
     * 设置SDK策略
     */
    fun notifySDKMode(mode:SDKMode){
        _sdkMode.value = mode
    }

    /**
     * 获取SDK策略列表
     */
    fun getSDKModeList():List<SDKMode>{
        return if (_printerMode.value == PrinterMode.ESC){
            getESCSDK()
        }else{
            getTscSDK()
        }
    }

    /**
     * 获取ESC SDK策略列表
     */
    private fun getESCSDK():List<SDKMode>{
        return mutableListOf<SDKMode>().apply {
            add(SDKMode.GPrinter)
            add(SDKMode.EpsonESC)
            if (_connectMode.value == ConnectMode.USB){
                add(SDKMode.NativeUsb)
            }
        }
    }

    /**
     * 获取TSC SDK策略列表
     */
    private fun getTscSDK():List<SDKMode>{
        return mutableListOf<SDKMode>().apply {
            add(SDKMode.GPrinter)
            add(SDKMode.BixolonTsc)
            if (_connectMode.value == ConnectMode.USB){
                add(SDKMode.NativeUsb)
            }
        }
    }

    //</editor-fold desc="参数设置">

    /**
     * 保存参数创建打印机
     */
    fun createPrinter(){
        when(connectMode.value){
            ConnectMode.BLE -> {
                //TODO
            }
            ConnectMode.USB -> {
                choseUSBPrinter.value?.let { createUSBPrinter(it) }
            }
            ConnectMode.WIFI -> {
                //TODO
            }
            else -> {
                //TODO
            }
        }
    }

    private fun createUSBPrinter(usbDevice: UsbDevice){
        when(_printerMode.value){
            PrinterMode.TSC ->{
                TscUsbGPrinter(
                    BaseApplication.getInstance(),
                    usbDevice.vendorId,
                    usbDevice.productId
                )
            }
            PrinterMode.ESC -> {
                EscUsbGPrinter(
                    BaseApplication.getInstance(),
                    usbDevice.vendorId,
                    usbDevice.productId
                )
            }
            else -> null
        }?.let {
            _printerList.value = getTempPrinter(it)
            closeCreatePrinterView()
            reSetParam()
        }
    }

    private fun getTempPrinter(newPrinter:BasePrinter):MutableList<BasePrinter>{
        return mutableListOf<BasePrinter>().apply {
            _printerList.value?.forEach { it->
                add(it)
            }
            add(newPrinter)
        }
    }

    private fun reSetParam(){
        _connectMode.value = ConnectMode.NONE
        _printerMode.value = PrinterMode.NONE
        _sdkMode.value = SDKMode.NONE
        _choseUSBPrinter.value = null
    }
}