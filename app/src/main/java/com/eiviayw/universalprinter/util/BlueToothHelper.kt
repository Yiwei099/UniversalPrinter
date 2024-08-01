package com.eiviayw.universalprinter.util

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.eiviayw.universalprinter.BaseApplication


class BlueToothHelper private constructor() {
    companion object {
        @Volatile
        private var instance: BlueToothHelper? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: BlueToothHelper().also { instance = it }
            }
    }

    private val bluetoothManager by lazy { BaseApplication.getInstance().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }

    private val bleAdapter by lazy { bluetoothManager.adapter }

    private fun supportBlueTooth(): Boolean = bleAdapter != null

    private fun enableBle(): Boolean = bleAdapter?.isEnabled ?: false

    fun needRequestEnableBle(): Boolean = supportBlueTooth() && !enableBle()

    fun discoveryBleDevice(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                BaseApplication.getInstance(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        if (!supportBlueTooth()) {
            return false
        }

        if (enableBle()) {
            val result = bleAdapter?.startDiscovery() ?: false
            return result
        }

        return false
    }

    fun stopDiscovery():Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                BaseApplication.getInstance(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return bleAdapter?.cancelDiscovery() ?: false
    }
}