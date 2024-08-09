package com.eiviayw.libcommon.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.eiviayw.libcommon.theme.Black141414

/**
 * 空提示视图
 * @param tips 提示内容
 */
@Composable
fun EmptyView(tips:String){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
       Text(
           text = tips,
           modifier = Modifier.align(Alignment.Center),
           fontSize = 14.sp,
           color = Black141414,
           textAlign = TextAlign.Center
       )
    }
}