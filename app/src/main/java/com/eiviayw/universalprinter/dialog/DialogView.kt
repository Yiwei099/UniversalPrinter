package com.eiviayw.universalprinter.dialog

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.eiviayw.universalprinter.BaseApplication
import com.eiviayw.universalprinter.constant.BuildMode
import com.eiviayw.universalprinter.constant.ConnectMode
import com.eiviayw.universalprinter.constant.DensityMode
import com.eiviayw.universalprinter.constant.ForWordMode
import com.eiviayw.universalprinter.constant.PaperMode
import com.eiviayw.universalprinter.constant.PrinterMode
import com.eiviayw.universalprinter.constant.SDKMode
import com.eiviayw.libcommon.theme.ColorE9E9E9
import com.eiviayw.universalprinter.util.BlueToothBroadcastReceiver
import com.eiviayw.universalprinter.util.BlueToothHelper
import com.eiviayw.universalprinter.util.UsbBroadcastReceiver
import com.eiviayw.universalprinter.viewMode.MyViewModel
import com.eiviayw.libcommon.views.ChoseOption
import com.eiviayw.libcommon.views.ComButton
import com.gprinter.command.LabelCommand

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
        title = "选择连接方式",
        data = connectModeList,
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
        title = "选择打印模式",
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
        title = "选择SDK策略",
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
fun USBDeviceListener(viewModel: MyViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val usbManager = BaseApplication.getInstance().getSystemService(Context.USB_SERVICE) as UsbManager

    // 记住 USB 监听器，以便稍后取消注册
    val usbReceiver = remember(usbManager) {
        UsbBroadcastReceiver().apply {
            setOnUsbReceiveListener(object : UsbBroadcastReceiver.OnUsbReceiveListener{
                override fun onUsbAttached(intent: Intent) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE,UsbDevice::class.java)
                    }else{
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    }?.let {
                        viewModel.addUsbDevice(it)
                    }
                }

                override fun onUsbDetached(intent: Intent) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE,UsbDevice::class.java)
                    }else{
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    }?.let { viewModel.removeUsbDevice(it) }
                }

                override fun onUsbPermission(intent: Intent) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE,UsbDevice::class.java)
                    }else{
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    }?.let { viewModel.addUsbDevice(it) }
                }
            })
        }
    }

    DisposableEffect(usbManager) {
        // 注册 USB 监听器
        usbReceiver.onRegister()

        viewModel.setUsbDevices(usbReceiver.getUsbDevices())

        onDispose {
            // 组件销毁时，取消注册 USB 监听器
            usbReceiver.onDestroy()
        }
    }
}

@Composable
fun UsbPrinterDialogV1(
    modifier: Modifier,
    viewModel: MyViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    defaultDevice: UsbDevice? = null,
    cancel: () -> Unit = {},
    confirm: (UsbDevice?) -> Unit = {}
){
    USBDeviceListener()

    var chooseDevice by remember { mutableStateOf(defaultDevice) }
    val devicesList = viewModel.usbDevicesList.collectAsState(initial = emptySet()).value

    val sizeDp = DpSize(LocalConfiguration.current.screenWidthDp.dp, LocalConfiguration.current.screenHeightDp.dp)

    Surface(
        modifier = modifier.wrapContentHeight(),
        color = Color.White,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(10.dp, 0.dp)
        ) {
            Text(
                text = "选择USB设备",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier.height(sizeDp.height * 0.7f)
            ) {
                items(
                    devicesList.toList(),
                ) { item ->
                    //对条目布局使用该修饰符来对列表的更改添加动画效果
                    ChoseOption(
                        title = item.manufacturerName ?: "" ,
                        chooseState = item.deviceName == chooseDevice?.deviceName,
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
                        confirm.invoke(chooseDevice)
                    }
                )
            }
        }
    }
}

@Composable
fun BleToothListener(viewModel: MyViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    val bleToothManager = BaseApplication.getInstance().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    val bleToothReceiver = remember(bleToothManager){
        BlueToothBroadcastReceiver{ viewModel.addBleDevice(it) }
    }

    DisposableEffect(bleToothManager) {
        // 注册 蓝牙 监听器
        bleToothReceiver.onReceive()
        //扫描设备
        BlueToothHelper.getInstance().discoveryBleDevice()

        onDispose {
            // 组件销毁时，取消注册 蓝牙 监听器
            bleToothReceiver.onDestroy()
            // 停止扫描
            BlueToothHelper.getInstance().stopDiscovery()
        }
    }
}

