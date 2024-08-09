package com.eiviayw.libdraw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eiviayw.libcommon.theme.UniversalPrinterTheme
import com.eiviayw.libcommon.utils.WeakDataHolder
import com.eiviayw.libdraw.ui.DrawingHome
import com.eiviayw.library.draw.BitmapOption

class DrawingActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        setContent {
            UniversalPrinterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        DrawingHome(bitmapOption = WeakDataHolder.getInstance().getData(WeakDataHolder.DATA,BitmapOption()))
                    }
                }
            }
        }
    }

}