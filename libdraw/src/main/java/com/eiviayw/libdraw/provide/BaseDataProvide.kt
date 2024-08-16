package com.eiviayw.libdraw.provide

import com.eiviayw.library.bean.param.BaseParam
import com.eiviayw.library.draw.BitmapOption
import com.eiviayw.library.provide.BaseProvide

class BaseDataProvide(
    private val bitmapOption: BitmapOption,
    private val data: List<BaseParam>
) : BaseProvide(bitmapOption) {

    private var bitmapDataByteArr: ByteArray? = null

    fun notifyDraw():ByteArray {
        if (bitmapDataByteArr == null){
            bitmapDataByteArr = startDraw(data)
        }
        return bitmapDataByteArr!!
    }
}