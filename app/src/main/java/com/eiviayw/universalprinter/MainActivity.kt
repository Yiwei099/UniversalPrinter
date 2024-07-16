package com.eiviayw.universalprinter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.eiviayw.universalprinter.dialog.ConnectModeDialog
import com.eiviayw.universalprinter.ui.theme.UniversalPrinterTheme
import com.eiviayw.universalprinter.util.UsbBroadcastReceiver
import com.eiviayw.universalprinter.viewMode.MainViewMode
import com.eiviayw.universalprinter.views.ComItemOption
import com.eiviayw.universalprinter.views.ComTopBar
import com.eiviayw.universalprinter.views.ComVerticalLine
import com.eiviayw.universalprinter.views.EmptyView

class MainActivity : ComponentActivity() {
    private val viewMode by lazy { ViewModelProvider(this)[MainViewMode::class.java] }
    private val usbBroadcastReceiver by lazy { UsbBroadcastReceiver(this) }

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
                val usbDevices = usbBroadcastReceiver.getUsbDevices()
                viewMode.notifyUsbDevicesList(usbDevices)
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
    }

    override fun onStart() {
        super.onStart()
        usbBroadcastReceiver.onRegister()
    }

    override fun onStop() {
        super.onStop()
        usbBroadcastReceiver.onDestroy()
    }
}

@Composable
fun Body(
    viewMode: MainViewMode
) {
    var createState by remember { mutableStateOf(false) }

    Column {
        ComTopBar(
            title = "打印助手",
            actionTitle = if (createState) "返回" else "创建",
            onActionClick = { createState = !createState }
        )
        Row {
            PrinterListView(modifier = Modifier.weight(3f))
            if (createState){
                ComVerticalLine()
                CreatePrinterView(
                    modifier = Modifier.weight(7f),
                    viewMode = viewMode
                )
            }else{
                EmptyView(tips = "点击右上角【创建】打印机开始体验吧~~")
            }
        }
    }


}

@Composable
fun PrinterListView(modifier: Modifier = Modifier) {
    val printerList = mutableListOf<String>().apply {
//        add("打印机1")
//        add("打印机2")
//        add("打印机3")
//        add("打印机4")
//        add("打印机5")
    }
    Column(
        modifier
    ) {
        if (printerList.isEmpty()) {
            EmptyView(tips = "尚未添加打印机")
        } else {
            for (printer in printerList) {
                ComItemOption(title = printer, value = "周杰伦", onClick = {
                })
            }
        }

    }
}
