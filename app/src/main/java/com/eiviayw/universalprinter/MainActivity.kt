package com.eiviayw.universalprinter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eiviayw.universalprinter.ui.theme.UniversalPrinterTheme
import com.eiviayw.universalprinter.util.UsbBroadcastReceiver
import com.eiviayw.universalprinter.views.ComItemOption
import com.eiviayw.universalprinter.views.ComTopBar
import com.eiviayw.universalprinter.views.ComVerticalLine
import com.eiviayw.universalprinter.views.EmptyView

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    //打印机列表
    private val printerList by lazy {
        mutableListOf<String>().apply {
            add("打印机A")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniversalPrinterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Body()
                    }
                }
            }
        }
    }
}

@Composable
fun PrinterListView(modifier: Modifier = Modifier) {
    val printerList = mutableListOf<String>().apply {
//        add("打印机1")
//        add("打印机2")
//        add("打印机3")
//        add("打印机4")
//        add("打印机5")
    }
    Column(
        modifier
    ) {
        if (printerList.isEmpty()) {
            EmptyView(tips = "尚未添加打印机")
        } else {
            for (printer in printerList) {
                ComItemOption(title = printer, value = "周杰伦", onClick = {
                })
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UniversalPrinterTheme {
        Body()
    }
}

@Composable
fun Body() {
    var createState by remember { mutableStateOf(false) }

    Column {
        ComTopBar(title = "打印助手", actionTitle = if (createState) "返回" else "创建", onActionClick = {
            //创建打印机
            createState = !createState
        })
        Row {
            PrinterListView(modifier = Modifier.weight(3f))
            ComVerticalLine()
            if (createState){
                CreatePrinterView(modifier = Modifier.weight(7f))
            }else{
                EmptyView(tips = "点击右上角【创建】打印机开始体验吧~~")
            }
        }
    }
}