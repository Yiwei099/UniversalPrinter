package com.eiviayw.universalprinter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eiviayw.universalprinter.bean.ConnectMode
import com.eiviayw.universalprinter.bean.PrinterMode
import com.eiviayw.universalprinter.views.ComItemOption

/**
 * 创建打印机
 */
@Composable
fun CreatePrinterView(modifier: Modifier) {
    var mode = ConnectMode.NONE
    var printerMode = PrinterMode.NONE

    var havePrinter = false

    Column(modifier = modifier.padding(0.dp,0.dp,20.dp,0.dp)) {
        StepOption("1. 选择连接方式","连接方式",mode.label){
        }

        if (mode.value != ConnectMode.NONE.value){
            val tips = when(mode.value){
                ConnectMode.BLE.value -> "蓝牙"
                ConnectMode.USB.value -> "USB"
                ConnectMode.WIFI.value -> "Wi-fi"
                else -> "null"
            }
            StepOption("2. 选择${tips}打印机","打印机",mode.label){
                havePrinter = true
            }

        }

        if (havePrinter){
            StepOption("3. 选择打印模式","打印模式",mode.label){
            }
        }
    }
}

@Composable
fun StepOption(stepTips:String,stepTitle:String,value:String,onClick:(() -> Unit)? = null) {
    Column {
        Text(text = stepTips)
        ComItemOption(title = stepTitle, value = value,onClick = onClick)
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePrinterViewPreview() {
    CreatePrinterView(modifier = Modifier)
}