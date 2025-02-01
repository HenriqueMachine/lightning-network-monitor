package com.henriquemachine.lightningmonitornetwork.presentation.lightning

import com.henriquemachine.lightningmonitornetwork.data.model.LightningNodeViewObject

sealed class LightningNodeViewState {
    data object Loading : LightningNodeViewState()
    data object Empty : LightningNodeViewState()
    data class Success(
        val nodes: List<LightningNodeViewObject>,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : LightningNodeViewState()
    data class Error(val message: String) : LightningNodeViewState()
}