package com.example.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WebhookClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val webhookUrl = "https://zulfiqar999.app.n8n.cloud/webhook/my%20chat%20app"
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun sendMessage(userMessage: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val jsonPayload = JSONObject().apply {
                put("message", userMessage)
            }.toString()

            val request = Request.Builder()
                .url(webhookUrl)
                .post(jsonPayload.toRequestBody(jsonMediaType))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build()

            val response = client.newCall(request).execute()
            val rawResponseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Server returned status code ${response.code}")
                )
            }

            val parsedText = parseResponse(rawResponseBody)
            Result.success(parsedText)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseResponse(rawBody: String): String {
        val trimmed = rawBody.trim()
        if (trimmed.isEmpty()) return "No content returned from Assets AI."

        return try {
            if (trimmed.startsWith("{")) {
                val jsonObject = JSONObject(trimmed)
                extractTextFromJsonObject(jsonObject)
            } else if (trimmed.startsWith("[")) {
                val jsonArray = JSONArray(trimmed)
                if (jsonArray.length() > 0) {
                    val first = jsonArray.get(0)
                    if (first is JSONObject) {
                        extractTextFromJsonObject(first)
                    } else {
                        first.toString()
                    }
                } else {
                    "Empty response array from Assets AI."
                }
            } else {
                trimmed
            }
        } catch (e: Exception) {
            trimmed
        }
    }

    private fun extractTextFromJsonObject(obj: JSONObject): String {
        val priorityKeys = listOf(
            "response", "output", "message", "text", "data", "reply", "content", "result", "answer"
        )

        for (key in priorityKeys) {
            if (obj.has(key) && !obj.isNull(key)) {
                val value = obj.get(key)
                if (value is JSONObject) {
                    return extractTextFromJsonObject(value)
                } else if (value is JSONArray && value.length() > 0) {
                    val first = value.get(0)
                    if (first is JSONObject) return extractTextFromJsonObject(first)
                    return first.toString()
                } else {
                    return value.toString()
                }
            }
        }

        // Fallback: search any string property or formatted json
        val keys = obj.keys()
        while (keys.hasNext()) {
            val k = keys.next()
            val v = obj.get(k)
            if (v is String && v.isNotBlank()) {
                return v
            }
        }

        return obj.toString()
    }
}
