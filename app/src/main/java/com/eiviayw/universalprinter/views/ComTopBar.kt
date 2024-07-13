package com.eiviayw.universalprinter.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eiviayw.universalprinter.ui.theme.White


/**
 * 自定义顶部导航栏
 * @param title 标题
 * @param actionTitle 按钮文字
 * @param onActionClick 按钮点击事件
 *
 */
@Composable
fun ComTopBar(
    title:String,
    actionTitle: String,
    onActionClick: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(6.dp, 0.dp)
    ) {
        Text(
            //文本内容
            text = title,
            //修饰：控件居中
            modifier = Modifier.align(Alignment.Center),
            //文本对齐方式
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
        )
        ComButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            click = onActionClick,
            value = actionTitle
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComTopBarPreview() {
    ComTopBar(title = "标题", actionTitle = "创建", onActionClick = {})
}