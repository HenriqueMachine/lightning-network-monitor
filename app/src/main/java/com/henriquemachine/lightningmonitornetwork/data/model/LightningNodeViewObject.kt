package com.henriquemachine.lightningmonitornetwork.data.model

data class LightningNodeViewObject(
    val publicKey: String,
    val alias: String,
    val channels: Int,
    val capacityBtc: Double,
    val firstSeen: String,
    val updatedAt: String,
    val city: String,
    val country: String
)
