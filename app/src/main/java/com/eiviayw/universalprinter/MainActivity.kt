package com.eiviayw.universalprinter

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.eiviayw.print.base.BasePrinter
import com.eiviayw.universalprinter.bean.ConnectMode
import com.eiviayw.universalprinter.dialog.ConnectModeDialog
import com.eiviayw.universalprinter.ui.theme.UniversalPrinterTheme
import com.eiviayw.universalprinter.util.BlueToothBroadcastReceiver
import com.eiviayw.universalprinter.util.BlueToothHelper
import com.eiviayw.universalprinter.util.PermissionUtil
import com.eiviayw.universalprinter.util.UsbBroadcastReceiver
import com.eiviayw.universalprinter.viewMode.MainViewMode
import com.eiviayw.universalprinter.views.ComItemOption
import com.eiviayw.universalprinter.views.ComTopBar
import com.eiviayw.universalprinter.views.ComVerticalLine
import com.eiviayw.universalprinter.views.EmptyView

class MainActivity : ComponentActivity() {
    private val viewMode by lazy { ViewModelProvider(this)[MainViewMode::class.java] }
    private val usbBroadcastReceiver by lazy { UsbBroadcastReceiver(this) }
    private val blueToothBroadcastReceiver by lazy { BlueToothBroadcastReceiver(this) }
    private val blueToothHelper by lazy { BlueToothHelper(this) }

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode  == RESULT_OK) {
            blueToothHelper.discoveryBleDevice()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniversalPrinterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Body(viewMode)
                    }
                }
            }
        }

        initEven()
    }

    private fun initEven(){
        viewMode.apply {
            openPrinterChoseDialog.observe(this@MainActivity){
                if (it){
                    when (connectMode.value) {
                        ConnectMode.USB -> {
                            val usbDevices = usbBroadcastReceiver.getUsbDevices()
                            viewMode.notifyUsbDevicesList(usbDevices)
                        }
                        ConnectMode.BLE -> {
                            findDeviceByBlueTooth()
                        }
                        else -> {
                            Log.d(TAG, "openPrinterChoseDialog connectMode.value is other")
                        }
                    }
                }else{
                    when (connectMode.value) {
                        ConnectMode.USB -> {
//                            usbBroadcastReceiver.onDestroy()
                        }
                        ConnectMode.BLE -> {
                            val result = blueToothHelper.stopDiscovery()
                            Log.d(TAG, "blueToothHelper.stopDiscovery：$result")
                        }
                        else -> {
                            Log.d(TAG, "openPrinterChoseDialog connectMode.value is other")
                        }
                    }
                }

            }
        }

        usbBroadcastReceiver.setOnUsbReceiveListener(object :UsbBroadcastReceiver.OnUsbReceiveListener{
            override fun onUsbAttached(intent: Intent) {

            }

            override fun onUsbDetached(intent: Intent) {
            }

            override fun onUsbPermission(intent: Intent) {
            }
        })

        blueToothBroadcastReceiver.setOnBleToothBroadcastListener {
            viewMode.notifyBleDevicesSet(it)
        }
    }

    private fun findDeviceByBlueTooth(){
        val result = PermissionUtil.getInstance().checkPermission(this, mutableListOf<String>().apply {
            add(Manifest.permission.BLUETOOTH)
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            PermissionUtil.getInstance().getPermissionFromSDKVersionS()
        },REQUEST_ENABLE_BT)
        if (!result){
            return
        }

        if (blueToothHelper.needRequestEnableBle()){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            resultLauncher.launch(enableBtIntent)
        }

        val startDiscovery = blueToothHelper.discoveryBleDevice()
        Log.d(TAG, "startDiscovery: $startDiscovery")
    }

    override fun onStart() {
        super.onStart()
        blueToothBroadcastReceiver.onReceive()
        usbBroadcastReceiver.onRegister()
    }

    override fun onStop() {
        super.onStop()
        blueToothBroadcastReceiver.onDestroy()
        usbBroadcastReceiver.onDestroy()
        blueToothHelper.stopDiscovery()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被用户同意，可以操作蓝牙
                findDeviceByBlueTooth()
            } else {
                // 权限被用户拒绝，需要提示用户或者自动回退
                Toast.makeText(this, "蓝牙权限被拒绝，无法使用蓝牙功能", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        private const val TAG = "MainActivity"
        private const val REQUEST_ENABLE_BT = 0x0A1
    }
}

@Composable
fun Body(
    viewMode: MainViewMode
) {
    var createState:State<Boolean> = viewMode.openCreatePrinterView.observeAsState(false)
    val printerList = viewMode.printerList.collectAsState(initial = emptyList())

    Column {
        ComTopBar(
            title = "打印助手",
            actionTitle = if (createState.value) "返回" else "创建",
            onActionClick = {
                if (createState.value){
                    viewMode.closeCreatePrinterView()
                }else{
                    viewMode.openCreatePrinterView()
                }
            }
        )
        Row {
            PrinterListView(modifier = Modifier.weight(3f),viewMode)
            if (printerList.value.isNotEmpty() || createState.value){
                if (createState.value){
                    ComVerticalLine()
                    CreatePrinterView(
                        modifier = Modifier.weight(7f),
                        viewMode = viewMode
                    )
                }
            }else{
                EmptyView(tips = "点击右上角【创建】打印机开始体验吧~~")
            }
        }
    }


}

@Composable
fun PrinterListView(
    modifier: Modifier = Modifier,
    viewMode: MainViewMode = MainViewMode()
) {
    val printerList = viewMode.printerList.collectAsState(initial = emptyList())
    Column(
        modifier
    ) {
        if (printerList.value.isEmpty()) {
            Log.d("MainActivity", "PrinterListView: printerList isEmpty")
            EmptyView(tips = "尚未添加打印机")
        } else {
            for (index in 0 until  printerList.value.size) {
                ComItemOption(
                    title = "打印机$index",
                    value = "",
                    onClick = {
                    }
                )
            }
        }
    }
}
