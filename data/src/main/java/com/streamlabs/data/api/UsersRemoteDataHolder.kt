package com.streamlabs.data.api

import com.streamlabs.data.api.model.User as UserApi
import com.streamlabs.data.mapper.ModelMapper
import com.streamlabs.entity.model.User

/**
 * In case of HTTP error throws an [EntityError]
 */
internal interface IUserRemoteDataHolder {
    suspend fun getUsers(ids: Array<String>): List<User>
    suspend fun getUserById(id: String): User?
}

internal class UsersRemoteDataHolder(
    private val webService: WebService,
    private val apiModelMapper: ModelMapper<User, UserApi>,
) : IUserRemoteDataHolder {
    override suspend fun getUsers(ids: Array<String>): List<User> {
        return safeApiCall {
            webService.getUsers(ids)
        }.let { users ->
            users.map {
                apiModelMapper.toModel(it)
            }
        }
    }

    override suspend fun getUserById(id: String): User? {
        return safeApiCall {
            webService.getUser(id)
        }.let { user ->
            apiModelMapper.toModel(user)
        }
    }
}