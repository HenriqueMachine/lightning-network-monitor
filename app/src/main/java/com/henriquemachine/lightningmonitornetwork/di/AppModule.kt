package com.henriquemachine.lightningmonitornetwork.di

import com.henriquemachine.lightningmonitornetwork.BuildConfig
import com.henriquemachine.lightningmonitornetwork.data.api.LightningApi
import com.henriquemachine.lightningmonitornetwork.data.repository.LightningRepository
import com.henriquemachine.lightningmonitornetwork.data.repository.LightningRepositoryImpl
import com.henriquemachine.lightningmonitornetwork.domain.GetLightningNodesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): LightningApi {
        return retrofit.create(LightningApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNodeRepository(apiService: LightningApi): LightningRepository {
        return LightningRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetLightningNodesUseCase(repository: LightningRepository): GetLightningNodesUseCase {
        return GetLightningNodesUseCase(repository)
    }
}
