package com.example.nytapp.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T : Any> makeCall(call: suspend () -> T): Resource<T> {
    return try {
        val response = withContext(Dispatchers.IO) { call.invoke() }
        Resource.Success(response)
    } catch (e: Exception) {
        Resource.Error(e.message)
    }
}