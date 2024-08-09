package com.eiviayw.libdraw.provide

import android.graphics.Typeface
import com.eiviayw.library.Constant
import com.eiviayw.library.bean.param.BaseParam
import com.eiviayw.library.bean.param.TextParam
import com.eiviayw.library.draw.BitmapOption
import com.eiviayw.library.provide.BaseProvide

/**
 * 指路：https://github.com/Yiwei099
 *
 * Created with Android Studio.
 * @Author: YYW
 * @Date: 2023-11-26 20:39
 * @Version Copyright (c) 2023, Android Engineer YYW All Rights Reserved.
 *
 * 标签数据提供者
 */

class LabelProvide(
    bitMapOption:BitmapOption,
):BaseProvide(bitMapOption){
    private var tscBitmapArray: ByteArray? = null

    fun getData(): ByteArray {
        if (tscBitmapArray == null) {
            tscBitmapArray = start()
        }
        return tscBitmapArray!!
    }

    private fun start(): ByteArray {
        val params = covertDrawParam()
        return startDraw(params)
    }

    private fun covertDrawParam() = mutableListOf<BaseParam>().apply {
        add(
            TextParam(
                text = "李宁"
            ).apply {
                size = 28f
                typeface = Typeface.DEFAULT_BOLD
                align = Constant.Companion.Align.ALIGN_CENTER
            }
        )

        add(
            TextParam(
                text = "毛衣"
            ).apply {
                size = 26f
                typeface = Typeface.DEFAULT
            }
        )

        add(
            TextParam(
                text = "尺码：S"
            ).apply {
                size = 26f
            }
        )

        add(
            TextParam(
                text = "颜色：黑色"
            ).apply {
                size = 26f
            }
        )

        add(
            TextParam(
                text = "零售价：￥100"
            ).apply {
                size = 26f
            }
        )

        add(
            TextParam(
                text = "折后价：￥90"
            ).apply {
                size = 26f
            }
        )

        add(
            TextParam(
                text = "款号：001"
            ).apply {
                size = 26f
            }
        )

        add(
            TextParam(
                text = "品牌：李宁"
            ).apply {
                size = 26f
            }
        )

        add(
            TextParam(
                text = "产品标准：GB/T22-21894576"
            ).apply {
                size = 26f
            }
        )

        add(
            TextParam(
                text = "质量等级：优"
            ).apply {
                size = 26f
            }
        )
    }

}