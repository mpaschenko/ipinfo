package com.uc.ipinfo.data.di

import com.uc.ipinfo.data.datasource.DataSource
import com.uc.ipinfo.data.datasource.DataSourceImpl
import com.uc.ipinfo.data.remote.IpInfoApi
import com.uc.ipinfo.data.repository.Repository
import com.uc.ipinfo.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideIpInfoApi(retrofit: Retrofit) : IpInfoApi = retrofit.create(IpInfoApi::class.java)
}