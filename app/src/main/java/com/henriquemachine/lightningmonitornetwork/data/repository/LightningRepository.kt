package com.henriquemachine.lightningmonitornetwork.data.repository

import com.henriquemachine.lightningmonitornetwork.data.model.LightningNodeViewObject
import com.henriquemachine.lightningmonitornetwork.utils.Result

interface LightningRepository {
    suspend fun getNodes(): Result<List<LightningNodeViewObject>>
}
