package com.eiviayw.universalprinter.bean.state

import android.hardware.usb.UsbDevice
import com.eiviayw.universalprinter.constant.ConnectMode
import com.eiviayw.universalprinter.constant.PrinterMode
import com.eiviayw.universalprinter.constant.SDKMode

data class CreateState(
    val connectMode:ConnectMode = ConnectMode.NONE,//连接方式
    val printerMode:PrinterMode = PrinterMode.ESC,//打印机模式
    val sdkMode: SDKMode = SDKMode.GPrinter,//SDK模式
    val address:String = "",// 局域网地址或蓝牙地址
    val usbDevice: UsbDevice? = null,//USB设备
)
