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
import com.eiviayw.universalprinter.bean.BuildMode
import com.eiviayw.universalprinter.bean.PaperMode
import com.eiviayw.universalprinter.dialog.BuildModeDialog
import com.eiviayw.universalprinter.dialog.PaperSizeDialog
import com.eiviayw.universalprinter.ui.theme.ColorE9E9E9
import com.eiviayw.universalprinter.viewMode.MainViewMode
import com.eiviayw.universalprinter.views.ComButton
import com.eiviayw.universalprinter.views.StepOption

@Composable
fun StartPrintView(
    modifier: Modifier = Modifier,
    viewMode: MainViewMode
) {

    val choosePrinter = viewMode.choosePrinter.observeAsState()

    var showPaperModeDialog by remember { mutableStateOf(false) }
    var showBuildModeDialog by remember { mutableStateOf(false) }
    var printTime by remember { mutableStateOf("1") }
    var startIndex by remember { mutableStateOf("0") }
    var topIndex by remember { mutableStateOf("0") }
    var paperSize by remember { mutableStateOf(PaperMode.NONE) }
    var buildMode by remember { mutableStateOf(BuildMode.Graphic) }


    Column(modifier = modifier.padding(0.dp, 0.dp, 20.dp, 0.dp)) {
        OutlinedTextField(
            value = printTime,
            onValueChange = { printTime = it },
            label = { Text(text = "打印份数") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
        )

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
                value = "返回",
                containerColor = ColorE9E9E9,
                click = {
                    viewMode.openStartPrintView(null)
                }
            )

            ComButton(
                value = "打印($printTime)",
                click = {}
            )
        }
    }

    if (showPaperModeDialog){
        val paperList = when(choosePrinter.value){
            is EscBtGPrinter, is EscUsbGPrinter -> {
                mutableListOf<PaperMode>().apply {
                    add(PaperMode.ESC_58)
                    add(PaperMode.ESC_80)
                    add(PaperMode.NONE)
                }
            }
            is TscBtGPrinter,is TscUsbGPrinter -> {
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
            else -> emptyList<PaperMode>()
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