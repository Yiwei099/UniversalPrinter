package com.eiviayw.universalprinter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eiviayw.universalprinter.bean.ConnectMode
import com.eiviayw.universalprinter.bean.PrinterMode
import com.eiviayw.universalprinter.bean.SDKMode
import com.eiviayw.universalprinter.dialog.BleToothPrinterDialog
import com.eiviayw.universalprinter.dialog.ConnectModeDialog
import com.eiviayw.universalprinter.dialog.PrinterModeDialog
import com.eiviayw.universalprinter.dialog.SDKModeDialog
import com.eiviayw.universalprinter.dialog.UsbPrinterDialog
import com.eiviayw.universalprinter.viewMode.MainViewMode
import com.eiviayw.universalprinter.views.ComButton
import com.eiviayw.universalprinter.views.ComItemOption
import com.eiviayw.universalprinter.views.StepOption

/**
 * 创建打印机
 */
@Composable
fun CreatePrinterView(
    modifier: Modifier,
    viewMode: MainViewMode = MainViewMode()
) {
    val connectMode: State<ConnectMode> = viewMode.connectMode.observeAsState(ConnectMode.NONE)
    val printerMode: State<PrinterMode> = viewMode.printerMode.observeAsState(PrinterMode.NONE)
    val sdkMode: State<SDKMode> = viewMode.sdkMode.observeAsState(SDKMode.NONE)
    val usbDevice = viewMode.choseUSBPrinter.observeAsState()
    val bleDevice = viewMode.choseBlePrinter.observeAsState()
    val openPrinterModeChoseDialog: State<Boolean> =
        viewMode.openPrinterModeChoseDialog.observeAsState(false)
    val openConnectModeChoseDialog: State<Boolean> =
        viewMode.openConnectModeChoseDialog.observeAsState(false)
    val openPrinterChoseDialog: State<Boolean> =
        viewMode.openPrinterChoseDialog.observeAsState(false)

    val openSDKModeChoseDialog: State<Boolean> =
        viewMode.openSDKModeChoseDialog.observeAsState(false)

    Column(modifier = modifier.padding(0.dp, 0.dp, 20.dp, 0.dp)) {
        StepOption(
            Modifier.padding(0.dp, 10.dp),
            stepTitle = "1. 选择连接方式",
            stepTips = "连接方式",
            value = connectMode.value.label
        ) {
            viewMode.openConnectModeChoseDialog()
        }

        if (connectMode.value != ConnectMode.NONE) {
            StepOption(
                modifier = Modifier.padding(0.dp, 10.dp),
                stepTitle = "2. 选择打印模式",
                stepTips = "打印模式",
                value = printerMode.value.label
            ) {
                viewMode.openPrinterModeChoseDialog()
            }
        }

        if (printerMode.value != PrinterMode.NONE) {
            StepOption(
                modifier = Modifier.padding(0.dp, 10.dp),
                stepTitle = "3. 选择SDK策略",
                stepTips = "SDK策略",
                value = sdkMode.value.label
            ) {
                viewMode.openSDKModeChoseDialog()
            }
        }

        if (sdkMode.value != SDKMode.NONE) {
            val tips = when (connectMode.value) {
                ConnectMode.BLE -> "蓝牙"
                ConnectMode.USB -> "USB"
                ConnectMode.WIFI -> "Wi-fi"
                else -> "null"
            }
            StepOption(
                modifier = Modifier.padding(0.dp, 10.dp),
                stepTitle = "4. 选择 $tips 打印机",
                stepTips = "打印机",
                value = when (connectMode.value) {
                    ConnectMode.BLE -> bleDevice.value?.name ?: ""
                    ConnectMode.USB -> usbDevice.value?.manufacturerName ?: ""
                    else -> ""
                }
            ) {
                viewMode.openPrinterChoseDialog()
            }
        }

        if (usbDevice.value != null || bleDevice.value != null){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ComButton(
                    value = "保存",
                    click = { viewMode.createPrinter() }
                )
            }
        }
    }

    //选择连接方式
    if (openConnectModeChoseDialog.value) {
        Dialog(onDismissRequest = { viewMode.closeConnectModeChoseDialog() }) {
            ConnectModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = connectMode.value,
                cancel = {
                    viewMode.closeConnectModeChoseDialog()
                },
                confirm = {
                    viewMode.notifyConnectMode(it)
                    viewMode.closeConnectModeChoseDialog()
                }
            )
        }
    }

    //选择打印模式
    if (openPrinterModeChoseDialog.value) {
        Dialog(onDismissRequest = { viewMode.closePrinterModeChoseDialog() }) {
            PrinterModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = printerMode.value,
                cancel = {
                    viewMode.closePrinterModeChoseDialog()
                },
                confirm = {
                    viewMode.notifyPrinterMode(it)
                    viewMode.closePrinterModeChoseDialog()
                }
            )
        }
    }

    if (openSDKModeChoseDialog.value) {
        Dialog(onDismissRequest = { viewMode.closeSDKModeChoseDialog() }) {
            SDKModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = sdkMode.value,
                dataList = viewMode.getSDKModeList(),
                cancel = {
                    viewMode.closeSDKModeChoseDialog()
                },
                confirm = {
                    viewMode.notifySDKMode(it)
                    viewMode.closeSDKModeChoseDialog()
                }
            )
        }
    }

    //选择Usb打印机
    if (openPrinterChoseDialog.value) {
        when (connectMode.value) {
            ConnectMode.USB -> {
                Dialog(onDismissRequest = { viewMode.closePrinterChoseDialog() }) {
                    UsbPrinterDialog(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(50.dp, 20.dp),
                        viewMode.usbDevicesList.value?.toList() ?: emptyList(),
                        cancel = {
                            viewMode.closePrinterChoseDialog()
                        },
                        confirm = {
                            viewMode.notifyChoseUSBDevice(it)
                            viewMode.closePrinterChoseDialog()
                        }
                    )
                }
            }

            ConnectMode.BLE -> {
                Dialog(onDismissRequest = { viewMode.closePrinterChoseDialog() }) {
                    BleToothPrinterDialog(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(50.dp, 20.dp),
                        viewMode,
                        bleDevice.value,
                        cancel = {
                            viewMode.closePrinterChoseDialog()
                        },
                        confirm = {
                            viewMode.notifyChoseBleDevice(it)
                            viewMode.closePrinterChoseDialog()
                        }
                    )
                }
            }

            else -> {

            }
        }

    }
}



