package com.henriquemachine.lightningmonitornetwork.presentation.lightning

sealed class LightningNodeViewEffect {
    data class ShowErrorMessage(val message: String) : LightningNodeViewEffect()
    data class ShowSuccessMessage(val message: String) : LightningNodeViewEffect()
    data object RefreshComplete : LightningNodeViewEffect()
}
