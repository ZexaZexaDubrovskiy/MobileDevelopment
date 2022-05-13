package com.example.rpcollegemobile

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

object Network {

    val client = OkHttpClient()

    fun getEventCall(): Call {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("188.68.31.11")
            .addEncodedPathSegments("event/list/json")
            .addQueryParameter("offset", "0")
            .build()
        val request = Request.Builder()
            .get()
            .url("")
            .build()
        return client.newCall(request)
    }


}