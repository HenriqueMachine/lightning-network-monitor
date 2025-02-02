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

/**
 * ViewModel responsible for managing the state and side effects related to Lightning Nodes.
 * It follows the MVI (Model-View-Intent) pattern, handling user intents and updating the view state accordingly.
 *
 * @property getLightningNodesUseCase Use case responsible for fetching Lightning Nodes.
 */
@HiltViewModel
class LightningNodeViewModel @Inject constructor(
    private val getLightningNodesUseCase: GetLightningNodesUseCase
) : ViewModel() {

    /**
     * Holds the state of the view.
     */
    private val _viewState = MutableStateFlow<LightningNodeViewState>(LightningNodeViewState.Empty)
    val viewState: StateFlow<LightningNodeViewState> = _viewState

    /**
     * Holds the view effects that can be observed as one-time events.
     */
    private val _viewEffect = MutableSharedFlow<LightningNodeViewEffect>(replay = 0)
    val viewEffect: SharedFlow<LightningNodeViewEffect> = _viewEffect

    /**
     * Processes user intents and triggers the corresponding actions.
     *
     * @param intent The user intent to be processed.
     */
    fun processUserIntent(intent: LightningNodeIntent) {
        when (intent) {
            is LightningNodeIntent.FetchNodes -> loadLightningNodes()
            is LightningNodeIntent.RefreshNodes -> refreshLightningNodes()
        }
    }

    /**
     * Loads Lightning Nodes from the use case and updates the view state.
     */
    private fun loadLightningNodes() {
        performFetchNodesOperation {
            return@performFetchNodesOperation getLightningNodesUseCase.getNodes()
        }
    }

    /**
     * Refreshes Lightning Nodes by fetching new data and emits a refresh completion effect.
     */
    private fun refreshLightningNodes() {
        performFetchNodesOperation {
            val result = getLightningNodesUseCase.getNodes()
            _viewEffect.emit(LightningNodeViewEffect.RefreshComplete)
            return@performFetchNodesOperation result
        }
    }

    /**
     * Performs a fetch operation for Lightning Nodes and updates the view state accordingly.
     *
     * @param fetchOperation The suspend function responsible for fetching the data.
     */
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