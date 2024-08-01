package com.eiviayw.universalprinter.ui.print

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.print.gprinter.EscBtGPrinter
import com.eiviayw.print.gprinter.EscNetGPrinter
import com.eiviayw.print.gprinter.EscUsbGPrinter
import com.eiviayw.print.gprinter.TscBtGPrinter
import com.eiviayw.print.gprinter.TscUsbGPrinter
import com.eiviayw.print.native.NativeUsbPrinter
import com.eiviayw.universalprinter.bean.MyPrinter
import com.eiviayw.universalprinter.constant.BuildMode
import com.eiviayw.universalprinter.constant.PaperMode
import com.eiviayw.universalprinter.dialog.BuildModeDialog
import com.eiviayw.universalprinter.dialog.PaperSizeDialog
import com.eiviayw.universalprinter.ui.theme.Color177FF
import com.eiviayw.universalprinter.ui.theme.ColorE9E9E9
import com.eiviayw.universalprinter.ui.theme.ColorFF3434
import com.eiviayw.universalprinter.ui.theme.OrangeFF870D
import com.eiviayw.universalprinter.viewMode.MainViewMode
import com.eiviayw.universalprinter.viewMode.MyViewModel
import com.eiviayw.universalprinter.views.ComButton
import com.eiviayw.universalprinter.views.StepOption
import com.gprinter.utils.Command

@Composable
fun StartPrintView(
    modifier: Modifier = Modifier,
    viewMode: MyViewModel = viewModel()
) {
    val viewState = viewMode.viewState.collectAsState().value
    val thisPrinter = viewMode.startPrintPrinter.collectAsState().value ?: MyPrinter()

    Column(modifier = modifier.padding(0.dp, 0.dp, 20.dp, 0.dp)) {
            Text(text = thisPrinter.name)
            OutlinedTextField(
                modifier = Modifier.padding(0.dp,20.dp),
                value = thisPrinter.times,
                onValueChange = {
                    thisPrinter.times = it
                    thisPrinter.markDataChange()
                },
                label = { Text(text = "打印份数") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            )

            if (!thisPrinter.isEsc()){
                OutlinedTextField(
                    value = thisPrinter.startPosition,
                    onValueChange = {
                        thisPrinter.startPosition = it
                        thisPrinter.markDataChange()
                    },
                    label = { Text(text = "左侧起始位置") },
                    modifier = Modifier.padding(0.dp,20.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                )

                OutlinedTextField(
                    value = thisPrinter.topPosition,
                    onValueChange = {
                        thisPrinter.topPosition = it
                        thisPrinter.markDataChange()
                    },
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
                value = thisPrinter.paperSize.label
            ) {
                viewMode.showPaperListDialog(true)
            }

            StepOption(
                Modifier.padding(0.dp, 10.dp),
                stepTitle = "选择指令类型",
                stepTips = "指令类型",
                value = thisPrinter.buildMode.label
            ) {
                viewMode.showBuildListDialog(true)
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    ComButton(
                        modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                        value = "删除",
                        containerColor = ColorFF3434,
                        click = {
                            thisPrinter.releasePrinter()
                            viewMode.deletePrinter(thisPrinter)
                        }
                    )

                    ComButton(
                        modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                        value = "编辑",
                        containerColor = Color177FF,
                        click = {
                            viewMode.modifyPrinter(thisPrinter)
                            viewMode.showBindPrinterView(true)
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    ComButton(
                        modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                        value = "缓存销毁",
                        containerColor = OrangeFF870D,
                        click = {
                            thisPrinter.releasePrinter()
                        }
                    )

                    ComButton(
                        modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                        value = "打印(${thisPrinter.times})",
                        click = {
                            thisPrinter.startPrint()
                        }
                    )
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
                            viewMode.showModifyPrinterView(false)
                        }
                    )
                }
            }
    }

    if (viewState.showPaperListDialog){
        val paperList = if(thisPrinter?.isEsc() == true) {
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

        Dialog(onDismissRequest = { viewMode.showPaperListDialog(false) }) {
            PaperSizeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                list = paperList,
                defaultChooseMode = thisPrinter?.paperSize ?: PaperMode.NONE,
                cancel = {
                    viewMode.showPaperListDialog(false)
                },
                confirm = {
                    thisPrinter?.paperSize = it
                    thisPrinter?.markDataChange()
                    viewMode.showPaperListDialog(false)
                }
            )
        }
    }

    if (viewState.showBuildListDialog){
        Dialog(onDismissRequest = { viewMode.showBuildListDialog(false) }) {
            BuildModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = thisPrinter?.buildMode ?: BuildMode.Graphic,
                cancel = {
                    viewMode.showBuildListDialog(false)
                },
                confirm = {
                    thisPrinter?.buildMode = it
                    thisPrinter?.markDataChange()
                    viewMode.showBuildListDialog(false)
                }
            )
        }
    }
}