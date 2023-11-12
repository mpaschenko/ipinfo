package com.uc.ipinfo.data.datasource

import com.uc.ipinfo.domain.model.IpInfo


interface DataSource {
    suspend fun getIpInfo(ipAddress: String): IpInfo?
}