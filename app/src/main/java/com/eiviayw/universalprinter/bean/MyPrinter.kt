package com.eiviayw.universalprinter.bean

import android.hardware.usb.UsbDevice
import com.eiviayw.universalprinter.constant.ConnectMode
import com.eiviayw.universalprinter.constant.PrinterMode
import com.eiviayw.universalprinter.constant.SDKMode

data class MyPrinter(
    var name:String = "",//打印机名称
    var id:String = "",//打印机id
    var connectMode: ConnectMode = ConnectMode.USB,//连接方式
    var printerMode: PrinterMode = PrinterMode.ESC,//打印机模式
    var sdkMode: SDKMode = SDKMode.GPrinter,//SDK模式
    var address:String = "",// 局域网地址或蓝牙地址
    var usbDevice: UsbDevice? = null,//USB设备

    var times:String = "1",//打印份数
)
