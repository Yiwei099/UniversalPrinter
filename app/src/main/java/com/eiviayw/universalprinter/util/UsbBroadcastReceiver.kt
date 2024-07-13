package com.eiviayw.universalprinter.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log

/**
 * Created with Android Studio.
 * @Author: YYW
 * @Date: 2023-12-09 16:43
 * @Version Copyright (c) 2023, Android Engineer YYW All Rights Reserved.
 * description : 通用USB广播管理
 */
class UsbBroadcastReceiver(private val context: Context): BroadcastReceiver() {

    private val usbManager by lazy { context.getSystemService(Context.USB_SERVICE) as UsbManager }

    private var onUsbReceiveListener: OnUsbReceiveListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> onUsbReceiveListener?.onUsbAttached(intent)

            UsbManager.ACTION_USB_DEVICE_DETACHED -> onUsbReceiveListener?.onUsbDetached(intent)

            ACTION_USB_PERMISSION -> onUsbReceiveListener?.onUsbPermission(intent)
            else ->{
                //暂不处理
            }
        }
    }

    fun getUsbService() = usbManager

    fun getUsbDevices():List<UsbDevice>{
        val result = mutableListOf<UsbDevice>()
        val deviceList = usbManager.deviceList
        val iterator = deviceList.iterator()
        while (iterator.hasNext()) {
            val device = iterator.next().value
            val usbInterface = device.getInterface(0).interfaceClass
            if (usbInterface == UsbConstants.USB_CLASS_PRINTER) {
                if (usbManager.hasPermission(device)) {
                    result.add(device)
                } else {
                    requestPermission(device)
                }

            }
        }

        return result
    }

    private fun requestPermission(device: UsbDevice) {
        val mPermissionIntent =
            PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_USB_PERMISSION,
                Intent(ACTION_USB_PERMISSION),
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT
            )
        usbManager.requestPermission(device, mPermissionIntent)
    }

    fun setOnUsbReceiveListener(listener: OnUsbReceiveListener){
        onUsbReceiveListener = listener
    }

    interface OnUsbReceiveListener{
        //USB接入
        fun onUsbAttached(intent: Intent)
        //USB解除
        fun onUsbDetached(intent: Intent)
        //授权
        fun onUsbPermission(intent: Intent)
    }

    fun onRegister(){
        context.registerReceiver(this, IntentFilter(ACTION_USB_PERMISSION).apply {
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        })
        Log.d(TAG, "USB广播被注册啦")
    }

    //销毁广播
    fun onDestroy(){
        context.unregisterReceiver(this)
        Log.d(TAG, "USB广播被销毁啦")
    }

    companion object{
        const val ACTION_USB_PERMISSION = "com.eiviayw.universalprinter.USB_PERMISSION"
        private const val REQUEST_CODE_USB_PERMISSION = 100

        private const val TAG = "UsbBroadcastReceiver"
    }
}