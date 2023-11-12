package com.uc.ipinfo.data.repository

import com.uc.ipinfo.domain.model.IpInfo

interface Repository {
    suspend fun getIPInfo(ipAddress: String): IpInfo?
}


