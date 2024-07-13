package com.eiviayw.universalprinter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eiviayw.universalprinter.bean.ConnectMode
import com.eiviayw.universalprinter.bean.PrinterMode
import com.eiviayw.universalprinter.dialog.ConnectModeDialog
import com.eiviayw.universalprinter.dialog.PrinterModeDialog
import com.eiviayw.universalprinter.viewMode.MainViewMode
import com.eiviayw.universalprinter.views.ComItemOption

/**
 * 创建打印机
 */
@Composable
fun CreatePrinterView(
    modifier: Modifier,
) {
    //打印机连接方式
    var connectMode by remember { mutableStateOf(ConnectMode.NONE) }
    //打印机指令模式
    var printerMode by remember { mutableStateOf(PrinterMode.NONE) }
    //选择打印机连接方式弹窗状态
    var openModeChoseDialog by remember { mutableStateOf(false) }

    var openPrinterModeChoseDialog by remember { mutableStateOf(false) }

    var havePrinter = false

    Column(modifier = modifier.padding(0.dp,0.dp,20.dp,0.dp)) {
        StepOption(
            Modifier.padding(0.dp,10.dp),
            stepTitle = "1. 选择连接方式",
            stepTips = "连接方式",
            value = connectMode.label){
            openModeChoseDialog = true
        }

        if (connectMode != ConnectMode.NONE){
            StepOption(
                modifier = Modifier.padding(0.dp,10.dp),
                stepTitle = "2. 选择打印模式",
                stepTips = "打印模式",
                value = printerMode.label){
                openPrinterModeChoseDialog = true
            }
        }

        if (printerMode != PrinterMode.NONE){
            val tips = when(connectMode){
                ConnectMode.BLE -> "蓝牙"
                ConnectMode.USB -> "USB"
                ConnectMode.WIFI -> "Wi-fi"
                else -> "null"
            }
            StepOption(
                modifier = Modifier.padding(0.dp,10.dp),
                stepTitle = "3. 选择 $tips 打印机",
                stepTips = "打印机",
                value = connectMode.label){
                havePrinter = true
            }
        }
    }

    if (openModeChoseDialog) {
        Dialog(onDismissRequest = { openModeChoseDialog = !openModeChoseDialog }) {
            ConnectModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = connectMode,
                cancel = {
                    openModeChoseDialog = !openModeChoseDialog
                },
                confirm = {
                    connectMode = it
                    openModeChoseDialog = !openModeChoseDialog
                }
            )
        }
    }

    if (openPrinterModeChoseDialog) {
        Dialog(onDismissRequest = { openPrinterModeChoseDialog = !openPrinterModeChoseDialog }) {
            PrinterModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = printerMode,
                cancel = {
                    openPrinterModeChoseDialog = !openPrinterModeChoseDialog
                },
                confirm = {
                    printerMode = it
                    openPrinterModeChoseDialog = !openPrinterModeChoseDialog
                }
            )
        }
    }
}

@Composable
fun StepOption(
    modifier: Modifier = Modifier,
    stepTips:String = "",
    stepTitle:String = "",
    value:String = "",
    onClick:() -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Text(text = stepTips)
        ComItemOption(title = stepTitle, value = value,onClick = onClick)
    }
}

