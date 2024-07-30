package com.eiviayw.universalprinter.util

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import com.eiviayw.universalprinter.BaseApplication

class BlueToothBroadcastReceiver (private val context: Context): BroadcastReceiver() {

    private var listener:((BluetoothDevice) -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.run {
            when(action){
                BluetoothDevice.ACTION_FOUND->{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        getParcelableExtra(BluetoothDevice.EXTRA_DEVICE,BluetoothDevice::class.java)
                    } else {
                        getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }?.let {
                        listener?.invoke(it)
                    }
                }
                else->{}
            }
        }
    }

    fun removeListener(){
        this.listener = null
    }
    fun setOnBleToothBroadcastListener(listener:(BluetoothDevice) -> Unit){
        this.listener = listener
    }

    fun onReceive() {
        context.registerReceiver(this, IntentFilter(BluetoothDevice.ACTION_FOUND))
    }

    fun onDestroy(){
        context.unregisterReceiver(this)
    }
}