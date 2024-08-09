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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eiviayw.libcommon.R
import com.eiviayw.libcommon.theme.Black141414

@Composable
fun ChoseOption(
    title:String = "",
    chooseState:Boolean = false,
    click:()->Unit = {}
) {
    Column(
        modifier = Modifier.padding(10.dp,0.dp).clickable { click.invoke() },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(0.dp,10.dp,0.dp,10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                color = Black141414,
            )
            Image(
                painter = painterResource(id = if (chooseState) R.mipmap.choose else R.mipmap.un_choose),
                contentDescription = "下一步",
                modifier = Modifier.size(14.dp)
            )
        }
        ComLine()
    }
}