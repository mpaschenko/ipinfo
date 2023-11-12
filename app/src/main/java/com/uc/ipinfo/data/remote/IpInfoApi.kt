package com.uc.ipinfo.data.remote

import com.uc.ipinfo.domain.model.IpInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IpInfoApi {
    @GET("json/{ip}")
    fun getIpInfo(@Path("ip") ipAddress: String): Call<IpInfo>
}