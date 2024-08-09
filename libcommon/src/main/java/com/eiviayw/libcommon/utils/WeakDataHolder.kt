package com.eiviayw.libcommon.utils

import android.util.Log
import java.lang.ref.WeakReference

/**
 * author ：YYW
 * date : 2023/4/21 0021
 * description : 大数据传递帮助，解决 Intent 传递数据量限制的问题
 */
class WeakDataHolder private constructor() {
    companion object {

        private val map = mutableMapOf<String,WeakReference<Any>>()
        @Volatile
        private var instance: WeakDataHolder? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: WeakDataHolder().also { instance = it }
            }

        const val DATA = "data"
        const val DATA_1 = "data_1"
        const val DATA_2 = "data_2"
        const val RESULT_DATA = "result_data"
        const val RESULT_DATA_1 = "result_data1"
    }

    fun saveData(id:String,data:Any) {
        map[id] = WeakReference(data)
    }

    fun <T>getData(id:String,nullReturn:T):T {
        val weakReference = map[id]
        val result = weakReference?.get() ?: return nullReturn
        //成功取出结果后进行类型转换
        try {
            result as T
            //从字典中移除，防止旧数据残留，再被别的地方Get出来，出现类型转换失败等异常
            map.remove(id)
            return result
        }catch (e:Exception){
            Log.d("WeakDataHolder","WeakDataHolder getData(${id})异常")
        }
        return nullReturn
    }
}