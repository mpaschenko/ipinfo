package com.uc.ipinfo.data.datasource

import com.uc.ipinfo.data.remote.IpInfoApi
import com.uc.ipinfo.domain.model.IpInfo
import com.uc.ipinfo.domain.model.Status

class DataSourceImpl(private val ipInfoApi: IpInfoApi): DataSource {
    override suspend fun getIpInfo(ipAddress: String): IpInfo? {
        return try {
            val response = ipInfoApi.getIpInfo(ipAddress).execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                IpInfo(status = Status.FAILURE, message = response.errorBody()?.string() ?: "")
            }
        } catch (e: Exception) {
            IpInfo(status = Status.FAILURE, message = e.message ?: "")
        }
    }
}