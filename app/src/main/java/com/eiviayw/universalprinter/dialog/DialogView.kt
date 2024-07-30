package com.eiviayw.universalprinter.dialog

import android.bluetooth.BluetoothDevice
import android.hardware.usb.UsbDevice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eiviayw.universalprinter.bean.BuildMode
import com.eiviayw.universalprinter.bean.ConnectMode
import com.eiviayw.universalprinter.bean.PaperMode
import com.eiviayw.universalprinter.bean.PrinterMode
import com.eiviayw.universalprinter.bean.SDKMode
import com.eiviayw.universalprinter.ui.theme.ColorE9E9E9
import com.eiviayw.universalprinter.viewMode.MainViewMode
import com.eiviayw.universalprinter.views.ChoseOption
import com.eiviayw.universalprinter.views.ComButton

@Composable
fun ConnectModeDialog(
    modifier: Modifier,
    defaultChooseMode: ConnectMode = ConnectMode.NONE,
    cancel: () -> Unit = {},
    confirm: (ConnectMode) -> Unit = {}
) {
    val connectModeList = mutableListOf<ConnectMode>().apply {
        add(ConnectMode.WIFI)
        add(ConnectMode.USB)
        add(ConnectMode.BLE)
    }

    var chooseMode by remember { mutableStateOf(defaultChooseMode) }

    ItemOptionList(
        data = connectModeList,
        modifier = modifier,
        getItemName = { it.name },
        getChooseState = { it.label == chooseMode.label },
        onItemClick = {
            chooseMode = it
        },
        cancel = cancel,
        confirm = {
            confirm.invoke(chooseMode)
        }
    )
}

@Composable
fun PrinterModeDialog(
    modifier: Modifier,
    defaultChooseMode: PrinterMode = PrinterMode.NONE,
    cancel: () -> Unit = {},
    confirm: (PrinterMode) -> Unit = {}
) {
    val printerModeList = mutableListOf<PrinterMode>().apply {
        add(PrinterMode.ESC)
        add(PrinterMode.TSC)
//        add(PrinterMode.NONE)
    }

    var chooseMode by remember { mutableStateOf(defaultChooseMode) }

    ItemOptionList(
        data = printerModeList,
        modifier = modifier,
        getItemName = { it.label },
        getChooseState = { it.value == chooseMode.value },
        onItemClick = {
            chooseMode = it
        },
        cancel = cancel,
        confirm = {
            confirm.invoke(chooseMode)
        }
    )
}

@Composable
fun SDKModeDialog(
    modifier: Modifier,
    defaultChooseMode: SDKMode = SDKMode.NONE,
    dataList: List<SDKMode> = emptyList<SDKMode>(),
    cancel: () -> Unit = {},
    confirm: (SDKMode) -> Unit = {}
) {

    var chooseMode by remember { mutableStateOf(defaultChooseMode) }

    ItemOptionList(
        data = dataList,
        modifier = modifier,
        getItemName = { it.label },
        getChooseState = { it.label == chooseMode.label },
        onItemClick = {
            chooseMode = it
        },
        cancel = cancel,
        confirm = {
            confirm.invoke(chooseMode)
        }
    )
}

@Composable
fun UsbPrinterDialog(
    modifier: Modifier,
    devicesList:List<UsbDevice>,
    cancel: () -> Unit = {},
    confirm: (UsbDevice) -> Unit = {}
){
    var chooseDevice by remember { mutableStateOf(devicesList.firstOrNull()) }
    ItemOptionList(
        data = devicesList,
        modifier = modifier,
        getItemName = { it.manufacturerName ?: "" },
        getChooseState = { chooseDevice?.deviceName == it.deviceName },
        onItemClick = {
            chooseDevice = it
        },
        cancel = cancel,
        confirm = {
            chooseDevice?.let { confirm.invoke(it) }
        }
    )
}

@Composable
fun BleToothPrinterDialog(
    modifier: Modifier,
    viewMode: MainViewMode,
    defaultChooseDevice: BluetoothDevice?,
    cancel: () -> Unit = {},
    confirm: (BluetoothDevice) -> Unit = {}
){
    var chooseDevice by remember { mutableStateOf(defaultChooseDevice) }

    val dataState = viewMode.bleDevicesSet.collectAsState(initial = emptySet())

    Surface(
        modifier = modifier.wrapContentHeight(),
        color = Color.White,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(10.dp, 0.dp)
        ) {
            Text(
                text = "选择蓝牙设备",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier.height(500.dp)
            ) {
                items(
                    dataState.value.toList(),
                ) { item ->
                    //对条目布局使用该修饰符来对列表的更改添加动画效果
                    ChoseOption(
                        title = "${item.name}-${item.address}" ?: "" ,
                        chooseState = item.address == chooseDevice?.address,
                        click = {
                            chooseDevice = item
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ComButton(
                    value = "取消",
                    containerColor = ColorE9E9E9,
                    click = cancel
                )
                ComButton(
                    value = "确定",
                    click = {
                        chooseDevice?.let{
                            confirm.invoke(it)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PaperSizeDialog(
    modifier: Modifier,
    list: List<PaperMode>,
    defaultChooseMode: PaperMode = PaperMode.NONE,
    cancel: () -> Unit = {},
    confirm: (PaperMode) -> Unit = {}
) {
    var chooseMode by remember { mutableStateOf(defaultChooseMode) }

    ItemOptionList(
        data = list,
        modifier = modifier,
        getItemName = { it.label },
        getChooseState = { it.value == chooseMode.value },
        onItemClick = {
            chooseMode = it
        },
        cancel = cancel,
        confirm = {
            confirm.invoke(chooseMode)
        }
    )
}

@Composable
fun BuildModeDialog(
    modifier: Modifier,
    defaultChooseMode: BuildMode = BuildMode.Graphic,
    cancel: () -> Unit = {},
    confirm: (BuildMode) -> Unit = {}
){
    var chooseMode by remember { mutableStateOf(defaultChooseMode) }

    ItemOptionList(
        data = mutableListOf<BuildMode>().apply {
            add(BuildMode.Graphic)
            add(BuildMode.Text)
        },
        modifier = modifier,
        getItemName = { it.label },
        getChooseState = { it.value == chooseMode.value },
        onItemClick = {
            chooseMode = it
        },
        cancel = cancel,
        confirm = {
            confirm.invoke(chooseMode)
        }
    )
}

@Composable
fun <T> ItemOptionList(
    data: List<T> = emptyList(),
    modifier: Modifier = Modifier,
    getItemName: (T) -> String = { "" },
    getChooseState: (T) -> Boolean = { false },
    onItemClick: (T) -> Unit = {},
    cancel: () -> Unit = {},
    confirm: () -> Unit = {}
) {
    Surface(
        modifier = modifier.wrapContentHeight(),
        color = Color.White,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(10.dp, 0.dp)
        ) {
            Text(
                text = "选择连接方式",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp),
                textAlign = TextAlign.Center
            )
            for (modeItem in data) {
                ChoseOption(
                    title = getItemName(modeItem),
                    chooseState = getChooseState(modeItem),
                    click = {
                        onItemClick(modeItem)
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ComButton(
                    value = "取消",
                    containerColor = ColorE9E9E9,
                    click = cancel
                )
                ComButton(
                    value = "确定",
                    click = confirm
                )
            }
        }
    }
}