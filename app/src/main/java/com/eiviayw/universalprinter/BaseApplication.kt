package com.eiviayw.universalprinter

import android.app.Application

class BaseApplication : Application() {
    companion object {
        @Volatile
        private var instance: BaseApplication? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: BaseApplication().also { instance = it }
            }

        private const val TAG = "BaseApplication"
    }
}