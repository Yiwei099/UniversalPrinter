package com.eiviayw.universalprinter.viewMode

import android.bluetooth.BluetoothDevice
import android.hardware.usb.UsbDevice
import androidx.lifecycle.ViewModel
import com.eiviayw.universalprinter.bean.MyPrinter
import com.eiviayw.universalprinter.bean.state.ViewState
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

    companion object{
        private const val TAG = "MyViewModel"
    }
}