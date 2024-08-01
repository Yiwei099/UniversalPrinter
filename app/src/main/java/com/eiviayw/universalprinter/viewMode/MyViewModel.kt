package com.eiviayw.universalprinter.viewMode

import android.bluetooth.BluetoothDevice
import android.hardware.usb.UsbDevice
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.eiviayw.universalprinter.bean.MyPrinter
import com.eiviayw.universalprinter.bean.state.ViewState
import com.eiviayw.universalprinter.util.StringUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyViewModel:ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    private val _myPrinter = MutableStateFlow<MyPrinter>(MyPrinter())
    val myPrinter:StateFlow<MyPrinter> = _myPrinter

    //已设置的打印机列表
    private val _myPrinterList = MutableStateFlow<MutableList<MyPrinter>>(mutableListOf())
    val myPrinterList:StateFlow<MutableList<MyPrinter>> = _myPrinterList

    //蓝牙设备列表
    private val _bleDevicesSet = MutableStateFlow<Set<BluetoothDevice>>(emptySet())
    val bleDevicesSet = _bleDevicesSet.asStateFlow()

    //Usb设备列表
    private val _usbDevicesList = MutableStateFlow<Set<UsbDevice>>(emptySet())
    var usbDevicesList = _usbDevicesList.asStateFlow()

    private val _startPrintPrinter = MutableStateFlow<MyPrinter?>(null)
    val startPrintPrinter:StateFlow<MyPrinter?> = _startPrintPrinter

    /**
     * 显示编辑打印机视图
     */
    fun showModifyPrinterView(show:Boolean){
        _viewState.value = _viewState.value.copy(showModifyPrinterView = show)
    }

    /**
     * 显示绑定打印机视图
     */
    fun showBindPrinterView(show:Boolean){
        _viewState.value = _viewState.value.copy(showBindPrinterView = show)
    }

    /**
     * 显示连接方式选择视图
     */
    fun showConnectModeListDialog(show:Boolean){
        _viewState.value = _viewState.value.copy(showConnectModeListDialog = show)
    }

    /**
     * 显示打印模式选择视图
     */
    fun showPrinterModeListDialog(show:Boolean){
        _viewState.value = _viewState.value.copy(showPrinterModeListDialog = show)
    }

    /**
     * 显示SDK选择视图
     */
    fun showSDKModeListDialog(show:Boolean){
        _viewState.value = _viewState.value.copy(showSDKModeListDialog = show)
    }

    fun showPaperListDialog(show: Boolean){
        _viewState.value = _viewState.value.copy(showPaperListDialog = show)
    }

    fun showBuildListDialog(show: Boolean){
        _viewState.value = _viewState.value.copy(showBuildListDialog = show)
    }

    fun showDeviceListDialog(show:Boolean){
        _viewState.value = _viewState.value.copy(showDeviceListDialog = show)
    }

    fun addBleDevice(device: BluetoothDevice) {
        _bleDevicesSet.value += setOf(device)
    }

    fun addUsbDevice(device: UsbDevice){
        _usbDevicesList.value += listOf(device)
    }

    fun setUsbDevices(device: List<UsbDevice>){
        _usbDevicesList.value = device.toSet()
    }

    fun removeUsbDevice(device: UsbDevice){
        _usbDevicesList.value -= setOf(device)
    }

    fun startPrinter(printer: MyPrinter){
        _startPrintPrinter.value = printer
        showModifyPrinterView(true)
    }

    fun modifyPrinter(printer: MyPrinter){
        _myPrinter.value = printer
    }

    fun deletePrinter(printer: MyPrinter){
        _myPrinterList.value -= setOf(printer)
        _startPrintPrinter.value = null
    }

    fun savePrinter(){
        if (TextUtils.isEmpty(_myPrinter.value.id)){
            _myPrinter.value.apply {
                id = StringUtils.getInstance().getRandomNum(6)
                name = "编号：${id}"
            }

            _myPrinterList.value += listOf(_myPrinter.value)
            _myPrinter.value = MyPrinter()
        }else{
            _myPrinter.value.markDataChange()
        }

        showBindPrinterView(false)
    }

}