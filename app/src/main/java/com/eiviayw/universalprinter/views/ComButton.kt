package com.eiviayw.universalprinter.views

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.eiviayw.universalprinter.ui.theme.White

@Composable
fun ComButton(
    modifier: Modifier = Modifier,
    click: () -> Unit = {},
    value: String = "",
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = White,
) {
    Button(
        //点击事件
        onClick = click,
        modifier = modifier,
        //颜色表现
        colors = ButtonDefaults.buttonColors(
            //容器颜色
            containerColor = containerColor,
            //内容颜色
            contentColor = contentColor
        ),
        //圆角尺寸
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComButtonPreview() {
    ComButton(value = "确定")
}