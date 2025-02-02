package com.henriquemachine.lightningmonitornetwork.data.model

/**
 * Represents a view object for displaying Lightning Network node information.
 *
 * This class is used to present node data in a more readable format for UI components.
 *
 * @property publicKey The public key of the Lightning Network node.
 * @property alias The alias of the node if available.
 * @property channels The number of channels the node has open.
 * @property capacityBtc The total capacity of the node represented in BTC.
 * @property firstSeen The formatted date when the node was first detected.
 * @property updatedAt The formatted date of the last update for this node.
 * @property city The city where the node is located.
 * @property country The country where the node is located.
 */
data class LightningNodeViewObject(
    val publicKey: String = "",
    val alias: String = "Unknown",
    val channels: Int = 0,
    val capacityBtc: Double = 0.0,
    val firstSeen: String = "Unknown Date",
    val updatedAt: String = "Unknown Date",
    val city: String = "Unknown",
    val country: String = "Unknown"
)
