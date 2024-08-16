package com.eiviayw.libdraw.provide

import android.graphics.Typeface
import com.eiviayw.library.Constant
import com.eiviayw.library.bean.param.BaseParam
import com.eiviayw.library.bean.param.LineDashedParam
import com.eiviayw.library.bean.param.MultiElementParam
import com.eiviayw.library.bean.param.TextParam
import com.eiviayw.library.draw.BitmapOption
import com.eiviayw.library.provide.BaseProvide
import com.eiviayw.libdraw.bean.Goods
import com.eiviayw.libdraw.bean.Order

class EscDataProvide(
    bitMapOption:BitmapOption,
):BaseProvide(bitMapOption) {

    private var bitmapArray:ByteArray? = null
    private val order by lazy { generateOrder() }
    private val goodsData by lazy { generateGoodsData() }
    private val is58 by lazy { bitMapOption.maxWidth < 576 }

    fun getData():ByteArray {
        if (bitmapArray == null){
            bitmapArray = start()
        }
        return bitmapArray!!
    }

    private fun start():ByteArray{
        val params = generateDrawParam(order, goodsData, !is58)
        return startDraw(params)
    }

    private fun generateDrawParam(order: com.eiviayw.libdraw.bean.Order, goodsData: List<com.eiviayw.libdraw.bean.Goods>, isMulti:Boolean): List<BaseParam>
            = mutableListOf<BaseParam>().apply {
        addAll(convertOrderHeader(order,isMulti))
        addAll(if (isMulti) convertOrderGoodsByMulti(goodsData) else convertOrderGoods(goodsData))
        addAll(convertOrderFooter(order))
    }

    private fun convertOrderGoodsByMulti(goodsData: List<com.eiviayw.libdraw.bean.Goods>) = mutableListOf<BaseParam>().apply {
        goodsData.forEachIndexed { index, it ->
            add(
                MultiElementParam(
                    param1 = TextParam(
                        text = it.goodsName,
                        weight = 0.5,
                    ).apply {
                        size = 26f
                        typeface = Typeface.DEFAULT_BOLD
                    },
                    param2 = TextParam(
                        text = "${it.qua}x${it.price}",
                        align = Constant.Companion.Align.ALIGN_END,
                        weight = 0.3,
                    ).apply {
                        size = 26f
                    },
                    param3 = TextParam(
                        text = it.totalPrice,
                        align = Constant.Companion.Align.ALIGN_END,
                        weight = 0.3,
                    ).apply {
                        size = 26f
                        typeface = Typeface.DEFAULT_BOLD
                    }
                ).apply {
                    perLineSpace = 8
                }
            )
        }
    }

    private fun convertOrderFooter(order: com.eiviayw.libdraw.bean.Order) = mutableListOf<BaseParam>().apply {
        add(LineDashedParam().apply {
            perLineSpace = 30
        })

        add(
            MultiElementParam(
                param1 = TextParam(
                    text = "数量",
                    weight = 0.5,
                ).apply {
                    size = 26f
                },
                param2 = TextParam(
                    text = order.qua,
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.5,
                ).apply {
                    size = 26f
                }
            )
        )

        add(
            MultiElementParam(
                param1 = TextParam(
                    text = "小计",
                    weight = 0.5,
                ).apply {
                    size = 26f
                },
                param2 = TextParam(
                    text = order.subTotal,
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.5,
                ).apply {
                    size = 26f
                }
            )
        )

        add(
            MultiElementParam(
                param1 = TextParam(
                    text = "总计",
                    weight = 0.5,
                ).apply {
                    size = 26f
                    typeface = Typeface.DEFAULT_BOLD
                },
                param2 = TextParam(
                    text = order.total,
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.5,
                ).apply {
                    size = 26f
                    typeface = Typeface.DEFAULT_BOLD
                }
            )
        )

        add(
            MultiElementParam(
                param1 = TextParam(
                    text = "现金",
                    weight = 0.5,
                ).apply {
                    size = 26f
                },
                param2 = TextParam(
                    text = order.total,
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.5,
                ).apply {
                    size = 26f
                }
            )
        )

        add(
            TextParam(
                text = order.orderType,
                align = Constant.Companion.Align.ALIGN_CENTER,
            ).apply {
                size = 26f
                typeface = Typeface.DEFAULT_BOLD
            }
        )
    }

    private fun convertOrderGoods(goodsData: List<com.eiviayw.libdraw.bean.Goods>) = mutableListOf<BaseParam>().apply {
        goodsData.forEachIndexed { index, it ->
            add(
                MultiElementParam(
                    param1 = TextParam(
                        text = "${index.plus(1)}.${it.goodsName}",
                        weight = 0.6,
                    ).apply {
                        size = 26f
                        typeface = Typeface.DEFAULT_BOLD
                    },
                    param2 = TextParam(
                        text = it.totalPrice,
                        align = Constant.Companion.Align.ALIGN_END,
                        weight = 0.4,
                    ).apply {
                        size = 26f
                        typeface = Typeface.DEFAULT_BOLD
                    }
                ).apply {
                    perLineSpace = 8
                }
            )

            add(
                TextParam(
                    text = "${it.qua} x ${it.price}",
                    align = Constant.Companion.Align.ALIGN_START,
                    weight = 0.7
                ).apply {
                    perLineSpace = if (index == goodsData.size - 1) 0 else 18
                    size = 26f
                    typeface = Typeface.DEFAULT_BOLD
                }
            )
        }
    }

    private fun convertOrderHeader(order: com.eiviayw.libdraw.bean.Order, isMulti: Boolean = false) = mutableListOf<BaseParam>().apply {
        add(
            TextParam(
                text = order.shopName,
                align = Constant.Companion.Align.ALIGN_CENTER,
            ).apply {
                size = 30f
                typeface = Typeface.DEFAULT_BOLD
            }
        )

        add(
            TextParam(
                text = order.shopAddress,
                align = Constant.Companion.Align.ALIGN_CENTER,
            ).apply {
                size = 26f
                typeface = Typeface.DEFAULT_BOLD
            }
        )

        add(
            TextParam(
                text = order.shopContact,
                align = Constant.Companion.Align.ALIGN_CENTER,
            ).apply {
                size = 26f
                typeface = Typeface.DEFAULT_BOLD
            }
        )

        add(
            TextParam(
                text = "桌号:${order.tableNo}",
                align = Constant.Companion.Align.ALIGN_CENTER,
            ).apply {
                size = 26f
                typeface = Typeface.DEFAULT_BOLD
            }
        )

        add(
            MultiElementParam(
                param1 = TextParam(
                    text = "收银员",
                    weight = 0.5,
                ).apply {
                    size = 26f
                },
                param2 = TextParam(
                    text = order.cashierID,
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.5,
                ).apply {
                    size = 26f
                }
            )
        )

        add(
            MultiElementParam(
                param1 = TextParam(
                    text = "日期",
                    weight = 0.3,
                ).apply {
                    size = 26f
                },
                param2 = TextParam(
                    text = order.orderTime,
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.7,
                ).apply {
                    size = 26f
                }
            )
        )

        add(
            MultiElementParam(
                param1 = TextParam(
                    text = "单号",
                    weight = 0.4,
                ).apply {
                    size = 26f
                    gravity = Constant.Companion.Gravity.CENTER
                },
                param2 = TextParam(
                    text = order.orderNo,
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.6,
                ).apply {
                    perLineSpace = 10
                    size = 26f
                }
            )
        )

        add(LineDashedParam().apply {
            perLineSpace = 30
        })

        val param = if (isMulti){
            MultiElementParam(
                param1 = TextParam(
                    text = "名称",
                    weight = 0.6,
                ).apply {
                    size = 26f
                },
                param2 = TextParam(
                    text = "数量",
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.2,
                ).apply {
                    size = 26f
                },
                param3 = TextParam(
                    text = "合计",
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.2,
                ).apply {
                    size = 26f
                }
            ).apply {
                perLineSpace = 0
            }
        }else{
            MultiElementParam(
                param1 = TextParam(
                    text = "名称",
                    weight = 0.5,
                ).apply {
                    size = 26f
                },
                param2 = TextParam(
                    text = "合计",
                    align = Constant.Companion.Align.ALIGN_END,
                    weight = 0.5,
                ).apply {
                    size = 26f
                }
            ).apply {
                perLineSpace = 0
            }
        }
        add(param)

        add(LineDashedParam().apply {
            perLineSpace = 30
        })
    }

    private fun generateGoodsData() = mutableListOf<com.eiviayw.libdraw.bean.Goods>().apply {
        add(
            com.eiviayw.libdraw.bean.Goods(
                goodsName = "清蒸六头鲍鱼(10只)",
                price = "88.00",
                qua = "2",
                totalPrice = "￥176.00"
            )
        )

        add(
            com.eiviayw.libdraw.bean.Goods(
                goodsName = "波士顿龙虾",
                price = "128.00",
                qua = "1",
                totalPrice = "￥128.00"
            )
        )

        add(
            com.eiviayw.libdraw.bean.Goods(
                goodsName = "三文鱼刺身",
                price = "68.00",
                qua = "1",
                totalPrice = "￥68.00"
            )
        )

        add(
            com.eiviayw.libdraw.bean.Goods(
                goodsName = "Mixed 中英 Chinese 超长混合 and 测试 English 效果",
                price = "28.00",
                qua = "1",
                totalPrice = "$28.00"
            )
        )

        add(
            com.eiviayw.libdraw.bean.Goods(
                goodsName = "威士忌",
                price = "288.00",
                qua = "2",
                totalPrice = "￥576.00"
            )
        )
    }

    private fun generateOrder() = com.eiviayw.libdraw.bean.Order(
        orderType = "正餐",
        orderNo = "RO2407311121030001",
        tableNo = "A区-1",
        orderTime = "2024-07-31 12:20",
        subTotal = "$100.50",
        total = "$100.00",
        qua = "5",
        cashierID = "Yiwei099",
        shopName = "广州酒家",
        shopContact = "020-10086",
        shopAddress = "广东·广州"
    )
}