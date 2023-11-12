package com.uc.ipinfo.utils

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject

class PrettyLogger : HttpLoggingInterceptor.Logger {

    private fun print(m: String) = Log.i(TAG, m)

    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("["))
            try {
                val prettyJson = JSONObject(message).toString(4)
                print(if (prettyJson.split("\n").size > MAX_LOG_LINES) message else prettyJson)
            } catch (e: JSONException) {
                print(message)
            }
        else
            print(message)
    }

    companion object {
        private const val MAX_LOG_LINES = 200
        private const val TAG = "API"
    }
}
