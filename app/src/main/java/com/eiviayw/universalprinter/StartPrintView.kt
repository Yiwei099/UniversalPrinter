package com.eiviayw.universalprinter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eiviayw.print.base.BasePrinter
import com.eiviayw.print.gprinter.EscBtGPrinter
import com.eiviayw.print.gprinter.EscUsbGPrinter
import com.eiviayw.print.gprinter.TscBtGPrinter
import com.eiviayw.print.gprinter.TscUsbGPrinter
import com.eiviayw.print.native.NativeUsbPrinter
import com.eiviayw.universalprinter.bean.BuildMode
import com.eiviayw.universalprinter.bean.PaperMode
import com.eiviayw.universalprinter.dialog.BuildModeDialog
import com.eiviayw.universalprinter.dialog.PaperSizeDialog
import com.eiviayw.universalprinter.ui.theme.Color177FF
import com.eiviayw.universalprinter.ui.theme.ColorE9E9E9
import com.eiviayw.universalprinter.ui.theme.ColorFF3434
import com.eiviayw.universalprinter.ui.theme.OrangeFF870D
import com.eiviayw.universalprinter.viewMode.MainViewMode
import com.eiviayw.universalprinter.views.ComButton
import com.eiviayw.universalprinter.views.StepOption
import com.gprinter.utils.Command

@Composable
fun StartPrintView(
    modifier: Modifier = Modifier,
    viewMode: MainViewMode,
    name:String=""
) {

    val choosePrinter = viewMode.choosePrinter.observeAsState()

    val isEscPrinter = when(choosePrinter.value){
        is EscBtGPrinter, is EscUsbGPrinter -> true
        is TscBtGPrinter,is TscUsbGPrinter -> false
        is NativeUsbPrinter -> (choosePrinter.value as NativeUsbPrinter).getCommandTypeValue() == Command.ESC
        else -> false
    }

    var showPaperModeDialog by remember { mutableStateOf(false) }
    var showBuildModeDialog by remember { mutableStateOf(false) }
    var printTime by remember { mutableStateOf("1") }
    var startIndex by remember { mutableStateOf("0") }
    var topIndex by remember { mutableStateOf("0") }
    var paperSize by remember { mutableStateOf(if (isEscPrinter) PaperMode.ESC_58 else PaperMode.NONE) }
    var buildMode by remember { mutableStateOf(BuildMode.Graphic) }


    Column(modifier = modifier.padding(0.dp, 0.dp, 20.dp, 0.dp)) {
        Text(text = name)
        OutlinedTextField(
            modifier = Modifier.padding(0.dp,20.dp),
            value = printTime,
            onValueChange = { printTime = it },
            label = { Text(text = "打印份数") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
        )

        if (!isEscPrinter){
            OutlinedTextField(
                value = startIndex,
                onValueChange = { startIndex = it },
                label = { Text(text = "左侧起始位置") },
                modifier = Modifier.padding(0.dp,20.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            )

            OutlinedTextField(
                value = topIndex,
                onValueChange = { topIndex = it },
                label = { Text(text = "顶部起始位置") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            )
        }

        StepOption(
            Modifier.padding(0.dp, 10.dp),
            stepTitle = "选择纸张尺寸",
            stepTips = "纸张尺寸",
            value = paperSize.label
        ) {
            showPaperModeDialog = true
        }

        StepOption(
            Modifier.padding(0.dp, 10.dp),
            stepTitle = "选择指令类型",
            stepTips = "指令类型",
            value = buildMode.label
        ) {
            showBuildModeDialog = true
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            ComButton(
                modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                value = "返回",
                containerColor = ColorE9E9E9,
                click = {
                    viewMode.openStartPrintView(null)
                }
            )

            ComButton(
                modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                value = "删除",
                containerColor = ColorFF3434,
                click = {
                    choosePrinter.value?.let {
                        viewMode.deletePrinter(it)
                    }
                }
            )

            ComButton(
                modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                value = "编辑",
                containerColor = Color177FF,
                click = {
                    choosePrinter.value?.let {
                        viewMode.modifyPrinter(it)
                    }
                }
            )

            ComButton(
                modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                value = "缓存销毁",
                containerColor = OrangeFF870D,
                click = {
                    choosePrinter.value?.let {
                        viewMode.onDestroyPrinter(it)
                    }
                }
            )

            ComButton(
                value = "打印($printTime)",
                click = { choosePrinter.value?.let {
                    viewMode.startPrint(it,isEscPrinter,printTime.toInt(),startIndex,topIndex,paperSize,buildMode)
                } }
            )
        }
    }

    if (showPaperModeDialog){
        val paperList = if(isEscPrinter) {
            mutableListOf<PaperMode>().apply {
                add(PaperMode.ESC_58)
                add(PaperMode.ESC_80)
            }
        }else{
            mutableListOf<PaperMode>().apply {
                add(PaperMode.TSC_3020)
                add(PaperMode.TSC_4030)
                add(PaperMode.TSC_4060)
                add(PaperMode.TSC_4080)
                add(PaperMode.TSC_6040)
                add(PaperMode.TSC_6080)
                add(PaperMode.NONE)
            }
        }

        Dialog(onDismissRequest = { showPaperModeDialog = false }) {
            PaperSizeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                list = paperList,
                defaultChooseMode = paperSize,
                cancel = {
                    showPaperModeDialog = false
                },
                confirm = {
                    paperSize = it
                    showPaperModeDialog = false
                }
            )
        }
    }

    if (showBuildModeDialog){
        Dialog(onDismissRequest = { showBuildModeDialog = false }) {
            BuildModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = buildMode,
                cancel = {
                    showBuildModeDialog = false
                },
                confirm = {
                    buildMode = it
                    showBuildModeDialog = false
                }
            )
        }
    }
}