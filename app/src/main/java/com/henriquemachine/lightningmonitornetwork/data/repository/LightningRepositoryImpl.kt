package com.henriquemachine.lightningmonitornetwork.data.repository

import com.henriquemachine.lightningmonitornetwork.data.api.LightningApi
import com.henriquemachine.lightningmonitornetwork.data.model.LightningNodeViewObject
import com.henriquemachine.lightningmonitornetwork.utils.Result
import retrofit2.HttpException
import java.io.IOException
import timber.log.Timber

class NodeRepositoryImpl(
    private val lightningApi: LightningApi
) : LightningRepository {

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
            Result.Error(Exception("Network error", e))
        } catch (e: HttpException) {
            Result.Error(Exception("HTTP error: ${e.code()} ${e.message()}", e))
        } catch (e: Exception) {
            Result.Error(Exception("Unexpected error", e))
        }
    }
}
