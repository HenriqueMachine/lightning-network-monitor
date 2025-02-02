package com.henriquemachine.lightningmonitornetwork.presentation.lightning

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.henriquemachine.lightningmonitornetwork.R
import com.henriquemachine.lightningmonitornetwork.data.model.LightningNodeViewObject
import com.henriquemachine.lightningmonitornetwork.utils.ext.toBtc
import com.henriquemachine.lightningmonitornetwork.utils.ext.formatUnixDate

/**
 * Composable function for displaying the Lightning Node screen.
 *
 * This screen fetches and displays a list of Lightning Network nodes using a grid layout.
 * It supports pull-to-refresh functionality and shows details of a selected node in a dialog.
 *
 * @param viewModel The ViewModel that handles fetching and managing the Lightning nodes.
 */
@Composable
fun LightningNodeScreen(
    viewModel: LightningNodeViewModel = hiltViewModel()
) {
    val state = viewModel.viewState.collectAsState().value
    val context = LocalContext.current
    var selectedNode by remember { mutableStateOf<LightningNodeViewObject?>(null) }

    val onRefresh = {
        viewModel.processUserIntent(LightningNodeIntent.RefreshNodes)
    }

    LaunchedEffect(Unit) {
        viewModel.viewEffect.collect { effect ->
            effect.let {
                when (it) {
                    is LightningNodeViewEffect.ShowErrorMessage -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    is LightningNodeViewEffect.ShowSuccessMessage -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    is LightningNodeViewEffect.RefreshComplete -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.newBatchLoaded), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    when (state) {
        is LightningNodeViewState.Loading -> LoadingView()
        is LightningNodeViewState.Success -> {
            PullToRefreshNodesGrid(
                nodes = state.nodes,
                isRefreshing = state.isLoading,
                onRefresh = onRefresh,
                onNodeSelected = { node ->
                    selectedNode = node
                }
            )
        }

        is LightningNodeViewState.Error -> {
            ErrorView(message = state.message)
        }

        is LightningNodeViewState.Empty -> {
            ErrorView(message = stringResource(R.string.noNodesFound))
        }
    }

    selectedNode?.let { node ->
        ShowNodeDialog(node = node, onDismiss = { selectedNode = null })
    }
}

/**
 * Displays a pull-to-refresh grid of Lightning nodes.
 *
 * @param nodes List of Lightning nodes to display.
 * @param isRefreshing Indicates whether data is being refreshed.
 * @param onRefresh Callback triggered when the user pulls to refresh.
 * @param onNodeSelected Callback triggered when a node is selected.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshNodesGrid(
    nodes: List<LightningNodeViewObject>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onNodeSelected: (LightningNodeViewObject) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(nodes.size) { index ->
                NodeItemView(node = nodes[index], onNodeSelected = onNodeSelected)
            }
        }
    }
}

/**
 * Displays a dialog with detailed information about a selected Lightning node.
 *
 * @param node The selected Lightning node.
 * @param onDismiss Callback triggered when the dialog is dismissed.
 */
@Composable
fun ShowNodeDialog(
    node: LightningNodeViewObject,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = node.alias)
        },
        text = {
            Column {
                Text(stringResource(id = R.string.publicKey, node.publicKey))
                Text(stringResource(id = R.string.channels, node.channels))
                Text(stringResource(id = R.string.capacity, node.capacityBtc.toBtc()))
                Text(stringResource(id = R.string.location, node.city, node.country))
                Text(stringResource(id = R.string.firstSeen, node.firstSeen.formatUnixDate()))
                Text(stringResource(id = R.string.updatedAt, node.updatedAt.formatUnixDate()))
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

/**
 * Displays a loading indicator while data is being fetched.
 */
@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/**
 * Displays an individual Lightning node as a card.
 *
 * @param node The Lightning node to display.
 * @param onNodeSelected Callback triggered when the node is clicked.
 */
@Composable
fun NodeItemView(
    node: LightningNodeViewObject,
    onNodeSelected: (LightningNodeViewObject) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onNodeSelected(node) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = node.alias,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = "Public Key: ${node.publicKey}")
            Text(text = "Channels: ${node.channels}")
            Text(text = "Capacity: ${node.capacityBtc.toBtc()} BTC")
            Text(text = "Location: ${node.city}, ${node.country}")
            Text(text = "First Seen: ${node.firstSeen.formatUnixDate()}")
            Text(text = "Updated At: ${node.updatedAt.formatUnixDate()}")
        }
    }
}

/**
 * Displays an error message when something goes wrong.
 *
 * @param message The error message to display.
 */
@Composable
fun ErrorView(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(align = Alignment.Center)
    ) {
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}
