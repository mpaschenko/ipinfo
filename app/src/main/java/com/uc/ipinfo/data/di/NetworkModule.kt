package com.uc.ipinfo.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.uc.ipinfo.BuildConfig
import com.uc.ipinfo.utils.PrettyLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder().apply {

        connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            addInterceptor(httpLoggingInterceptor)
        }
    }.build()

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .setLenient()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()

    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: URL,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideBaseUrl(): URL = URL(BuildConfig.BASE_URL)

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(PrettyLogger()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}