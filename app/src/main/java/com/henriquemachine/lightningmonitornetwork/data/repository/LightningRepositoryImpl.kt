package com.henriquemachine.lightningmonitornetwork.data.repository

import com.henriquemachine.lightningmonitornetwork.data.api.LightningApi
import com.henriquemachine.lightningmonitornetwork.data.model.LightningNodeViewObject
import com.henriquemachine.lightningmonitornetwork.utils.Result
import retrofit2.HttpException
import java.io.IOException
import timber.log.Timber

/**
 * Implementation of [LightningRepository] that fetches Lightning Network node data from an API.
 *
 * This repository interacts with [LightningApi] to retrieve node information and handles possible
 * errors such as network failures, HTTP errors, and unexpected exceptions.
 *
 * @property lightningApi The API service used to fetch Lightning Network node data.
 */
class LightningRepositoryImpl(
    private val lightningApi: LightningApi
) : LightningRepository {

    /**
     * Fetches the list of Lightning Network nodes from the API.
     *
     * This method makes a network request to retrieve node data and maps the response into
     * a list of [LightningNodeViewObject]. It handles different error cases, logging them
     * appropriately and returning a [Result.Error] in case of failure.
     *
     * @return A [Result] containing a list of [LightningNodeViewObject] if successful,
     * or an error result in case of failure.
     */
    override suspend fun getNodes(): Result<List<LightningNodeViewObject>> {
        return try {
            val response = lightningApi.getLightningNodes()
            Timber.d("API Response: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Timber.d("Nodes fetched successfully: ${body.size} nodes found")
                    Result.Success(body.map { it.toViewObject() })
                } ?: run {
                    Result.Error(NullPointerException("Response body is null"))
                }
            } else {
                Timber.e("HTTP Error: ${response.code()} ${response.message()}")
                Result.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Timber.e(e, "Network error while fetching Lightning nodes")
            Result.Error(Exception("Network error", e))
        } catch (e: HttpException) {
            Timber.e(e, "HTTP error: ${e.code()} ${e.message()}")
            Result.Error(Exception("HTTP error: ${e.code()} ${e.message()}", e))
        } catch (e: Exception) {
            Timber.e(e, "Unexpected error occurred")
            Result.Error(Exception("Unexpected error", e))
        }
    }
}
