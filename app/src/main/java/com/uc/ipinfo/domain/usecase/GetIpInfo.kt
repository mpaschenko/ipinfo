package com.uc.ipinfo.domain.usecase

import com.uc.ipinfo.data.repository.Repository
import javax.inject.Inject

class GetIpInfo @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(ipAddress: String) = repository.getIPInfo(ipAddress)
}