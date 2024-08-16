package com.eiviayw.libdraw.bean

import android.graphics.Paint
import com.eiviayw.library.Constant
import com.eiviayw.library.bean.param.LineDashedParam
import com.eiviayw.library.bean.param.LineParam
import com.eiviayw.library.bean.param.MultiElementParam
import com.eiviayw.library.bean.param.TextParam

class DefaultDrawItemParam private constructor() {
    companion object {
        @Volatile
        private var instance: DefaultDrawItemParam? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: DefaultDrawItemParam().also { instance = it }
            }
    }

    fun getSingleTextParam() = TextParam(
        text = "华为官方商城",
        align = Constant.Companion.Align.ALIGN_CENTER,
    )

    fun getPairTextParam() = MultiElementParam(
        param1 = TextParam(
            text = "订单编号",
            weight = 0.5,
            align = Constant.Companion.Align.ALIGN_START
        ).apply {
            size = 18f
        },
        param2 = TextParam(
            text = "20240816007",
            weight = 0.5,
            align = Constant.Companion.Align.ALIGN_END
        ).apply {
            size = 18f
        }
    )

    fun getTripleTextParam() = MultiElementParam(
        param1 = TextParam(
            text = "华为Mate60 Pro",
            weight = 0.6,
            align = Constant.Companion.Align.ALIGN_START
        ).apply {
            size = 18f
        },
        param2 = TextParam(
            text = "x1",
            weight = 0.2,
            align = Constant.Companion.Align.ALIGN_END
        ).apply {
            size = 18f
        },
        param3 = TextParam(
            text = "￥6,999",
            weight = 0.2,
            align = Constant.Companion.Align.ALIGN_END
        ).apply {
            size = 18f
        }
    )

    fun getDashLineParam() = LineDashedParam().apply {
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    fun getLineParam() = LineParam()
}