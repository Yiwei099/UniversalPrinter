package com.eiviayw.universalprinter.ui.create

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.universalprinter.BaseApplication
import com.eiviayw.libcommon.R
import com.eiviayw.universalprinter.constant.ConnectMode
import com.eiviayw.universalprinter.constant.PrinterMode
import com.eiviayw.universalprinter.constant.SDKMode
import com.eiviayw.universalprinter.dialog.BleToothPrinterDialogV1
import com.eiviayw.universalprinter.dialog.ConnectModeDialog
import com.eiviayw.universalprinter.dialog.PrinterModeDialog
import com.eiviayw.universalprinter.dialog.SDKModeDialog
import com.eiviayw.universalprinter.dialog.UsbPrinterDialogV1
import com.eiviayw.universalprinter.viewMode.MyViewModel
import com.eiviayw.libcommon.views.ComButton
import com.eiviayw.libcommon.views.StepOption

@Composable
fun BindPrinterView(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = viewModel()
) {
    val viewState = viewModel.viewState.collectAsState().value
    val newPrinter = viewModel.myPrinter.collectAsState().value
    var address by remember { mutableStateOf(newPrinter.address) }

    LazyColumn(modifier = modifier.padding(0.dp, 0.dp, 20.dp, 0.dp)) {
        item {
            StepOption(
                Modifier.padding(0.dp, 10.dp),
                stepTitle = stringResource(R.string.choose_connection_type),
                stepTips = stringResource(R.string.connection_type),
                value = newPrinter.connectMode.label
            ) {
                viewModel.showConnectModeListDialog(true)
            }
        }

        item {
            StepOption(
                modifier = Modifier.padding(0.dp, 10.dp),
                stepTitle = stringResource(R.string.choose_printing_mode),
                stepTips = stringResource(R.string.printing_mode),
                value = newPrinter.printerMode.label
            ) {
                viewModel.showPrinterModeListDialog(true)
            }
        }

        item {
            StepOption(
                modifier = Modifier.padding(0.dp, 10.dp),
                stepTitle = stringResource(R.string.choose_sdk_policy),
                stepTips = stringResource(R.string.sdk_policy),
                value = newPrinter.sdkMode.label
            ) {
                viewModel.showSDKModeListDialog(true)
            }
        }

        item {
            if (newPrinter.connectMode != ConnectMode.WIFI) {
                val tips = when (newPrinter.connectMode) {
                    ConnectMode.BLE -> stringResource(R.string.blue_tooth)
                    else -> stringResource(R.string.usb)
                }
                StepOption(
                    modifier = Modifier.padding(0.dp, 10.dp),
                    stepTitle = stringResource(R.string.choose_x_device, tips),
                    stepTips = stringResource(R.string.device),
                    value = when (newPrinter.connectMode) {
                        ConnectMode.BLE -> newPrinter.address
                        ConnectMode.USB -> newPrinter.usbDevice?.manufacturerName ?: ""
                        else -> ""
                    }
                ) {
                    viewModel.showDeviceListDialog(true)
                }
            } else {
                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        newPrinter.address = it
                    },
                    label = { Text(text = stringResource(R.string.device_ip)) },
                    modifier = Modifier.padding(0.dp, 20.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                    ),
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ComButton(
                    value = stringResource(R.string.save),
                    click = {
                        if (newPrinter.connectMode == ConnectMode.BLE) {
                            if (newPrinter.address.isEmpty()) {
                                Toast.makeText(
                                    BaseApplication.getInstance(),
                                    BaseApplication.getInstance()
                                        .getString(R.string.blue_tooth_device_ip_dont_empty),
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@ComButton
                            }

                            viewModel.savePrinter()
                        }

                        if (newPrinter.connectMode == ConnectMode.WIFI) {
                            if (newPrinter.address.isEmpty()) {
                                Toast.makeText(
                                    BaseApplication.getInstance(),
                                    BaseApplication.getInstance()
                                        .getString(R.string.device_ip_dont_empty),
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@ComButton
                            }

                            viewModel.savePrinter()
                        }

                        if (newPrinter.connectMode == ConnectMode.USB) {
                            newPrinter.usbDevice?.let {
                                viewModel.savePrinter()
                            } ?: {
                                Toast.makeText(
                                    BaseApplication.getInstance(),
                                    BaseApplication.getInstance()
                                        .getString(R.string.please_select_a_usb_device),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
            }
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
        when (newPrinter.connectMode) {
            ConnectMode.BLE -> {
                Dialog(onDismissRequest = { viewModel.showDeviceListDialog(false) }) {
                    BleToothPrinterDialogV1(
                        modifier = Modifier.padding(50.dp, 20.dp),
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
            }
            ConnectMode.USB -> {
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
            }
            else -> {
                //其它
            }
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