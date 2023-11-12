package com.uc.ipinfo.domain.model

import com.google.gson.annotations.SerializedName

enum class Status {

    @SerializedName("fail")
    FAILURE,

    @SerializedName("success")
    SUCCESS
}