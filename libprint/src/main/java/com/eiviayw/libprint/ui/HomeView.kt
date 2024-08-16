package com.eiviayw.libprint.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.libcommon.R
import com.eiviayw.libcommon.views.ComItemOption
import com.eiviayw.libcommon.views.ComTopBar
import com.eiviayw.libcommon.views.ComVerticalLine
import com.eiviayw.libcommon.views.EmptyView
import com.eiviayw.libprint.bean.MyPrinter
import com.eiviayw.libprint.ui.create.BindPrinterView
import com.eiviayw.libprint.ui.print.StartPrintView
import com.eiviayw.libprint.viewMode.MyViewModel

@Composable
fun Home(viewModel: MyViewModel = viewModel()) {

    val state = viewModel.viewState.collectAsState().value
    val showBindPrinterView = state.showBindPrinterView
    val showModifyPrinterView = state.showModifyPrinterView

    val printerList = viewModel.myPrinterList.collectAsState().value
    var thisPrinter by remember { mutableStateOf<MyPrinter?>(null) }

    Column {
        ComTopBar(
            title = stringResource(id = R.string.app_name),
            actionTitle = stringResource(if (showBindPrinterView) R.string.cancel else R.string.create),
            onActionClick = {
                if (showBindPrinterView) {
                    //关闭绑定设备视图
                    viewModel.showBindPrinterView(false)
                } else {
                    //显示绑定设备视图
                    viewModel.showBindPrinterView(true)
                }
            }
        )
        Row {
            PrinterListView(modifier = Modifier.weight(3f)) {
                //打开设置打印机视图
                thisPrinter = it
                viewModel.showModifyPrinterView(true)
            }
            if (printerList.isNotEmpty() || showBindPrinterView) {
                ComVerticalLine()
                if (showBindPrinterView) {
                    BindPrinterView(modifier = Modifier.weight(7f))
                } else if (showModifyPrinterView) {
                    thisPrinter?.let {
                        StartPrintView(modifier = Modifier.weight(7f), printer = it)
                    }
                }
            } else {
                EmptyView(tips = stringResource(R.string.empty_printer_list_tips))
            }
        }
    }
}

@Composable
fun PrinterListView(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = viewModel(),
    printerItemClick: (MyPrinter) -> Unit
) {
    val printerList = viewModel.myPrinterList.collectAsState().value

    Column(
        modifier
    ) {
        if (printerList.isEmpty()) {
            EmptyView(tips = stringResource(R.string.empty_printer_list_tips_1))
        } else {
            printerList.forEach {
                ComItemOption(
                    title = it.name,
                    value = "",
                    onClick = { printerItemClick.invoke(it) }
                )
            }
        }
    }
}