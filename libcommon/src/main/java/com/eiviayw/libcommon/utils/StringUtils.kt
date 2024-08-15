package com.eiviayw.libcommon.utils

import java.util.Random


class StringUtils private constructor(){
    companion object{
        @Volatile
        private var instance: StringUtils? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: StringUtils().also { instance = it }
            }
    }

    fun getRandomNum(num: Int): String {
        val base = "0123456789"
        val random = Random()
        val sb = StringBuffer()
        for (i in 0 until num) {
            val number: Int = random.nextInt(base.length)
            sb.append(base[number])
        }
        return sb.toString()
    }
}