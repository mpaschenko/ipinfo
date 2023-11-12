package com.uc.ipinfo.domain.model

data class IpInfo(
    val query: String = "",
    val status: Status = Status.SUCCESS,
    val message: String = "",
    val country: String = "",
    val countryCode: String = "",
    val region: String = "",
    val regionName: String = "",
    val city: String = "",
    val zip: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val timezone: String = "",
    val isp: String = "",
    val org: String = ""
)