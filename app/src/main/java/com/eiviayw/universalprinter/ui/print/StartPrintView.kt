package com.eiviayw.universalprinter.ui.print

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.libcommon.R
import com.eiviayw.universalprinter.bean.MyPrinter
import com.eiviayw.universalprinter.constant.PaperMode
import com.eiviayw.universalprinter.dialog.BuildModeDialog
import com.eiviayw.universalprinter.dialog.DensityDialog
import com.eiviayw.universalprinter.dialog.ForWordModeDialog
import com.eiviayw.universalprinter.dialog.PaperSizeDialog
import com.eiviayw.libcommon.theme.Color177FF
import com.eiviayw.libcommon.theme.ColorCF5EEF
import com.eiviayw.libcommon.theme.ColorE9E9E9
import com.eiviayw.libcommon.theme.ColorFF3434
import com.eiviayw.libcommon.theme.OrangeFF870D
import com.eiviayw.libcommon.utils.WeakDataHolder
import com.eiviayw.libdraw.DrawingActivity
import com.eiviayw.universalprinter.viewMode.MyViewModel
import com.eiviayw.libcommon.views.ComButton
import com.eiviayw.libcommon.views.StepOption
import com.eiviayw.library.draw.BitmapOption

@Composable
fun StartPrintView(
    modifier: Modifier = Modifier,
    viewMode: MyViewModel = viewModel(),
    printer: MyPrinter
) {
    val viewState = viewMode.viewState.collectAsState().value
    var printTime by remember { mutableStateOf(printer.times) }
    var startPosition by remember { mutableStateOf(printer.startPosition) }
    var topPosition by remember { mutableStateOf(printer.topPosition) }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            WeakDataHolder.getInstance().getData<Pair<ByteArray,BitmapOption>?>(WeakDataHolder.RESULT_DATA,null)?.let {
                printer.notifyPrinterData(it.first,it.second)
            }
        }
    }

    LazyColumn(modifier = modifier.padding(0.dp, 0.dp, 20.dp, 0.dp)) {
        item {
            Text(text = printer.name)
        }

        item {
            OutlinedTextField(
                modifier = Modifier.padding(0.dp,20.dp,0.dp,0.dp),
                value = printTime,
                onValueChange = {
                    printTime = it
                    printer.times = it
                    printer.markDataChange()
                },
                label = { Text(text = stringResource(R.string.print_times)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            )
        }

        item {
            if (!printer.isEsc()){
                OutlinedTextField(
                    value = startPosition,
                    onValueChange = {
                        startPosition = it
                        printer.startPosition = it
                        printer.markDataChange()
                    },
                    label = { Text(text = stringResource(id = R.string.start_position)) },
                    modifier = Modifier.padding(0.dp,20.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                )

                OutlinedTextField(
                    value = topPosition,
                    onValueChange = {
                        topPosition = it
                        printer.topPosition = it
                        printer.markDataChange()
                    },
                    label = { Text(text = stringResource(id = R.string.top_position)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                )
            }
        }

        item {
            StepOption(
                Modifier.padding(0.dp, 10.dp),
                stepTitle = stringResource(id = R.string.choose_page_size),
                stepTips = stringResource(id = R.string.page_size),
                value = printer.paperSize.label
            ) {
                viewMode.showPaperListDialog(true)
            }
        }

        item {
            StepOption(
                Modifier.padding(0.dp, 10.dp),
                stepTitle = stringResource(id = R.string.choose_source_type),
                stepTips = stringResource(id = R.string.source_type),
                value = printer.buildMode.label
            ) {
                viewMode.showBuildListDialog(true)
            }
        }

        if (!printer.isEsc()){
            item {
                StepOption(
                    Modifier.padding(0.dp, 10.dp),
                    stepTitle = stringResource(id = R.string.choose_print_for_word),
                    stepTips = stringResource(id = R.string.print_for_word),
                    value = printer.forWordMode.label
                ) {
                    viewMode.showForWordListDialog(true)
                }
            }

            item {
                StepOption(
                    Modifier.padding(0.dp, 10.dp),
                    stepTitle = stringResource(id = R.string.choose_print_density),
                    stepTips = stringResource(id = R.string.print_density),
                    value = printer.density.label
                ) {
                    viewMode.showDensityListDialog(true)
                }
            }
        }

        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    ComButton(
                        modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                        value = stringResource(id = R.string.delete),
                        containerColor = ColorFF3434,
                        click = {
                            printer.releasePrinter()
                            viewMode.deletePrinter(printer)
                        }
                    )

                    ComButton(
                        modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                        value = stringResource(id = R.string.modify),
                        containerColor = Color177FF,
                        click = {
                            viewMode.modifyPrinter(printer)
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
                        value = stringResource(id = R.string.modify_print_source_data),
                        containerColor = ColorCF5EEF,
                        click = {
                            WeakDataHolder.getInstance().saveData(WeakDataHolder.DATA,printer.getBitmapOption())
                            launcher.launch(Intent(context,DrawingActivity::class.java))
                        }
                    )

                    ComButton(
                        modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                        value = stringResource(id = R.string.destroy_cache),
                        containerColor = OrangeFF870D,
                        click = {
                            printer.releasePrinter()
                        }
                    )

                    ComButton(
                        modifier = Modifier.padding(0.dp, 0.dp, 20.dp, 0.dp),
                        value = stringResource(id = R.string.print_x, printTime),
                        click = {
                            if (printer.printDataIsNotEmpty()){
                                printer.startPrint()
                                return@ComButton
                            }

                            Toast.makeText(context, context.getString(R.string.please_modify_print_source_data_first), Toast.LENGTH_SHORT).show()
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
                        value = stringResource(id = R.string.cancel),
                        containerColor = ColorE9E9E9,
                        click = {
                            viewMode.showModifyPrinterView(false)
                        }
                    )
                }
            }
        }
    }

    if (viewState.showPaperListDialog){
        val paperList = if(printer.isEsc()) {
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
                    .height((LocalConfiguration.current.screenHeightDp * 0.8f).dp)
                    .padding(50.dp, 20.dp),
                list = paperList,
                defaultChooseMode = printer.paperSize,
                cancel = {
                    viewMode.showPaperListDialog(false)
                },
                confirm = {
                    printer.paperSize = it
                    printer.markDataChange()
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
                defaultChooseMode = printer.buildMode,
                cancel = {
                    viewMode.showBuildListDialog(false)
                },
                confirm = {
                    printer.buildMode = it
                    printer.markDataChange()
                    viewMode.showBuildListDialog(false)
                }
            )
        }
    }

    if (viewState.showForWordListDialog){
        Dialog(onDismissRequest = { viewMode.showForWordListDialog(false) }) {
            ForWordModeDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = printer.forWordMode,
                cancel = {
                    viewMode.showForWordListDialog(false)
                },
                confirm = {
                    printer.forWordMode = it
                    printer.markDataChange()
                    viewMode.showForWordListDialog(false)
                }
            )
        }
    }

    if (viewState.showDensityListDialog){
        Dialog(onDismissRequest = { viewMode.showDensityListDialog(false) }) {
            DensityDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(50.dp, 20.dp),
                defaultChooseMode = printer.density,
                cancel = {
                    viewMode.showDensityListDialog(false)
                },
                confirm = {
                    printer.density = it
                    printer.markDataChange()
                    viewMode.showDensityListDialog(false)
                }
            )
        }
    }
}