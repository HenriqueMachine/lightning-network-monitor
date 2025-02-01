package com.henriquemachine.lightningmonitornetwork.data.model

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class LightningNode(
    @Json(name = "publicKey") val publicKey: String,
    @Json(name = "alias") val alias: String?,
    @Json(name = "channels") val channels: Int,
    @Json(name = "capacity") val capacity: Long,
    @Json(name = "firstSeen") val firstSeen: Long,
    @Json(name = "updatedAt") val updatedAt: Long,
    @Json(name = "city") val city: Map<String, String>?,
    @Json(name = "country") val country: Map<String, String>?
) {
    fun toViewObject(): LightningNodeViewObject {
        return LightningNodeViewObject(
            publicKey = publicKey,
            alias = alias ?: "Unknown",
            channels = channels,
            capacityBtc = capacity / 100_000_000.0,
            firstSeen = firstSeen.toFormattedDate(),
            updatedAt = updatedAt.toFormattedDate(),
            city = city?.get("pt-BR") ?: city?.get("en") ?: "Unknown",
            country = country?.get("pt-BR") ?: country?.get("en") ?: "Unknown"
        )
    }
}

fun Long.toFormattedDate(): String {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        sdf.format(Date(this * 1000))
    } catch (e: Exception) {
        "Invalid Date"
    }
}
