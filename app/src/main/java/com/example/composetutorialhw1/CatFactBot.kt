package com.example.composetutorialhw1

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlinx.serialization.json.Json
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable

private val catJson = Json { ignoreUnknownKeys = true }

@Serializable
data class FactsResponse(
    val data: List<String> = emptyList()
)

suspend fun fetchCatFact(): String {
    val request = "https://meowfacts.herokuapp.com/"

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

    val parsed = catJson.decodeFromString<FactsResponse>(data)
    return parsed.data.firstOrNull() ?: "Nada"
}

private var catBotScope: CoroutineScope? = null
fun startCatBot(context : Context, messageDao: MessageDao) {
    val scope = CoroutineScope(SupervisorJob())
    catBotScope = scope

    scope.launch(Dispatchers.IO) {
        while (isActive) {
            val now = System.currentTimeMillis() //kinda like unreal
            val delayMs = 60_000L - (now % 60_000L)
            delay(delayMs) //sleep mirmir

            try {
                val fact = fetchCatFact()
                messageDao.insertMessage(MessageData(author = "CatBot", text = fact))
                pushNotification(context, "CatBot", fact)
            } catch (e: Exception) {
                messageDao.insertMessage(MessageData(author = "CatBot", text = e.toString()))
            }
        }
    }
}
