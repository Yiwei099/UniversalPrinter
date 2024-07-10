package com.eiviayw.universalprinter.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eiviayw.universalprinter.ui.theme.GrayE3E3E3

/**
 * 横线
 */
@Composable
fun ComLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .border(width = 0.5.dp, color = GrayE3E3E3)
    )
}

/**
 * 竖线
 */
@Composable
fun ComVerticalLine() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(10.dp,0.dp,16.dp,0.dp)
            .width(1.dp)
            .border(width = 0.5.dp, color = GrayE3E3E3)
    )
}

@Preview(showBackground = true)
@Composable
fun ComLinePreview() {
    ComLine()
}