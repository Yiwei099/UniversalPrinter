package com.eiviayw.libdraw.ui

import android.graphics.Paint
import android.text.TextUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.eiviayw.libcommon.R
import com.eiviayw.libcommon.theme.ColorCF5EEF
import com.eiviayw.libcommon.theme.ColorE9E9E9
import com.eiviayw.libcommon.views.ChoseOption
import com.eiviayw.libcommon.views.ComButton
import com.eiviayw.libcommon.views.ComItemOption
import com.eiviayw.libcommon.views.ComLine
import com.eiviayw.libdraw.constant.TextAlignType
import com.eiviayw.libdraw.constant.TextTypeFace
import com.eiviayw.libdraw.utils.DrawLibUtils
import com.eiviayw.library.bean.param.BaseParam
import com.eiviayw.library.bean.param.LineDashedParam
import com.eiviayw.library.bean.param.LineParam
import com.eiviayw.library.bean.param.MultiElementParam
import com.eiviayw.library.bean.param.TextParam
import com.eiviayw.library.draw.BitmapOption

@Composable
fun ModifySingleTextDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
    onConfirm: (BaseParam) -> Unit = {},
    autoWidth: Boolean = true
) {
    var textAlignDialog by remember { mutableStateOf(false) }
    var textFaceDialog by remember { mutableStateOf(false) }
    var autoWarp by remember { mutableStateOf(true) }

    var widthWeight by remember { mutableStateOf("1") }
    var paramText by remember { mutableStateOf("") }
    var textSize by remember { mutableStateOf("26") }
    var textAlign by remember { mutableStateOf(TextAlignType.ALIGN_START) }
    var textFace by remember { mutableStateOf(TextTypeFace.DEFAULT) }

    val sizeDp = DpSize(
        LocalConfiguration.current.screenWidthDp.dp,
        LocalConfiguration.current.screenHeightDp.dp
    )
    Dialog(onDismissRequest = { onCancel.invoke() }) {
        Surface(
            modifier = modifier.wrapContentHeight(),
            color = Color.White,
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                modifier = Modifier.padding(10.dp, 0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.modify_text_element),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 6.dp),
                    textAlign = TextAlign.Center
                )

                LazyColumn(
                    modifier = Modifier.height(sizeDp.height * 0.7f)
                ) {
                    item {
                        if (autoWidth) {
                            OutlinedTextField(
                                value = paramText,
                                onValueChange = {
                                    paramText = it
                                },
                                label = { Text(text = stringResource(id = R.string.text_content)) },
                                modifier = Modifier.padding(0.dp, 20.dp)
                            )
                        } else {
                            Row {
                                OutlinedTextField(
                                    value = paramText,
                                    onValueChange = {
                                        paramText = it
                                    },
                                    label = { Text(text = stringResource(id = R.string.text_content)) },
                                    modifier = Modifier
                                        .padding(0.dp, 20.dp)
                                        .weight(0.5f)
                                )

                                Text(
                                    modifier = Modifier.weight(0.3f),
                                    text = ""
                                )

                                OutlinedTextField(
                                    value = widthWeight,
                                    onValueChange = {
                                        widthWeight = it
                                    },
                                    label = { Text(text = stringResource(id = R.string.width_weight)) },
                                    modifier = Modifier
                                        .padding(0.dp, 20.dp)
                                        .weight(0.5f),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal
                                    )
                                )
                            }
                        }

                    }

                    item {
                        Row(
                            modifier = Modifier.padding(10.dp, 0.dp)
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(id = R.string.text_size),
                                fontSize = 14.sp,
                            )
                            Icon(
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        if (textSize.toInt() > 0) {
                                            textSize = (textSize.toInt() - 2).toString()
                                        }
                                    },
                                painter = painterResource(id = R.mipmap.sub),
                                contentDescription = stringResource(id = R.string.sub),
                            )
                            Text(
                                modifier = Modifier.padding(20.dp, 0.dp),
                                text = textSize,
                                fontSize = 14.sp,
                            )
                            Icon(
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        textSize = (textSize.toInt() + 2).toString()
                                    },
                                painter = painterResource(id = R.mipmap.plus),
                                contentDescription = stringResource(id = R.string.add_v1)
                            )
                        }
                        ComLine(
                            modifier = Modifier.padding(10.dp, 4.dp)
                        )
                    }

                    item {
                        ComItemOption(
                            title = stringResource(id = R.string.align_type),
                            value = DrawLibUtils.getInstance().getTextAlignName(textAlign.index)
                        ) {
                            textAlignDialog = true
                        }
                    }

                    item {
                        ComItemOption(
                            title = stringResource(id = R.string.text_style),
                            value = textFace.label
                        ) {
                            textFaceDialog = true
                        }
                    }

                    item {
                        ChoseOption(
                            title = stringResource(id = R.string.auto_warp),
                            chooseState = autoWarp
                        ) {
                            autoWarp = !autoWarp
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ComButton(
                        value = stringResource(id = R.string.cancel_v1),
                        containerColor = ColorE9E9E9,
                        click = { onCancel.invoke() }
                    )
                    ComButton(
                        value = stringResource(id = R.string.confirm),
                        click = {
                            onConfirm.invoke(TextParam(
                                text = paramText,
                                align = textAlign.index,
                                autoWrap = autoWarp,
                                weight = if (TextUtils.isEmpty(widthWeight)) 1.0 else widthWeight.toDouble()
                            ).apply {
                                size = textSize.toFloat()
                                typeface = textFace.face
                            })
                        }
                    )
                }
            }

            if (textAlignDialog) {
                Dialog(onDismissRequest = { textAlignDialog = false }) {
                    TextAlignDialog(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(50.dp, 20.dp),
                        defaultChooseMode = textAlign,
                        cancel = {
                            textAlignDialog = false
                        },
                        confirm = {
                            textAlign = it
                            textAlignDialog = false
                        }
                    )
                }
            }

            if (textFaceDialog) {
                Dialog(onDismissRequest = { textFaceDialog = false }) {
                    TextTypeFaceDialog(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(50.dp, 20.dp),
                        defaultChooseMode = textFace,
                        cancel = {
                            textFaceDialog = false
                        },
                        confirm = {
                            textFace = it
                            textFaceDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ModifyMultiTextDialog(
    modifier: Modifier = Modifier,
    cancel: () -> Unit = {},
    confirm: (BaseParam) -> Unit = {}
) {
    var elementList by remember { mutableStateOf(emptyList<BaseParam>()) }
    var showModifyItemDialog by remember {
        mutableStateOf(false)
    }

    val sizeDp = DpSize(
        LocalConfiguration.current.screenWidthDp.dp,
        LocalConfiguration.current.screenHeightDp.dp
    )

    Dialog(onDismissRequest = cancel) {
        Surface(
            modifier = modifier,
            color = Color.White,
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                modifier = Modifier.padding(10.dp, 0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.modify_multi_element),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 6.dp),
                    textAlign = TextAlign.Center
                )

                LazyColumn(
                    modifier = Modifier.height(sizeDp.height * 0.7f)
                ) {
                    items(elementList) { item ->
                        when (item) {
                            is TextParam -> {
                                ComItemOption(title = stringResource(id = R.string.text_element), value = item.text)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ComButton(
                        value = stringResource(id = R.string.cancel_v1),
                        containerColor = ColorE9E9E9,
                        click = { cancel.invoke() }
                    )


                    ComButton(
                        value = stringResource(id = R.string.confirm),
                        click = {
                            val size = elementList.size
                            if (size < 1) {
                                cancel.invoke()
                                return@ComButton
                            }
                            confirm.invoke(
                                MultiElementParam(
                                    param1 = elementList[0],
                                    param2 = if (1 <= size - 1) elementList[1] else BaseParam(),
                                    param3 = if (2 <= size - 1) elementList[2] else BaseParam(),
                                )
                            )
                        }
                    )

                    if (elementList.size < 3) {
                        ComButton(
                            value = stringResource(id = R.string.add),
                            containerColor = ColorCF5EEF,
                            click = { showModifyItemDialog = true }
                        )
                    }
                }
            }

            if (showModifyItemDialog) {
                ModifySingleTextDialog(
                    autoWidth = false,
                    onCancel = {
                        showModifyItemDialog = false
                    },
                    onConfirm = {
                        elementList += listOf(it)
                        showModifyItemDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun TextAlignDialog(
    modifier: Modifier = Modifier,
    defaultChooseMode: TextAlignType = TextAlignType.ALIGN_START,
    cancel: () -> Unit = {},
    confirm: (TextAlignType) -> Unit = {}
) {
    var chooseMode by remember { mutableStateOf(defaultChooseMode) }

    Surface(
        modifier = modifier,
        color = Color.White,
        shape = MaterialTheme.shapes.small
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.choose_align_type),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 6.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(mutableListOf<TextAlignType>().apply {
                    add(TextAlignType.ALIGN_START)
                    add(TextAlignType.ALIGN_CENTER)
                    add(TextAlignType.ALIGN_END)
                }) { item ->
                    ChoseOption(
                        title = item.label,
                        chooseState = item.index == chooseMode.index,
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
                    value = stringResource(id = R.string.cancel_v1),
                    containerColor = ColorE9E9E9,
                    click = cancel
                )
                ComButton(
                    value = stringResource(id = R.string.confirm),
                    click = {
                        confirm.invoke(chooseMode)
                    }
                )
            }
        }
    }
}

@Composable
fun TextTypeFaceDialog(
    modifier: Modifier = Modifier,
    defaultChooseMode: TextTypeFace = TextTypeFace.DEFAULT,
    cancel: () -> Unit = {},
    confirm: (TextTypeFace) -> Unit = {}
) {

    var chooseMode by remember { mutableStateOf(defaultChooseMode) }
    Dialog(onDismissRequest = cancel) {
        Surface(
            modifier = modifier,
            color = Color.White,
            shape = MaterialTheme.shapes.small
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.choose_text_style),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 6.dp),
                    textAlign = TextAlign.Center
                )

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(mutableListOf<TextTypeFace>().apply {
                        add(TextTypeFace.DEFAULT)
                        add(TextTypeFace.BOLD)
                    }) { item ->
                        ChoseOption(
                            title = item.label,
                            chooseState = item.face == chooseMode.face,
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
                        value = stringResource(id = R.string.cancel_v1),
                        containerColor = ColorE9E9E9,
                        click = cancel
                    )
                    ComButton(
                        value = stringResource(id = R.string.confirm),
                        click = {
                            confirm.invoke(chooseMode)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BitmapOptionView(
    modifier: Modifier = Modifier,
    defaultData: BitmapOption = BitmapOption(),
    cancel: () -> Unit = {},
    confirm: (BitmapOption) -> Unit = {}
) {
    var maxWidth by remember { mutableStateOf(defaultData.maxWidth.toString()) }
    var maxHeight by remember { mutableStateOf(defaultData.maxHeight.toString()) }
    var startIndentation by remember {
        mutableStateOf(
            defaultData.startIndentation.toInt().toString()
        )
    }
    var endIndentation by remember { mutableStateOf(defaultData.endIndentation.toInt().toString()) }

    var openAlias by remember { mutableStateOf(defaultData.antiAlias) }
    var followEffectItem by remember { mutableStateOf(defaultData.followEffectItem) }

    Dialog(onDismissRequest = cancel) {
        Surface(
            modifier = modifier,
            color = Color.White,
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                modifier = Modifier.padding(10.dp, 0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.bitmap_option_set),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 6.dp),
                    textAlign = TextAlign.Center
                )
                LazyColumn {
                    item {
                        Row {
                            OutlinedTextField(
                                value = maxWidth,
                                onValueChange = {
                                    maxWidth = it
                                },
                                label = { Text(text = stringResource(id = R.string.bitmap_max_width)) },
                                modifier = Modifier
                                    .padding(0.dp, 20.dp)
                                    .weight(0.5f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Text(
                                modifier = Modifier.weight(0.3f),
                                text = ""
                            )

                            OutlinedTextField(
                                value = maxHeight,
                                onValueChange = {
                                    maxHeight = it
                                },
                                label = { Text(text = stringResource(id = R.string.bitmap_max_height)) },
                                modifier = Modifier
                                    .padding(0.dp, 20.dp)
                                    .weight(0.5f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedTextField(
                                value = startIndentation,
                                onValueChange = {
                                    startIndentation = it
                                },
                                label = { Text(text = stringResource(id = R.string.start_indentation)) },
                                modifier = Modifier
                                    .padding(0.dp, 20.dp)
                                    .weight(0.5f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Text(
                                modifier = Modifier.weight(0.3f),
                                text = ""
                            )

                            OutlinedTextField(
                                value = endIndentation,
                                onValueChange = {
                                    endIndentation = it
                                },
                                label = { Text(text = stringResource(id = R.string.end_indentation)) },
                                modifier = Modifier
                                    .padding(0.dp, 20.dp)
                                    .weight(0.5f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                    item {
                        ChoseOption(title = stringResource(id = R.string.open_alias), chooseState = openAlias) {
                            openAlias = !openAlias
                        }
                    }
                    item {
                        ChoseOption(
                            title = stringResource(id = R.string.followEffectItem_tips),
                            chooseState = followEffectItem
                        ) {
                            followEffectItem = !followEffectItem
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ComButton(
                        value = stringResource(id = R.string.cancel_v1),
                        containerColor = ColorE9E9E9,
                        click = cancel
                    )
                    ComButton(
                        value = stringResource(id = R.string.confirm),
                        click = {
                            confirm.invoke(
                                BitmapOption(
                                    maxWidth = maxWidth.toInt(),
                                    maxHeight = maxHeight.toInt(),
                                    startIndentation = if (TextUtils.isEmpty(startIndentation)) 0f else startIndentation.toFloat(),
                                    endIndentation = if (TextUtils.isEmpty(endIndentation)) 0f else endIndentation.toFloat(),
                                    antiAlias = openAlias,
                                    followEffectItem = followEffectItem
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ModifyLineDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    confirm: (BaseParam) -> Unit
){

    var isDishLine by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = { onCancel.invoke() }) {
        Surface(
            modifier = modifier.wrapContentHeight(),
            color = Color.White,
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                modifier = Modifier.padding(10.dp, 0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.modify_line_element),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 6.dp),
                    textAlign = TextAlign.Center
                )

                ChoseOption(title = stringResource(id = R.string.line_is_dash), chooseState = isDishLine){
                    isDishLine = !isDishLine
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ComButton(
                        value = stringResource(id = R.string.cancel_v1),
                        containerColor = ColorE9E9E9,
                        click = onCancel
                    )
                    ComButton(
                        value = stringResource(id = R.string.confirm),
                        click = {
                            confirm.invoke(
                                if (isDishLine){
                                    LineDashedParam().apply {
                                        strokeWidth = 2f
                                        style = Paint.Style.STROKE
                                    }
                                }else{
                                    LineParam().apply {
                                        strokeWidth = 2f
                                        style = Paint.Style.STROKE
                                    }
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}