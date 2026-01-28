package com.example.composetutorialhw1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private suspend fun fetchIsEven(): String {
    val request = "https://api.isevenapi.xyz/api/iseven/6/"

    val data = withContext(Dispatchers.IO) {
        val url = URL(request)
        val connection = url.openConnection() as HttpsURLConnection
        try {
            connection.requestMethod = "GET"
            connection.inputStream.bufferedReader().readText()
        } finally {
            connection.disconnect()
        }
    }
    return "will work later"
}