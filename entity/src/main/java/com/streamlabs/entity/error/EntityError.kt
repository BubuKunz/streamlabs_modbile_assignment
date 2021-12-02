package com.streamlabs.entity.error

abstract class EntityError(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Error(message, cause)

data class GenericError(
    val code: Int? = null,
    val error: String? = null,
    val extra: String? = null
) : EntityError(
    message = error
)

data class AuthError(
    val code: Int? = null,
    val error: String? = null,
    val extra: String? = null
) : EntityError(
    message = error
)

object NetworkError : EntityError()