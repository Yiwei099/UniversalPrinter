package com.eiviayw.universalprinter.util

import android.graphics.Bitmap

class BitmapUtils private constructor() {
    companion object {
        @Volatile
        private var instance: BitmapUtils? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: BitmapUtils().also { instance = it }
            }
    }

    fun bitmapToBytes(bitmap: Bitmap): List<Byte> {
        val width = bitmap.width
        val height = bitmap.height
        val bytes: MutableList<Byte> = ArrayList()

        val p = IntArray(8)
        for (i in 0 until height) {
            for (j in 0 until width / 8) {
                for (k in 0..7) {
                    val grey = bitmap.getPixel(j * 8 + k, i)
                    val red = ((grey and 0x00FF0000) shr 16)
                    val green = ((grey and 0x0000FF00) shr 8)
                    val blue = (grey and 0x000000FF)
                    val gray = (0.29900 * red + 0.58700 * green + 0.11400 * blue).toInt() // 灰度转化公式
                    if (gray <= 180) {
                        p[k] = 0
                    } else {
                        p[k] = 1
                    }
                }

                val value =
                    p[0] * 128 + p[1] * 64 + p[2] * 32 + p[3] * 16 + p[4] * 8 + p[5] * 4 + p[6] * 2 + p[7]
                bytes.add(value.toByte())
            }
        }
        return bytes
    }
}