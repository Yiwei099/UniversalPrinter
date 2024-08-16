package com.eiviayw.libdraw.ui

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.libcommon.BaseApplication
import com.eiviayw.libcommon.theme.Color177FF
import com.eiviayw.libcommon.theme.ColorE9E9E9
import com.eiviayw.libcommon.theme.OrangeFF870D
import com.eiviayw.libcommon.utils.WeakDataHolder
import com.eiviayw.libcommon.views.ComButton
import com.eiviayw.libcommon.views.ComItemOption
import com.eiviayw.libcommon.views.ComTopBar
import com.eiviayw.libcommon.views.EmptyViewV1
import com.eiviayw.libdraw.DrawingViewMode
import com.eiviayw.libcommon.R
import com.eiviayw.libdraw.bean.DefaultDrawItemParam
import com.eiviayw.libdraw.constant.ParamType
import com.eiviayw.library.bean.param.LineDashedParam
import com.eiviayw.library.bean.param.LineParam
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
    val bitmap = viewModel.bitmap.collectAsState().value

    var closeActivity by remember { mutableStateOf(false) }
    var resultOk by remember { mutableStateOf(false) }

    DisposableEffect(closeActivity) {
        Log.d(BaseApplication.TAG, "DrawingHome: setBitmapOption")
        viewModel.setBitmapOption(bitmapOption)

        onDispose {
            Log.d(BaseApplication.TAG, "DrawingHome: onDispose")
            bitmap?.recycle()
        }
    }

    // 监听生命周期的变化，当需要关闭 Activity 时调用 `finish()`
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_RESUME && closeActivity) {
                source.lifecycle.removeObserver(this)
                val currentActivity = source as? androidx.activity.ComponentActivity
                if(resultOk){
                    currentActivity?.setResult(RESULT_OK)
                }
                currentActivity?.finish()
            }
        }
    })

    Column(modifier.fillMaxSize()) {
        ComTopBar(title = stringResource(id = R.string.print_source_data), actionTitle = stringResource(id = R.string.add)) {
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
                    ParamType.SINGLE_TEXT -> viewModel.showSingleTextView(true)
                    ParamType.MULTI_TEXT -> viewModel.showMultiElementView(true)
                    ParamType.LINE -> viewModel.showLineElementView(true)
                    ParamType.BITMAP -> viewModel.showBitmapOptionView(true)
                    ParamType.IMAGE -> {

                    }

                    else -> {}
                }
            }
        }

        Row(
            modifier = Modifier.weight(1f)
        ) {
            if (bitmap == null) {
                EmptyViewV1(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(10.dp)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.medium)
                        .drawBehind {
                            drawRect(
                                color = ColorE9E9E9,
                                style = Stroke(
                                    width = 10f,
                                    cap = StrokeCap.Round
                                )
                            )
                        },
                    tips = stringResource(id = R.string.content_preview_area)
                )
            } else {
                Image(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(),
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.content_preview)
                )
            }

            if (paramList.isEmpty()) {
                EmptyViewV1(
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(10.dp)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.medium)
                        .drawBehind {
                            drawRect(
                                color = ColorE9E9E9,
                                style = Stroke(
                                    width = 10f,
                                    cap = StrokeCap.Round
                                )
                            )
                        },
                    tips = stringResource(id = R.string.element_list_area)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(0.3f)
                        .padding(10.dp)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.medium)
                        .drawBehind {
                            drawRect(
                                color = ColorE9E9E9,
                                style = Stroke(
                                    width = 10f,
                                    cap = StrokeCap.Round
                                )
                            )
                        }
                ) {
                    items(paramList.toList()) { item ->
                        when (item) {
                            is TextParam -> ComItemOption(title = stringResource(id = R.string.text_element), value = item.text)
                            is MultiElementParam -> ComItemOption(title = stringResource(id = R.string.multi_element), value = "")
                            is LineParam -> ComItemOption(title = stringResource(id = R.string.line_element), value = stringResource(id = R.string.line))
                            is LineDashedParam -> ComItemOption(
                                title = stringResource(id = R.string.line_element),
                                value = stringResource(id = R.string.dash)
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ComButton(
                value = stringResource(id = R.string.auto_add_single_text_element),
                containerColor = Color177FF,
                click = {
                    viewModel.addParam(DefaultDrawItemParam.getInstance().getSingleTextParam())
                }
            )

            ComButton(
                value = stringResource(id = R.string.auto_add_double_text_element),
                containerColor = Color177FF,
                click = {
                    viewModel.addParam(DefaultDrawItemParam.getInstance().getPairTextParam())
                }
            )

            ComButton(
                value = stringResource(id = R.string.auto_add_tripe_element),
                containerColor = Color177FF,
                click = {
                    viewModel.addParam(DefaultDrawItemParam.getInstance().getTripleTextParam())
                }
            )

            ComButton(
                value = stringResource(id = R.string.auto_add_dash_line_element),
                containerColor = Color177FF,
                click = {
                    viewModel.addParam(DefaultDrawItemParam.getInstance().getDashLineParam())
                }
            )

            ComButton(
                value = stringResource(id = R.string.auto_add_line_element),
                containerColor = Color177FF,
                click = {
                    viewModel.addParam(DefaultDrawItemParam.getInstance().getLineParam())
                }
            )

            ComButton(
                value = stringResource(id = R.string.cancel),
                containerColor = ColorE9E9E9,
                click = {
                    closeActivity = true
                }
            )

            ComButton(
                value = stringResource(id = R.string.save),
                containerColor = OrangeFF870D,
                click = {
                    viewModel.getResultData()?.let {
                        WeakDataHolder.getInstance().saveData(WeakDataHolder.RESULT_DATA, it)
                        resultOk = true
                        closeActivity = true
                    }
                }
            )
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

    if (viewState.showLineElementDialog) {
        ModifyLineDialog(
            onCancel = {
                viewModel.showLineElementView(false)
            },
            confirm = {
                viewModel.addParam(it)
                viewModel.showLineElementView(false)
            }
        )
    }

    if (viewState.showBitmapOptionView) {
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