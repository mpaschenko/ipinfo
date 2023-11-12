package com.uc.ipinfo.data.repository

import com.uc.ipinfo.data.datasource.DataSource
import com.uc.ipinfo.domain.model.IpInfo

class RepositoryImpl(private val dataSource: DataSource) : Repository {
    override suspend fun getIPInfo(ipAddress: String): IpInfo? = dataSource.getIpInfo(ipAddress)
}