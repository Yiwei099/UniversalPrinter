package com.eiviayw.universalprinter.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eiviayw.libcommon.BaseApplication

class PermissionUtil private constructor() {
    companion object {
        @Volatile
        private var instance: PermissionUtil? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: PermissionUtil().also { instance = it }
            }
    }

    /**
     * 判断是否权限都已授权，存在未授权则进行申请授权
     * @param activity
     * @param permissions 权限列表
     * @param requestCode
     * @return
     */
    fun checkPermission(
        activity: Activity,
        permissions: List<String>,
        requestCode: Int
    ): Boolean {

        val needRequest = mutableListOf<String>()

        // 循环处理
        for (permission in permissions) {
            // 验证单个权限是否授权-【核心】
            val check = ContextCompat.checkSelfPermission(activity, permission)
            // 判断发现存在某一个未授权
            if (check != PackageManager.PERMISSION_GRANTED) {
                needRequest.add(permission)
            }
        }

        // 存在某一个未授权，重新进行申请权限
        if (needRequest.isNotEmpty()) {

            // 执行权限列表的申请-【核心】
            ActivityCompat.requestPermissions(activity, needRequest.toTypedArray(), requestCode)
            return false
        }
        return true
    }

    /**
     * 判断是否权限都已授权，存在未授权则进行申请授权
     * @param permissions 权限列表
     */
    fun checkPermissionV1(
        permissions: List<String>,
        onSuccess:() -> Unit,
        onFailure:(List<String>) -> Unit
    ) {

        val needRequest = mutableListOf<String>()

        // 循环处理
        for (permission in permissions) {
            // 验证单个权限是否授权-【核心】
            val check = ContextCompat.checkSelfPermission(BaseApplication.getInstance(), permission)
            // 判断发现存在某一个未授权
            if (check != PackageManager.PERMISSION_GRANTED) {
                needRequest.add(permission)
            }
        }

        // 存在某一个未授权，重新进行申请权限
        if (needRequest.isNotEmpty()) {
            // 执行权限列表的申请-【核心】
            onFailure.invoke(needRequest)
            return
        }

        onSuccess.invoke()
    }

    fun getPermissionFromSDKVersionS():List<String>{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            return mutableListOf<String>().apply {
                add(Manifest.permission.BLUETOOTH_SCAN)
                add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }
        return emptyList()
    }
}