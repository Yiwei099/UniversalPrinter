package com.eiviayw.universalprinter.util

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import com.eiviayw.universalprinter.BaseApplication

class BlueToothBroadcastReceiver(
    private var listener: ((BluetoothDevice) -> Unit)
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            BluetoothDevice.ACTION_FOUND->{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE,BluetoothDevice::class.java)
                } else {
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                }?.let {
                    listener.invoke(it)
                }
            }
            else->{}
        }
    }

    fun onReceive() {
        Log.d(BaseApplication.TAG, "BlueToothBroadcastReceiver onReceive")
        BaseApplication.getInstance().registerReceiver(this, IntentFilter(BluetoothDevice.ACTION_FOUND))
    }

    fun onDestroy(){
        Log.d(BaseApplication.TAG, "BlueToothBroadcastReceiver onDestroy")
        BaseApplication.getInstance().unregisterReceiver(this)
    }
}