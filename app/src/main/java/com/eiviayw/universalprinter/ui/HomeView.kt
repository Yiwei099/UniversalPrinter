package com.eiviayw.universalprinter.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.universalprinter.R
import com.eiviayw.universalprinter.bean.MyPrinter
import com.eiviayw.universalprinter.ui.create.BindPrinterView
import com.eiviayw.universalprinter.viewMode.MyViewModel
import com.eiviayw.universalprinter.views.ComItemOption
import com.eiviayw.universalprinter.views.ComTopBar
import com.eiviayw.universalprinter.views.ComVerticalLine
import com.eiviayw.universalprinter.views.EmptyView

@Composable
fun Home(viewModel: MyViewModel = viewModel()) {

    val state = viewModel.viewState.collectAsState().value
    val showBindPrinterView = state.showBindPrinterView
    val showModifyPrinterView = state.showModifyPrinterView

    val printerList = viewModel.myPrinterList.collectAsState().value

    Column {
        ComTopBar(
            title = stringResource(id = R.string.app_name),
            actionTitle = if (showBindPrinterView)
                stringResource(R.string.cancel)
            else
                stringResource(R.string.create),
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
            PrinterListView(modifier = Modifier.weight(3f)) { it, name->
                //打开设置打印机视图
                viewModel.showModifyPrinterView(true)
            }
            if (printerList.isNotEmpty() || showBindPrinterView) {
                if (showBindPrinterView) {
                    ComVerticalLine()
                    BindPrinterView(modifier = Modifier.weight(7f))
                } else if (showModifyPrinterView) {
                    ComVerticalLine()
//                    StartPrintView(modifier = Modifier.weight(7f))
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
    printerItemClick: (MyPrinter, String) -> Unit
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
                    onClick = { printerItemClick.invoke(it,it.name) }
                )
            }
        }
    }
}