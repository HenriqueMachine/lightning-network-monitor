package com.henriquemachine.lightningmonitornetwork.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.henriquemachine.lightningmonitornetwork.utils.LightningMonitorNetworkTheme
import com.henriquemachine.lightningmonitornetwork.presentation.lightning.LightningNodeScreen
import com.henriquemachine.lightningmonitornetwork.presentation.lightning.LightningNodeIntent
import com.henriquemachine.lightningmonitornetwork.presentation.lightning.LightningNodeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LightningMonitorNetworkTheme {
                val viewModel: LightningNodeViewModel = hiltViewModel()

                viewModel.processUserIntent(LightningNodeIntent.FetchNodes)

                LightningNodeScreen(
                    viewModel = viewModel,
                )
            }
        }
    }
}

