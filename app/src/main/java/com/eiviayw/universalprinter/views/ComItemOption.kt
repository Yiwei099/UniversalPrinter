package com.eiviayw.universalprinter.views

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eiviayw.universalprinter.R
import com.eiviayw.universalprinter.ui.theme.Black141414

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
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.padding(10.dp,0.dp),
        //点击事件相应
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(0.dp,10.dp,0.dp,10.dp),
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
                contentDescription = "下一步",
                modifier = Modifier.size(12.dp)
            )
        }
        ComLine()
    }
}

@Preview(showBackground = true)
@Composable
fun ComItemOptionPreview() {
    ComItemOption(title = "周杰伦", value = "七里香", onClick = {})
}