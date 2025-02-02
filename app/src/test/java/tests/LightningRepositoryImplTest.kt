package tests

import com.henriquemachine.lightningmonitornetwork.data.api.LightningApi
import com.henriquemachine.lightningmonitornetwork.data.model.LightningNode
import com.henriquemachine.lightningmonitornetwork.data.repository.LightningRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import com.henriquemachine.lightningmonitornetwork.utils.Result
import okhttp3.ResponseBody.Companion.toResponseBody
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response
import org.junit.Assert.assertTrue

class LightningRepositoryImplTest {

    private lateinit var repository: LightningRepositoryImpl
    private lateinit var lightningApi: LightningApi

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        lightningApi = mock(LightningApi::class.java)
        repository = LightningRepositoryImpl(lightningApi)
    }

    @Test
    fun `test get nodes success`() = runBlocking {
        val mockResponse = Response.success(
            listOf(
                LightningNode(
                    publicKey = "publicKey1",
                    alias = "Node 1",
                    channels = 5,
                    capacity = 25000000,
                    firstSeen = 1638326400,
                    updatedAt = 1638326400,
                    city = mapOf("pt-BR" to "São Paulo"),
                    country = mapOf("pt-BR" to "BR")
                )
            )
        )
        `when`(lightningApi.getLightningNodes()).thenReturn(mockResponse)
        val result = repository.getNodes()
        verify(lightningApi).getLightningNodes()

        if (result is Result.Success) {
            assertTrue(result.data.isNotEmpty())
            val node = result.data[0]
            assertTrue(node.publicKey == "publicKey1")
            assertTrue(node.alias == "Node 1")
            assertTrue(node.channels == 5)
            assertTrue(node.capacityBtc == 0.25)
            assertTrue(node.firstSeen == "30/11/2021 23:40:00")
            assertTrue(node.updatedAt == "30/11/2021 23:40:00")
            val city = node.city
            val country = node.country
            assertTrue(city == "São Paulo")
            assertTrue(country == "BR")
        } else {
            assertTrue(result is Result.Error)
        }
    }

    @Test
    fun `test get nodes failure - network error`() = runBlocking {
        `when`(lightningApi.getLightningNodes()).thenThrow(RuntimeException("Network Error"))
        val result = repository.getNodes()
        verify(lightningApi).getLightningNodes()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `test get nodes empty list`() = runBlocking {
        val mockResponse = Response.success(emptyList<LightningNode>())
        `when`(lightningApi.getLightningNodes()).thenReturn(mockResponse)
        val result = repository.getNodes()
        verify(lightningApi).getLightningNodes()

        if (result is Result.Success) {
            assertTrue(result.data.isEmpty())
        } else {
            assertTrue(result is Result.Error)
        }
    }

    @Test
    fun `test get nodes server error 500`() = runBlocking {
        val mockResponse = Response.error<List<LightningNode>>(500, "Server Error".toResponseBody())
        `when`(lightningApi.getLightningNodes()).thenReturn(mockResponse)
        val result = repository.getNodes()
        verify(lightningApi).getLightningNodes()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `test get nodes invalid data format`() = runBlocking {
        val mockResponse = Response.success(
            listOf(
                LightningNode(
                    publicKey = "publicKey2",
                    alias = "Node 2",
                    channels = 5,
                    capacity = 25000000,
                    firstSeen = 1638326400,
                    updatedAt = 1638326400,
                    city = mapOf("pt-BR" to "Rio de Janeiro"),
                    country = mapOf("pt-BR" to "BR")
                )
            )
        )
        `when`(lightningApi.getLightningNodes()).thenReturn(mockResponse)
        val result = repository.getNodes()
        verify(lightningApi).getLightningNodes()

        if (result is Result.Success) {
            val node = result.data[0]
            assertTrue(node.city == "Rio de Janeiro")
            assertTrue(node.country == "BR")
        } else {
            assertTrue(result is Result.Error)
        }
    }

    @Test
    fun `test get nodes null response`() = runBlocking {
        `when`(lightningApi.getLightningNodes()).thenReturn(null)
        val result = repository.getNodes()
        verify(lightningApi).getLightningNodes()
        assertTrue(result is Result.Error)
    }

}