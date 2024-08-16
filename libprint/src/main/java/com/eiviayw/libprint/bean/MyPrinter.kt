package com.eiviayw.libprint.bean

import android.hardware.usb.UsbDevice
import android.text.TextUtils
import android.util.Log
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
import com.eiviayw.libcommon.BaseApplication
import com.eiviayw.libprint.constant.BuildMode
import com.eiviayw.libprint.constant.ConnectMode
import com.eiviayw.libprint.constant.DensityMode
import com.eiviayw.libprint.constant.ForWordMode
import com.eiviayw.libprint.constant.PaperMode
import com.eiviayw.libprint.constant.PrinterMode
import com.eiviayw.libprint.constant.SDKMode
import com.gprinter.utils.Command

data class MyPrinter(
    var name:String = "",//打印机名称
    var id:String = "",//打印机id
    var connectMode: ConnectMode = ConnectMode.USB,//连接方式
    var printerMode: PrinterMode = PrinterMode.ESC,//打印机模式
    var sdkMode: SDKMode = SDKMode.GPrinter,//SDK模式
    var address:String = "",// 局域网地址或蓝牙地址
    var usbDevice: UsbDevice? = null,//USB设备

    var times:String = "1",//打印份数
    var startPosition:String = "0",//左侧偏移距离
    var topPosition:String = "0",//顶部偏移距离
    var paperSize: PaperMode = PaperMode.NONE,//纸张尺寸
    var buildMode: BuildMode = BuildMode.Graphic,//构建模式
    var forWordMode: ForWordMode = ForWordMode.NORMAL,//打印方向
    var density: DensityMode = DensityMode.DENSITY_0,//打印浓度

    var supportPrinter:BasePrinter? = null,//打印机实例

    var dataChange:Boolean = true,//参数是否被修改
){

    private var printSourceData:ByteArray? = null
    private var bitmapOption: BitmapOption = BitmapOption()

    private fun build(){
        val usbDevicePass = usbDevice != null
        val addressPass = !TextUtils.isEmpty(address)
        if (isNativeUsb() && usbDevicePass){
            //原生Usb
            supportPrinter = NativeUsbPrinter(
                mContext = BaseApplication.getInstance(),
                usbDevice = usbDevice!!,
                commandType = if (isEsc()) Command.ESC else Command.TSC,
                adjustX = getStartPositionFloat().toInt(),
                adjustY = getTopPositionFloat().toInt(),
                density = density.value
            )

            return
        }

        if (isGPrinter()){
            buildGPrinter(usbDevicePass,addressPass)
            return
        }
    }

    private fun buildGPrinter(usbDevicePass:Boolean = false, addressPass:Boolean = false){
        when(connectMode){
            ConnectMode.USB -> {
                if (usbDevicePass){
                    supportPrinter = if (isEsc()){
                        EscUsbGPrinter(
                            mContext = BaseApplication.getInstance(),
                            vID = usbDevice!!.vendorId,
                            pID = usbDevice!!.productId,
                        )
                    }else{
                        TscUsbGPrinter(
                            mContext = BaseApplication.getInstance(),
                            vID = usbDevice!!.vendorId,
                            pID = usbDevice!!.productId,
                            density = density.value
                        )
                    }
                }else{
                    Log.d(BaseApplication.TAG, "MyPrinter build failure because usbDevice is not found")
                }
            }
            ConnectMode.BLE -> {
                if (addressPass){
                    supportPrinter = if (isEsc()){
                        EscBtGPrinter(
                            mContext = BaseApplication.getInstance(),
                            macAddress = address
                        )
                    }else{
                        TscBtGPrinter(
                            mContext = BaseApplication.getInstance(),
                            macAddress = address,
                            density = density.value
                        )
                    }
                }else{
                    Log.d(BaseApplication.TAG, "MyPrinter build failure because address is empty")
                }
            }
            ConnectMode.WIFI -> {
                if (addressPass){
                    supportPrinter = if (isEsc()){
                        EscNetGPrinter(
                            mContext = BaseApplication.getInstance(),
                            ipAddress = address
                        )
                    }else{
                        TscNetGPrinter(
                            mContext = BaseApplication.getInstance(),
                            ipAddress = address,
                            adjustY = getTopPositionFloat().toInt(),
                            adjustX = getStartPositionFloat().toInt(),
                            density = density.value
                        )
                    }
                }
            }
            else -> {}
        }
    }

    fun startPrint(){
        if (dataChange){
            //数据变更，销毁打印队列
            releasePrinter()
            //重新创建打印机
            build()
            //执行打印
            print()
            dataChange = false
        }else{
            print()
        }
    }

    fun releasePrinter(){
        supportPrinter?.onDestroy()
    }

    private fun print(){
        supportPrinter?.let {
            it.addMission(GraphicMission(printSourceData!!, forward = forWordMode.value,selfAdaptionHeight = paperSize == PaperMode.NONE).apply {
                count = times.toInt()
                countByOne = true
                if (!isEsc()){
                    //佳博标签传值单位是 mm 必须除以8
                    bitmapWidth = bitmapOption.maxWidth / 8
                    bitmapHeight = bitmapOption.maxHeight / 8
                }
            })
        } ?: {
            Log.d(BaseApplication.TAG, "打印机实例已被销毁")
        }
    }

    private fun getStartPositionFloat() = if (TextUtils.isEmpty(startPosition)) 0f else startPosition.toFloat()
    private fun getTopPositionFloat() = if (TextUtils.isEmpty(topPosition)) 0f else topPosition.toFloat()

    fun markDataChange(){
        dataChange = true
    }

    fun isEsc():Boolean{
        var nativeEsc = false
        if (supportPrinter != null){
            if (supportPrinter is NativeUsbPrinter){
                nativeEsc = (supportPrinter as NativeUsbPrinter).getCommandTypeValue() == Command.ESC
            }
        }

        return printerMode == PrinterMode.ESC || nativeEsc
    }

    fun notifyPrinterData(dataSource:ByteArray,option: BitmapOption){
        Log.d("StartPrintView", "StartPrintView: 数据回来啦")
        printSourceData = dataSource
        bitmapOption = option
        markDataChange()
    }

    fun isUsb() = connectMode == ConnectMode.USB
    fun isGPrinter() = sdkMode == SDKMode.GPrinter
    fun isNativeUsb() = sdkMode == SDKMode.NativeUsb
    fun isBt() = connectMode == ConnectMode.BLE
    fun printDataIsNotEmpty() = printSourceData != null
    fun getBitmapOption():BitmapOption {
        bitmapOption = BitmapOption(
            maxWidth = paperSize.width,
            maxHeight = paperSize.heigth,
            startIndentation = if (isEsc()) bitmapOption.startIndentation else getStartPositionFloat(),
            topIndentation = if (isEsc()) bitmapOption.topIndentation else getTopPositionFloat(),
            endIndentation = bitmapOption.endIndentation,
            antiAlias = bitmapOption.antiAlias,
            followEffectItem = bitmapOption.followEffectItem
        )
        return bitmapOption
    }
}
