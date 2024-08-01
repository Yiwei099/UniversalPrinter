package com.eiviayw.universalprinter.ui.create

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.universalprinter.BaseApplication
import com.eiviayw.universalprinter.constant.ConnectMode
import com.eiviayw.universalprinter.constant.PrinterMode
import com.eiviayw.universalprinter.constant.SDKMode
import com.eiviayw.universalprinter.dialog.BleToothPrinterDialogV1
import com.eiviayw.universalprinter.dialog.ConnectModeDialog
import com.eiviayw.universalprinter.dialog.PrinterModeDialog
import com.eiviayw.universalprinter.dialog.SDKModeDialog
import com.eiviayw.universalprinter.dialog.UsbPrinterDialogV1
import com.eiviayw.universalprinter.viewMode.MyViewModel
import com.eiviayw.universalprinter.views.ComButton
import com.eiviayw.universalprinter.views.StepOption

@Composable
fun BindPrinterView(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = viewModel()
) {
    val viewState = viewModel.viewState.collectAsState().value
    val newPrinter = viewModel.myPrinter.collectAsState().value

    Column(modifier = modifier.padding(0.dp, 0.dp, 20.dp, 0.dp)) {
        StepOption(
            Modifier.padding(0.dp, 10.dp),
            stepTitle = "1. 选择连接方式",
            stepTips = "连接方式",
            value = newPrinter.connectMode.label
        ) {
            viewModel.showConnectModeListDialog(true)
        }

        StepOption(
            modifier = Modifier.padding(0.dp, 10.dp),
            stepTitle = "2. 选择打印模式",
            stepTips = "打印模式",
            value = newPrinter.printerMode.label
        ) {
            viewModel.showPrinterModeListDialog(true)
        }

        StepOption(
            modifier = Modifier.padding(0.dp, 10.dp),
            stepTitle = "3. 选择SDK策略",
            stepTips = "SDK策略",
            value = newPrinter.sdkMode.label
        ) {
            viewModel.showSDKModeListDialog(true)
        }

        if (newPrinter.connectMode != ConnectMode.WIFI){
            val tips = when(newPrinter.connectMode){
                ConnectMode.BLE -> "蓝牙"
                else -> "USB"
            }
            StepOption(
                modifier = Modifier.padding(0.dp, 10.dp),
                stepTitle = "4. 选择 $tips 打印机",
                stepTips = "打印机",
                value = when (newPrinter.connectMode) {
                    ConnectMode.BLE -> newPrinter.address
                    ConnectMode.USB -> newPrinter.usbDevice?.manufacturerName ?: ""
                    else -> ""
                }
            ) {
                viewModel.showDeviceListDialog(true)
            }
        }else{
            OutlinedTextField(
                value = newPrinter.address,
                onValueChange = { newPrinter.address = it },
                label = { Text(text = "打印机IP地址") },
                modifier = Modifier.padding(0.dp,20.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                ),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            ComButton(
                value = "保存",
                click = {
                    if (newPrinter.connectMode == ConnectMode.BLE) {
                        if (newPrinter.address.isEmpty()) {
                            Toast.makeText(BaseApplication.getInstance(), "蓝牙地址不能为空", Toast.LENGTH_SHORT).show()
                            return@ComButton
                        }

                        viewModel.savePrinter()
                    }

                    if (newPrinter.connectMode == ConnectMode.WIFI) {
                        if (newPrinter.address.isEmpty()) {
                            Toast.makeText(BaseApplication.getInstance(), "Ip地址不能为空", Toast.LENGTH_SHORT).show()
                            return@ComButton
                        }

                        viewModel.savePrinter()
                    }

                    if (newPrinter.connectMode == ConnectMode.USB) {
                        newPrinter.usbDevice?.let {
                            viewModel.savePrinter()
                        } ?: { Toast.makeText(BaseApplication.getInstance(), "请选择USB设备", Toast.LENGTH_SHORT).show() }
                    }
                }
            )
        }
    }

    //选择连接方式
    if (viewState.showConnectModeListDialog) {
        Dialog(onDismissRequest = { viewModel.showConnectModeListDialog(false) }) {
            ConnectModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = newPrinter.connectMode,
                cancel = {
                    viewModel.showConnectModeListDialog(false)
                },
                confirm = {
                    newPrinter.connectMode = it
                    viewModel.showConnectModeListDialog(false)
                }
            )
        }
    }

    //选择打印模式
    if (viewState.showPrinterModeListDialog) {
        Dialog(onDismissRequest = { viewModel.showPrinterModeListDialog(false) }) {
            PrinterModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = newPrinter.printerMode,
                cancel = {
                    viewModel.showPrinterModeListDialog(false)
                },
                confirm = {
                    newPrinter.printerMode = it
                    viewModel.showPrinterModeListDialog(false)
                }
            )
        }
    }

    //选择SDK策略
    if (viewState.showSDKModeListDialog) {
        Dialog(onDismissRequest = { viewModel.showSDKModeListDialog(false) }) {
            SDKModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = newPrinter.sdkMode,
                dataList = getSDKModeList(newPrinter.printerMode,newPrinter.connectMode),
                cancel = {
                    viewModel.showSDKModeListDialog(false)
                },
                confirm = {
                    newPrinter.sdkMode = it
                    viewModel.showSDKModeListDialog(false)
                }
            )
        }
    }

    if (viewState.showDeviceListDialog){
        if (newPrinter.connectMode == ConnectMode.BLE){
            Dialog(onDismissRequest = { viewModel.showDeviceListDialog(false) }) {
                BleToothPrinterDialogV1(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(50.dp, 20.dp),
                    defaultChooseAddress = newPrinter.address,
                    cancel = {
                        viewModel.showDeviceListDialog(false)
                    },
                    confirm = {
                        newPrinter.address = it
                        viewModel.showDeviceListDialog(false)
                    }
                )
            }
        }else if (newPrinter.connectMode == ConnectMode.USB){
            Dialog(onDismissRequest = { viewModel.showDeviceListDialog(false) }) {
                UsbPrinterDialogV1(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(50.dp, 20.dp),
                    cancel = {
                        viewModel.showDeviceListDialog(false)
                    },
                    confirm = {
                        newPrinter.usbDevice = it
                        viewModel.showDeviceListDialog(false)
                    }
                )
            }
        }else{
            //其它
        }
    }
}

/**
 * 获取SDK策略列表
 */
private fun getSDKModeList(printerMode:PrinterMode,connectMode: ConnectMode): List<SDKMode> {
    return if (printerMode == PrinterMode.ESC) {
        getEscSDK(connectMode)
    } else {
        getTscSDK(connectMode)
    }
}

/**
 * 获取ESC SDK策略列表
 */
private fun getEscSDK(connectMode:ConnectMode): List<SDKMode> {
    return mutableListOf<SDKMode>().apply {
        add(SDKMode.GPrinter)
        add(SDKMode.EpsonESC)
        if (connectMode == ConnectMode.USB) {
            add(SDKMode.NativeUsb)
        }
    }
}

/**
 * 获取TSC SDK策略列表
 */
private fun getTscSDK(connectMode:ConnectMode): List<SDKMode> {
    return mutableListOf<SDKMode>().apply {
        add(SDKMode.GPrinter)
        add(SDKMode.BixolonTsc)
        if (connectMode == ConnectMode.USB) {
            add(SDKMode.NativeUsb)
        }
    }
}