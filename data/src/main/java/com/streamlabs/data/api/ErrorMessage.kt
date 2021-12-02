package com.streamlabs.data.api

import com.google.gson.JsonObject

internal data class ErrorMessage(
    val message: String,
    val errors: JsonObject
)