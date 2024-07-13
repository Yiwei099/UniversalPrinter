package com.eiviayw.universalprinter.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eiviayw.universalprinter.bean.ConnectMode
import com.eiviayw.universalprinter.bean.PrinterMode
import com.eiviayw.universalprinter.ui.theme.ColorE9E9E9
import com.eiviayw.universalprinter.ui.theme.GrayE3E3E3
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
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
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

@Preview(showBackground = true)
@Composable
fun ConnectModeDialogPreview() {
    ConnectModeDialog(modifier = Modifier)
}