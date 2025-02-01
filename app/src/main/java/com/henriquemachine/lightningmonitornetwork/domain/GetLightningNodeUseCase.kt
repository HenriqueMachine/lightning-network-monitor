package com.henriquemachine.lightningmonitornetwork.domain

import com.henriquemachine.lightningmonitornetwork.data.model.LightningNodeViewObject
import com.henriquemachine.lightningmonitornetwork.data.repository.LightningRepository
import com.henriquemachine.lightningmonitornetwork.utils.Result
import javax.inject.Inject

class GetLightningNodesUseCase @Inject constructor(
    private val repository: LightningRepository
) {
    suspend fun execute(): Result<List<LightningNodeViewObject>> {
        return repository.getNodes()
    }
}
