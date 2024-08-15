package com.eiviayw.libdraw.utils

import com.eiviayw.library.Constant

class DrawLibUtils private constructor() {
    companion object {
        @Volatile
        private var instance: DrawLibUtils? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: DrawLibUtils().also { instance = it }
            }
    }

    fun getTextAlignName(align: Int): String {
        return when(align){
            Constant.Companion.Align.ALIGN_CENTER -> "居中"
            Constant.Companion.Align.ALIGN_END -> "靠右(结束方向)"
            else -> "靠左(开始方向)"
        }
    }
}