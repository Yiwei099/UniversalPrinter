package com.eiviayw.libdraw.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eiviayw.libcommon.views.ComTopBar
import com.eiviayw.libdraw.DrawingViewMode
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.library.draw.BitmapOption

@Composable
fun DrawingHome(
    modifier: Modifier = Modifier,
    bitmapOption: BitmapOption = BitmapOption(),
    viewModel: DrawingViewMode = viewModel()
){

    Column(modifier) {
        ComTopBar(title = "打印数据源", actionTitle = "编辑") {

        }


    }
}