package com.henriquemachine.lightningmonitornetwork.data.api

import com.henriquemachine.lightningmonitornetwork.data.model.LightningNode
import retrofit2.Response
import retrofit2.http.GET

interface LightningApi {
    @GET("v1/lightning/nodes/rankings/connectivity")
    suspend fun getLightningNodes(): Response<List<LightningNode>>
}
