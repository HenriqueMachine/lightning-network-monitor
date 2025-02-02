package com.henriquemachine.lightningmonitornetwork.data.model

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Represents a Lightning Network node.
 *
 * This class stores information about a node in the Lightning Network, including its public key,
 * alias, number of channels, capacity, and location details.
 *
 * @property publicKey The public key of the node.
 * @property alias The alias of the node if available. Can be `null`.
 * @property channels The number of open channels the node has.
 * @property capacity The total capacity of the node (in Satoshis).
 * @property firstSeen Unix timestamp representing when the node was first detected.
 * @property updatedAt Unix timestamp of the last update of the node.
 * @property city A map containing city information in different languages (e.g., "pt-BR", "en").
 * @property country A map containing country information in different languages (e.g., "pt-BR", "en").
 */
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

    /**
     * Converts this `LightningNode` into a `LightningNodeViewObject`,
     * formatting the data properly for UI display.
     *
     * @return A `LightningNodeViewObject` with formatted data.
     */
    fun toViewObject(): LightningNodeViewObject {
        return LightningNodeViewObject(
            publicKey = publicKey,
            alias = alias ?: "Unknown",
            channels = channels,
            capacityBtc = capacity / 100_000_000.0, // Converts satoshis to BTC
            firstSeen = firstSeen.toFormattedDate(),
            updatedAt = updatedAt.toFormattedDate(),
            city = city.getLocalizedValue(),
            country = country.getLocalizedValue()
        )
    }

    /**
     * Retrieves a localized value from a language map. If the desired language is not available,
     * it falls back to an alternative language.
     *
     * @receiver A map of strings where the key is the language code (e.g., "pt-BR", "en").
     * @return The corresponding localized value or "Unknown" if no value is found.
     */
    private fun Map<String, String>?.getLocalizedValue(): String {
        return this?.get("pt-BR") ?: this?.get("en") ?: "Unknown"
    }
}

/**
 * Extension function to convert a Unix timestamp (in seconds) into a formatted date string.
 *
 * @receiver The Unix timestamp in seconds.
 * @return A string formatted as `dd/MM/yyyy HH:mm:ss`. Returns "Invalid Date" if conversion fails.
 */
fun Long.toFormattedDate(): String {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        sdf.format(Date(this * 1000)) // Multiplies by 1000 to convert seconds to milliseconds
    } catch (e: Exception) {
        "Invalid Date"
    }
}