@Composable
fun BleToothPrinterDialogV1(
    modifier: Modifier,
    viewModel: MyViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    defaultChooseAddress:String = "",
    cancel: () -> Unit = {},
    confirm: (String) -> Unit = {}
){
    BleToothListener()

    var address by remember { mutableStateOf(defaultChooseAddress) }
    val dataState = viewModel.bleDevicesSet.collectAsState(initial = emptySet()).value

    Surface(
        modifier = modifier
            .width((LocalConfiguration.current.screenHeightDp * 0.8f).dp)
            .height((LocalConfiguration.current.screenHeightDp * 0.8f).dp),
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
                modifier = Modifier.weight(1f)
            ) {
                items(
                    dataState.toList(),
                ) { item ->
                    //对条目布局使用该修饰符来对列表的更改添加动画效果
                    ChoseOption(
                        title = "${item.name}-${item.address}" ?: "" ,
                        chooseState = item.address == address,
                        click = {
                            address = item.address
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
                        confirm.invoke(address)
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

    Surface(
        modifier = modifier,
        color = Color.White,
        shape = MaterialTheme.shapes.small
    ) {
        Column {
            Text(
                text = "选择纸张尺寸",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(list) { item ->
                    ChoseOption(
                        title = item.label,
                        chooseState = item.value == chooseMode.value,
                        click = {
                            chooseMode = item
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
                        confirm.invoke(chooseMode)
                    }
                )
            }
        }
    }
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
        title = "选择数据源类型",
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
fun ForWordModeDialog(
    modifier: Modifier,
    defaultChooseMode: ForWordMode = ForWordMode.NORMAL,
    cancel: () -> Unit = {},
    confirm: (ForWordMode) -> Unit = {}
) {
    var chooseMode by remember { mutableStateOf(defaultChooseMode) }

    Surface(
        modifier = modifier,
        color = Color.White,
        shape = MaterialTheme.shapes.small
    ) {
        Column {
            Text(
                text = "选择打印方向",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(mutableListOf<ForWordMode>().apply {
                    add(ForWordMode.NORMAL)
                    add(ForWordMode.BACK_FOR_WORD)
                }) { item ->
                    ChoseOption(
                        title = item.label,
                        chooseState = item.value == chooseMode.value,
                        click = {
                            chooseMode = item
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
                        confirm.invoke(chooseMode)
                    }
                )
            }
        }
    }
}

@Composable
fun DensityDialog(
    modifier: Modifier,
    defaultChooseMode: DensityMode = DensityMode.DENSITY_0,
    cancel: () -> Unit = {},
    confirm: (DensityMode) -> Unit = {}
) {
    var chooseMode by remember { mutableStateOf(defaultChooseMode) }

    Surface(
        modifier = modifier,
        color = Color.White,
        shape = MaterialTheme.shapes.small
    ) {
        Column {
            Text(
                text = "选择打印浓度",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(mutableListOf<DensityMode>().apply {
                    add(DensityMode.DENSITY_0)
                    add(DensityMode.DENSITY_1)
                    add(DensityMode.DENSITY_2)
                    add(DensityMode.DENSITY_3)
                    add(DensityMode.DENSITY_4)
                    add(DensityMode.DENSITY_5)
                    add(DensityMode.DENSITY_6)
                    add(DensityMode.DENSITY_7)
                    add(DensityMode.DENSITY_8)
                    add(DensityMode.DENSITY_9)
                    add(DensityMode.DENSITY_10)
                    add(DensityMode.DENSITY_11)
                    add(DensityMode.DENSITY_12)
                    add(DensityMode.DENSITY_13)
                    add(DensityMode.DENSITY_14)
                    add(DensityMode.DENSITY_15)
                }) { item ->
                    ChoseOption(
                        title = item.label,
                        chooseState = item.value == chooseMode.value,
                        click = {
                            chooseMode = item
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
                        confirm.invoke(chooseMode)
                    }
                )
            }
        }
    }
}

@Composable
fun <T> ItemOptionList(
    title:String = "",
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
                text = title,
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