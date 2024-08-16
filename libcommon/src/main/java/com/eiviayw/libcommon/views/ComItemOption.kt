package com.eiviayw.libcommon.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eiviayw.libcommon.R
import com.eiviayw.libcommon.theme.Black141414

/**
 * 选项
 * @param title 标题
 * @param value 值
 * @param onClick 点击事件
 */
@Composable
fun ComItemOption(
    title: String,
    value: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(10.dp, 0.dp)
            .clickable {
                onClick.invoke()
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = title,
                fontSize = 14.sp,
                color = Black141414,
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Black141414,
                modifier = Modifier.weight(1.0f),
                textAlign = TextAlign.End
            )
            Image(
                painter = painterResource(id = R.mipmap.next),
                contentDescription = stringResource(id = R.string.next),
                modifier = Modifier.size(12.dp)
            )
        }
        ComLine()
    }
}

@Composable
fun StepOption(
    modifier: Modifier = Modifier,
    stepTips: String = "",
    stepTitle: String = "",
    value: String = "",
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Text(text = stepTips)
        ComItemOption(title = stepTitle, value = value, onClick = onClick)
    }
}