package com.eiviayw.libdraw.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.libcommon.views.ComItemOption
import com.eiviayw.libcommon.views.ComTopBar
import com.eiviayw.libdraw.DrawingViewMode
import com.eiviayw.libdraw.constant.ParamType
import com.eiviayw.library.bean.param.MultiElementParam
import com.eiviayw.library.bean.param.TextParam
import com.eiviayw.library.draw.BitmapOption

@Composable
fun DrawingHome(
    modifier: Modifier = Modifier,
    bitmapOption: BitmapOption = BitmapOption(),
    viewModel: DrawingViewMode = viewModel()
) {
    val viewState = viewModel.drawViewState.collectAsState().value
    val paramList = viewModel.sourceDataList.collectAsState(initial = emptySet()).value

    Column(modifier) {
        ComTopBar(title = "打印数据源", actionTitle = "添加") {
            viewModel.showParamTypePopupView(true)
        }

        if (viewState.showPopupMenu) {
            ParamPopupMenu(
                true,
                offset = DpOffset(
                    LocalConfiguration.current.screenWidthDp.dp,
                    0.dp
                ),
                data = listOf(
                    ParamType.SINGLE_TEXT,
                    ParamType.MULTI_TEXT,
                    ParamType.IMAGE,
                    ParamType.LINE,
                    ParamType.MULTI,
                    ParamType.BITMAP
                ),
                getLabelText = {
                    it.label
                }
            ) {
                viewModel.showParamTypePopupView(false)
                when (it) {
                    ParamType.SINGLE_TEXT -> {
                        viewModel.showSingleTextView(true)
                    }

                    ParamType.MULTI_TEXT -> {
                        viewModel.showMultiElementView(true)
                    }

                    ParamType.IMAGE -> {
                    }

                    ParamType.LINE -> {
                    }

                    ParamType.BITMAP -> {
                        viewModel.showBitmapOptionView(true)
                    }

                    else -> {}
                }
            }
        }

        Row {
//          Image(painter = , contentDescription = "")

            LazyColumn(
                modifier = Modifier.weight(0.7f)
            ) {
                items(paramList.toList()) { item ->
                    when(item){
                        is TextParam -> ComItemOption(title = "文本元素", value = item.text)
                        
                        is MultiElementParam -> ComItemOption(title = "混排元素", value = "")
                    }
                }
            }
        }
    }

    if (viewState.showSingleTextDialog) {
        ModifySingleTextDialog(
            onCancel = {
                viewModel.showSingleTextView(false)
            },
            onConfirm = {
                viewModel.addParam(it)
                viewModel.showSingleTextView(false)
            }
        )
    }

    if (viewState.showMultiElementDialog) {
        ModifyMultiTextDialog(
            cancel = {
                viewModel.showMultiElementView(false)
            },
            confirm = {
                viewModel.addParam(it)
                viewModel.showMultiElementView(false)
            }
        )
    }

    if (viewState.showBitmapOptionView){
        BitmapOptionView(
            defaultData = viewModel.getBitmapOption(),
            cancel = {
                viewModel.showBitmapOptionView(false)
            },
            confirm = {
                viewModel.showBitmapOptionView(false)
                viewModel.setBitmapOption(it)
            }
        )
    }
}