package com.eiviayw.libdraw

import androidx.lifecycle.ViewModel
import com.eiviayw.libdraw.bean.state.DrawViewState
import com.eiviayw.libdraw.provide.EscDataProvide
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

    fun addParam(item:BaseParam){
        _sourceDataList.value += listOf(item)
    }

    fun removeParam(item: BaseParam){
        _sourceDataList.value -= listOf(item)
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

    fun setBitmapOption(bitmapOption: BitmapOption){
        _bitmapOption.value = bitmapOption
    }

    fun getBitmapOption():BitmapOption{
        return _bitmapOption.value
    }
}