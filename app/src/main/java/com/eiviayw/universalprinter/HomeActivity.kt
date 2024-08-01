package com.eiviayw.universalprinter

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.eiviayw.universalprinter.ui.Home
import com.eiviayw.universalprinter.ui.theme.UniversalPrinterTheme
import com.eiviayw.universalprinter.util.PermissionUtil

class HomeActivity: ComponentActivity() {

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                //权限申请通过
                Log.d(BaseApplication.TAG, "程序权限列表正常")
            }else{
                // 权限被用户拒绝，需要提示用户或者自动回退
                Toast.makeText(this, "蓝牙权限被拒绝，无法使用蓝牙功能", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initData()
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
                        Home()
                    }
                }
            }
        }
    }

    private fun initData(){
        PermissionUtil.getInstance().checkPermissionV1(
            permissions = mutableListOf<String>().apply {
            add(Manifest.permission.BLUETOOTH)
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            PermissionUtil.getInstance().getPermissionFromSDKVersionS()
        }, onSuccess = {
                Log.d(BaseApplication.TAG, "initData: Permission all pass")
        }, onFailure = {
            //申请权限
            permReqLauncher.launch(it.toTypedArray())
        })
    }
}