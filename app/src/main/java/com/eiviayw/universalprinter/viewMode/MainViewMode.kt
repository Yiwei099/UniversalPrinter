package com.eiviayw.universalprinter.viewMode

import android.bluetooth.BluetoothDevice
import android.hardware.usb.UsbDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eiviayw.library.draw.BitmapOption
import com.eiviayw.print.base.BasePrinter
import com.eiviayw.print.bean.mission.GraphicMission
import com.eiviayw.print.gprinter.EscBtGPrinter
import com.eiviayw.print.gprinter.EscNetGPrinter
import com.eiviayw.print.gprinter.EscUsbGPrinter
import com.eiviayw.print.gprinter.TscBtGPrinter
import com.eiviayw.print.gprinter.TscNetGPrinter
import com.eiviayw.print.gprinter.TscUsbGPrinter
import com.eiviayw.print.native.NativeUsbPrinter
import com.eiviayw.universalprinter.BaseApplication
import com.eiviayw.universalprinter.bean.BuildMode
import com.eiviayw.universalprinter.bean.ConnectMode
import com.eiviayw.universalprinter.bean.PaperMode
import com.eiviayw.universalprinter.bean.PrinterMode
import com.eiviayw.universalprinter.bean.SDKMode
import com.eiviayw.universalprinter.provide.EscDataProvide
import com.gprinter.utils.Command
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewMode : ViewModel() {
    //<editor-fold desc="弹窗状态控制">
    private val _openCreatePrinterView = MutableLiveData(false)
    var openCreatePrinterView: LiveData<Boolean> = _openCreatePrinterView

    //打印机连接方式弹窗状态
    private val _openConnectModeChoseDialog = MutableLiveData(false)
    var openConnectModeChoseDialog: LiveData<Boolean> = _openConnectModeChoseDialog

    //打印机指令模式弹窗状态
    private val _openPrinterModeChoseDialog = MutableLiveData(false)
    var openPrinterModeChoseDialog: LiveData<Boolean> = _openPrinterModeChoseDialog

    //打印机设备列表弹窗状态
    private val _openPrinterChoseDialog = MutableLiveData(false)
    var openPrinterChoseDialog: LiveData<Boolean> = _openPrinterChoseDialog

    //指令构建方式弹窗状态
    private val _openSDKModeChoseDialog = MutableLiveData(false)
    var openSDKModeChoseDialog: LiveData<Boolean> = _openSDKModeChoseDialog
    //</editor-fold desc="弹窗状态控制">

    //<editor-fold desc="打印机参数">
    //打印机连接方式
    private val _connectMode = MutableLiveData(ConnectMode.NONE)
    var connectMode: LiveData<ConnectMode> = _connectMode

    //打印机指令方式
    private val _printerMode = MutableLiveData(PrinterMode.NONE)
    var printerMode: LiveData<PrinterMode> = _printerMode

    //构建指令方式
    private val _buildMode = MutableLiveData(BuildMode.NONE)
    var buildMode: LiveData<BuildMode> = _buildMode

    //SDK策略
    private val _sdkMode = MutableLiveData<SDKMode>(SDKMode.NONE)
    var sdkMode: LiveData<SDKMode> = _sdkMode
    //</editor-fold desc="打印机参数">

    //<editor-fold desc="USB相关">
    //Usb设备列表
    private val _usbDevicesList = MutableLiveData(mutableListOf<UsbDevice>())
    var usbDevicesList: LiveData<MutableList<UsbDevice>> = _usbDevicesList

    //选择的Usb设备
    private val _choseUSBPrinter = MutableLiveData<UsbDevice?>(null)
    var choseUSBPrinter: LiveData<UsbDevice?> = _choseUSBPrinter

    //</editor-fold desc="USB相关">

    //<editor-fold desc="蓝牙相关">
    private val _bleDevicesSet = MutableStateFlow<Set<BluetoothDevice>>(emptySet())
    val bleDevicesSet = _bleDevicesSet.asStateFlow()

    //选择的BLe设备
    private val _choseBlePrinter = MutableLiveData<BluetoothDevice?>(null)
    var choseBlePrinter: LiveData<BluetoothDevice?> = _choseBlePrinter
    //</editor-fold desc="蓝牙相关">

    //打印机列表
    private val _printerList = MutableStateFlow<MutableList<BasePrinter>>(mutableListOf())
    val printerList = _printerList.asStateFlow()

    private val _choosePrinter = MutableLiveData<BasePrinter?>(null)
    var choosePrinter:LiveData<BasePrinter?> = _choosePrinter

    private val _isModifyPrinter = MutableLiveData(false)
    var isModifyPrinter: LiveData<Boolean> = _isModifyPrinter

    //<editor-fold desc="视图控制">
    fun openStartPrintView(printer: BasePrinter?) {
        _choosePrinter.value = printer
    }

    /**
     * 显示创建打印机视图
     */
    fun openCreatePrinterView() {
        _openCreatePrinterView.value = true
    }

    /**
     * 隐藏打印机视图
     */
    fun closeCreatePrinterView() {
        _openCreatePrinterView.value = false
    }

    /**
     * 打开SDK选择策略弹窗
     */
    fun openSDKModeChoseDialog() {
        _openSDKModeChoseDialog.value = true
    }

    /**
     * 关闭SDK选择策略弹窗
     */
    fun closeSDKModeChoseDialog() {
        _openSDKModeChoseDialog.value = false
    }

    /**
     * 打开打印机选择弹窗
     */
    fun openPrinterChoseDialog() {
        _openPrinterChoseDialog.value = true
    }

    /**
     * 关闭打印机选择弹窗
     */
    fun closePrinterChoseDialog() {
        _openPrinterChoseDialog.value = false
        //清空缓存
        when (_connectMode.value) {
            ConnectMode.BLE -> _bleDevicesSet.value = emptySet<BluetoothDevice>()
            ConnectMode.USB -> _usbDevicesList.value = mutableListOf()
            else -> {}
        }
    }

    /**
     * 关闭连接模式弹窗
     */
    fun closeConnectModeChoseDialog() {
        _openConnectModeChoseDialog.value = false
    }

    /**
     * 打开连接模式弹窗
     */
    fun openConnectModeChoseDialog() {
        _openConnectModeChoseDialog.value = true
    }

    /**
     * 关闭打印模式弹窗
     */
    fun closePrinterModeChoseDialog() {
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
    fun notifyBleDevicesSet(device: BluetoothDevice) {
        _bleDevicesSet.value += setOf(device)
    }

    /**
     * 获取USB设备列表
     */
    fun notifyUsbDevicesList(list: List<UsbDevice>) {
        _usbDevicesList.value = list.toMutableList()
    }

    /**
     * 设置已选中的USB设备
     */
    fun notifyChoseUSBDevice(usbDevice: UsbDevice) {
        _choseUSBPrinter.value = usbDevice
    }

    /**
     * 设置已选中的Ble设备
     */
    fun notifyChoseBleDevice(usbDevice: BluetoothDevice) {
        _choseBlePrinter.value = usbDevice
    }

    /**
     * 设置连接模式
     */
    fun notifyConnectMode(mode: ConnectMode) {
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
    fun notifySDKMode(mode: SDKMode) {
        _sdkMode.value = mode
    }

    /**
     * 获取SDK策略列表
     */
    fun getSDKModeList(): List<SDKMode> {
        return if (_printerMode.value == PrinterMode.ESC) {
            getESCSDK()
        } else {
            getTscSDK()
        }
    }

    /**
     * 获取ESC SDK策略列表
     */
    private fun getESCSDK(): List<SDKMode> {
        return mutableListOf<SDKMode>().apply {
            add(SDKMode.GPrinter)
            add(SDKMode.EpsonESC)
            if (_connectMode.value == ConnectMode.USB) {
                add(SDKMode.NativeUsb)
            }
        }
    }

    /**
     * 获取TSC SDK策略列表
     */
    private fun getTscSDK(): List<SDKMode> {
        return mutableListOf<SDKMode>().apply {
            add(SDKMode.GPrinter)
            add(SDKMode.BixolonTsc)
            if (_connectMode.value == ConnectMode.USB) {
                add(SDKMode.NativeUsb)
            }
        }
    }

    //</editor-fold desc="参数设置">

    /**
     * 保存参数创建打印机
     */
    fun createPrinter(idAddress:String = "") {
        if (_isModifyPrinter.value == true){
            _choosePrinter.value?.let { deletePrinter(it) }
            openStartPrintView(null)
        }

        when (connectMode.value) {
            ConnectMode.BLE -> choseBlePrinter.value?.let { createBlePrinter(it) }
            ConnectMode.USB -> choseUSBPrinter.value?.let { createUSBPrinter(it) }
            ConnectMode.WIFI -> createNetPrinter(idAddress)
            else -> {
                //TODO
            }
        }
    }

    private fun createNetPrinter(address:String){
        when(_printerMode.value){
            PrinterMode.TSC -> TscNetGPrinter(BaseApplication.getInstance(), address)
            PrinterMode.ESC -> EscNetGPrinter(BaseApplication.getInstance(), address)
            else -> null
        }?.let {
            _printerList.value = mutableListOf<BasePrinter>().apply {
                addAll(_printerList.value)
                add(it)
            }
            closeCreatePrinterView()
            reSetParam()
        }
    }

    /**
     * 创建蓝牙打印机
     */
    private fun createBlePrinter(device: BluetoothDevice) {
        when (_printerMode.value) {
            PrinterMode.TSC -> TscBtGPrinter(BaseApplication.getInstance(), device.address)
            PrinterMode.ESC -> EscBtGPrinter(BaseApplication.getInstance(), device.address)
            else -> null
        }?.let {
            _printerList.value = mutableListOf<BasePrinter>().apply {
                addAll(_printerList.value)
                add(it)
            }
            closeCreatePrinterView()
            reSetParam()
        }
    }

    /**
     * 创建USB打印机
     */
    private fun createUSBPrinter(usbDevice: UsbDevice) {
        when (_printerMode.value) {
            PrinterMode.TSC -> {
                TscUsbGPrinter(
                    BaseApplication.getInstance(),
                    usbDevice.vendorId,
                    usbDevice.productId
                )
            }

            PrinterMode.ESC -> {
                when(sdkMode.value){
                    SDKMode.NativeUsb -> {
                        NativeUsbPrinter(BaseApplication.getInstance(),usbDevice,Command.ESC)
                    }
                    else -> {
                        EscUsbGPrinter(
                            BaseApplication.getInstance(),
                            usbDevice.vendorId,
                            usbDevice.productId
                        )
                    }
                }

            }

            else -> null
        }?.let {
            _printerList.value = mutableListOf<BasePrinter>().apply {
                addAll(_printerList.value)
                add(it)
            }
            closeCreatePrinterView()
            reSetParam()
        }
    }

    /**
     * 重置参数
     */
    private fun reSetParam() {
        _connectMode.value = ConnectMode.NONE
        _printerMode.value = PrinterMode.NONE
        _sdkMode.value = SDKMode.NONE
        _choseUSBPrinter.value = null
        _choseBlePrinter.value = null
        _isModifyPrinter.value = false
    }

    /**
     * 开始打印
     */
    fun startPrint(printer:BasePrinter,isEsc:Boolean,times:Int = 1, startIndex:String, topIndex:String, paperSize: PaperMode, buildMode:BuildMode){
        var data:ByteArray? = null
        if (isEsc){
            val bitmapOption = when (paperSize) {
                PaperMode.ESC_58 -> {
                    BitmapOption(maxWidth = 384)
                }
                PaperMode.ESC_80 -> {
                    BitmapOption()
                }
                else -> {
                    BitmapOption()
                }
            }
            data = EscDataProvide(bitmapOption).getData()
        }

        data?.let {
            printer.addMission(GraphicMission(data).apply {
                count = times
            })
        }
    }

    fun onDestroyPrinter(printer:BasePrinter){
        printer.onDestroy()
    }

    fun deletePrinter(printer: BasePrinter){
        printer.onDestroy()
        val iterator = _printerList.value.iterator()
        while(iterator.hasNext()){
            if (iterator.next() == printer){
                iterator.remove()
                openStartPrintView(null)
            }
        }
    }

    fun modifyPrinter(printer: BasePrinter){
        _isModifyPrinter.value = true
        //打开视图
        openCreatePrinterView()
        when(printer){
            is EscBtGPrinter -> {
                _connectMode.value = ConnectMode.BLE
                _printerMode.value = PrinterMode.ESC
                _sdkMode.value = SDKMode.GPrinter
            }
            is EscUsbGPrinter -> {
                _connectMode.value = ConnectMode.USB
                _printerMode.value = PrinterMode.ESC
                _sdkMode.value = SDKMode.GPrinter
            }
            is NativeUsbPrinter -> {
                _connectMode.value = ConnectMode.USB
                _printerMode.value = PrinterMode.ESC
                _sdkMode.value = SDKMode.NativeUsb
            }
        }
    }
}