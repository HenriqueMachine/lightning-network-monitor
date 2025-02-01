package com.henriquemachine.lightningmonitornetwork.utils.ext

fun Double.toBtc(): String {
    return String.format("%.8f", this / 100000000.0)
}