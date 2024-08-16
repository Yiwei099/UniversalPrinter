package com.eiviayw.libdraw

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.eiviayw.libdraw.bean.state.DrawViewState
import com.eiviayw.libdraw.provide.BaseDataProvide
import com.eiviayw.library.bean.param.BaseParam
import com.eiviayw.library.draw.BitmapOption
import com.eiviayw.library.util.BitmapUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DrawingViewMode:ViewModel() {

    private val _sourceDataList = MutableStateFlow(mutableListOf<BaseParam>())
    val sourceDataList = _sourceDataList.asStateFlow()

    private val _drawViewState = MutableStateFlow(DrawViewState())
    val drawViewState: StateFlow<DrawViewState> = _drawViewState

    private val _bitmapOption = MutableStateFlow(BitmapOption())
    val bitmapOption = _bitmapOption.asStateFlow()

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap = _bitmap.asStateFlow()

    private var dataSourceByByteArr :ByteArray? = null

    fun addParam(item:BaseParam){
        _sourceDataList.value += listOf(item)
        notifyPreViewBitMap()
    }

    fun removeParam(item: BaseParam){
        _sourceDataList.value -= listOf(item).toSet()
        notifyPreViewBitMap()
    }

    fun showParamTypePopupView(show:Boolean){
        _drawViewState.value = _drawViewState.value.copy(showPopupMenu = show)
    }

    fun showSingleTextView(show:Boolean){
        _drawViewState.value = _drawViewState.value.copy(showSingleTextDialog = show)
    }

    fun showMultiElementView(show:Boolean){
        _drawViewState.value = _drawViewState.value.copy(showMultiElementDialog = show)
    }

    fun showBitmapOptionView(show:Boolean){
        _drawViewState.value = _drawViewState.value.copy(showBitmapOptionView = show)
    }

    fun showLineElementView(show:Boolean){
        _drawViewState.value = _drawViewState.value.copy(showLineElementDialog = show)
    }

    fun setBitmapOption(bitmapOption: BitmapOption){
        _bitmapOption.value = bitmapOption
        notifyPreViewBitMap()
    }

    fun getBitmapOption():BitmapOption{
        return _bitmapOption.value
    }

    private fun notifyPreViewBitMap() {
        dataSourceByByteArr = BaseDataProvide(_bitmapOption.value, sourceDataList.value).notifyDraw()

        dataSourceByByteArr?.let {
            _bitmap.value = BitmapUtils.getInstance().byteDataToBitmap(it)
        }
    }

    fun getResultData():Pair<ByteArray,BitmapOption>? {
        if (dataSourceByByteArr == null){
            return null
        }
        return Pair(dataSourceByByteArr!!, _bitmapOption.value)
    }
}