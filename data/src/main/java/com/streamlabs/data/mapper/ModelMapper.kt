package com.streamlabs.data.mapper

internal interface ModelMapper<T, R> {

    fun fromModel(model: T): R

    fun toModel(other: R): T
}