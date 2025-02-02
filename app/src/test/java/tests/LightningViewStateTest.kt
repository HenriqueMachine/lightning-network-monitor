package tests

import com.henriquemachine.lightningmonitornetwork.data.model.LightningNodeViewObject
import com.henriquemachine.lightningmonitornetwork.presentation.lightning.LightningNodeViewState
import org.junit.Assert.assertEquals
import org.junit.Test

class LightningNodeViewStateTest {

    @Test
    fun `test loading state`() {
        val loadingState = LightningNodeViewState.Loading

        assertEquals(LightningNodeViewState.Loading, loadingState)
    }

    @Test
    fun `test empty state`() {
        val emptyState = LightningNodeViewState.Empty

        assertEquals(LightningNodeViewState.Empty, emptyState)
    }

    @Test
    fun `test success state with nodes`() {
        val mockNodes = listOf(
            LightningNodeViewObject(
                publicKey = "publicKey1",
                alias = "Node 1",
                channels = 5,
                capacityBtc = 0.45,
                firstSeen = "2022-01-01",
                updatedAt = "2023-01-01",
                city = "New York",
                country = "USA"
            )
        )
        val successState = LightningNodeViewState.Success(nodes = mockNodes)

        assertEquals(LightningNodeViewState.Success(nodes = mockNodes), successState)
    }

    @Test
    fun `test success state with isLoading and error`() {
        val mockNodes = listOf(
            LightningNodeViewObject(
                publicKey = "publicKey1",
                alias = "Node 1",
                channels = 5,
                capacityBtc = 0.45,
                firstSeen = "2022-01-01",
                updatedAt = "2023-01-01",
                city = "New York",
                country = "USA"
            )
        )
        val successState = LightningNodeViewState.Success(
            nodes = mockNodes,
            isLoading = true,
            error = "An error occurred"
        )

        assertEquals(
            LightningNodeViewState.Success(
                nodes = mockNodes,
                isLoading = true,
                error = "An error occurred"
            ), successState
        )
    }

    @Test
    fun `test error state`() {
        val errorMessage = "An error occurred"
        val errorState = LightningNodeViewState.Error(message = errorMessage)

        assertEquals(LightningNodeViewState.Error(message = errorMessage), errorState)
    }
}
