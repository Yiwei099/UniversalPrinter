package com.eiviayw.universalprinter.bean

enum class ConnectMode(
    val value:Int,
    val label: String,
) {
    WIFI(0,"WIFI"),
    USB(1,"USB"),
    BLE(2,"BLE"),
    NONE(3,"")
}