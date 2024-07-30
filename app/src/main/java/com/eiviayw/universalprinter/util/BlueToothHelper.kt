package com.eiviayw.universalprinter.util

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.eiviayw.universalprinter.BaseApplication


class BlueToothHelper(private val context: Context) {

    private val bluetoothManager by lazy { context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }

    private val bleAdapter by lazy { bluetoothManager.adapter }

    fun supportBlueTooth(): Boolean = bleAdapter != null

    fun enableBle(): Boolean = bleAdapter?.isEnabled ?: false

    fun needRequestEnableBle(): Boolean = supportBlueTooth() && !enableBle()

    fun discoveryBleDevice(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                context,
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

    fun stopDiscovery() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bleAdapter?.cancelDiscovery()
    }
}