package com.henriquemachine.lightningmonitornetwork.presentation.lightning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henriquemachine.lightningmonitornetwork.domain.GetLightningNodesUseCase
import com.henriquemachine.lightningmonitornetwork.data.model.LightningNodeViewObject
import com.henriquemachine.lightningmonitornetwork.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LightningNodeViewModel @Inject constructor(
    private val getLightningNodesUseCase: GetLightningNodesUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<LightningNodeViewState>(LightningNodeViewState.Empty)
    val viewState: StateFlow<LightningNodeViewState> = _viewState

    private val _viewEffect = MutableSharedFlow<LightningNodeViewEffect>(replay = 0)
    val viewEffect: SharedFlow<LightningNodeViewEffect> = _viewEffect

    fun processUserIntent(intent: LightningNodeIntent) {
        when (intent) {
            is LightningNodeIntent.FetchNodes -> loadLightningNodes()
            is LightningNodeIntent.RefreshNodes -> refreshLightningNodes()
        }
    }

    private fun loadLightningNodes() {
        performFetchNodesOperation {
            return@performFetchNodesOperation getLightningNodesUseCase.execute()
        }
    }

    private fun refreshLightningNodes() {
        performFetchNodesOperation {
            val result = getLightningNodesUseCase.execute()
            _viewEffect.emit(LightningNodeViewEffect.RefreshComplete)
            return@performFetchNodesOperation result
        }
    }

    private fun performFetchNodesOperation(fetchOperation: suspend () -> Result<List<LightningNodeViewObject>>) {
        viewModelScope.launch {
            _viewState.value = LightningNodeViewState.Loading
            when (val result = fetchOperation()) {
                is Result.Success -> {
                    val nodes = result.data
                    _viewState.value = if (nodes.isEmpty()) {
                        LightningNodeViewState.Empty
                    } else {
                        LightningNodeViewState.Success(nodes = nodes)
                    }
                }

                is Result.Error -> {
                    _viewState.value =
                        LightningNodeViewState.Error("Error: ${result.exception.message}")
                }
            }
        }
    }
}
