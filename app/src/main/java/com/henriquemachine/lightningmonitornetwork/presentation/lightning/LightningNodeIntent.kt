package com.henriquemachine.lightningmonitornetwork.presentation.lightning

sealed class LightningNodeIntent {
    data object FetchNodes : LightningNodeIntent()
    data object RefreshNodes : LightningNodeIntent()
}
