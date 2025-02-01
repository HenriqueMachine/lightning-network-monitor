package com.henriquemachine.lightningmonitornetwork.utils.ext

import java.text.SimpleDateFormat
import java.util.Locale

fun String.formatUnixDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(this)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        if (date != null) {
            outputFormat.format(date)
        } else {
            "Invalid date"
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
        "Invalid date"
    }
}