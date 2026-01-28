package com.example.composetutorialhw1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val API_KEY = "dda105bc5fe9480da224443b29ea5cd7"
private const val BASE_URL = "https://api.twelvedata.com/"

//https://kotlinlang.org/api/kotlinx.serialization/kotlinx-serialization-json/kotlinx.serialization.json/-json-builder/ignore-unknown-keys.html
private val json = Json { ignoreUnknownKeys = true }
@Serializable
data class StockQuote(
    val symbol: String,
    val name: String,
    val close: String,
    val change: String,
    val percent_change: String
)

suspend fun lookUpStock(stock: String): String {
    val symbol = stock.uppercase()
    //https://api.twelvedata.com/quote?symbol=AAPL&apikey=demo format
    val request = BASE_URL + "quote?symbol=" + symbol + "&apikey=" + API_KEY
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
    return parseData(data)
}

//https://medium.com/@midoripig1009/working-with-json-in-kotlin-parsing-and-serialization-a62300ec43b8
fun parseData(data: String): String {
    try {
        val quote = json.decodeFromString<StockQuote>(data)

        val change = quote.change.toDoubleOrNull() ?: 0.0
        val percent = quote.percent_change.toDoubleOrNull() ?: 0.0

        var doom = false
        var stagnant = false
        var flavourMessage: String
        if (change > 0) {
            doom = false
        } else if (change < 0) {
            doom = true
        } else {
            stagnant = true
        }

        val chance = (1..5).random() //https://stackoverflow.com/questions/45685026/how-can-i-get-a-random-number-in-kotlin
        if (doom) {
            flavourMessage = when (chance) {
                1 -> "Freak the fuck out and panic sell everything right now. It's fucking over"
                2 ->"AAAAAAAAAAAAAAAAAAA!!! AAAAAAAAAAAAAAAAA!!!!!"
                3 -> "🥀🥀🥀🥀"
                4 -> "DOWN 📉😭😭😭💔💔💔"
                else -> "it's over"
            }
        } else if (stagnant) {
            flavourMessage = "Nothing ever happens"
        } else {
            flavourMessage = when (chance) {
                1 -> "WE'RE SO BACK"
                2 -> "BIIIG MONEEYY"
                3 -> "UP! 📈🤑🤑🤑💵💵💵"
                4 -> "THE GOAT"
                else -> "PLUH!"
            }
        }

        return "$flavourMessage\n${quote.name} (${quote.symbol}) | Close: ${quote.close} Change: $change ($percent%)"

    } catch (e: Exception) {
        return if (data.contains("Pro")) {
            "HOWDY THERE PARTNER, US MARKETS ONLY!!! 🤠🦅🦅🦅"
        } else {
            "IT'S SO OVER. THE STOCKBOT DIED PARSING THIS: \n$data"
        }
    }
}

fun checkUserInput(input: String) : String? {
    val inputBig = input.uppercase().trim()
    val words = inputBig.split(" ")
    for (i in words.indices) {
        if (words[i] == "HOWS" || words[i] == "HOW'S") {
            if (i + 1 < words.size) {
                return words[i + 1]
            }
        }
    }
    return null
}

suspend fun checkIfStockExists(input : String): Boolean {
    val symbol = input.uppercase().trim()

    //just to speed up search
    val countries = listOf(
        "United States",
        "Canada",
        "United Kingdom",
        "Germany",
        "France",
        "Netherlands",
        "Switzerland",
        "Sweden",
        "Norway",
        "Denmark",
        "Finland",
        "Ireland",
        "Spain",
        "Italy",
        "Belgium",
        "Austria",
        "Portugal",
        "Australia",
        "New Zealand"
    )

    for (country in countries) {  //United States = United%20States , just UnitedStates wont work
        val request = BASE_URL + "stocks?symbol=" + symbol + "&country=" + country.replace(" ", "%20") + "&format=JSON"

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

        if (data.contains("\"symbol\":\"$symbol\"")) {
            return true
        }
    }

    return false
}
