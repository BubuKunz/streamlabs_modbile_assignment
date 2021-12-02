package com.streamlabs.entity.data

import com.streamlabs.entity.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    /**
     * @param fetch - if true receive users from network and start observing cache, otherwise
     * only observes cache
     * @throws EntityError.kt in case of any errors (including HTTP errors)
     * @return obserbale list of users
     */
    suspend fun getUsers(fetch: Boolean = false, ids: List<String>): Flow<List<User>>

    /**
     * Fetches users from network by [ids] and store it to cache
     * @throws EntityError.kt in case of any errors (including HTTP errors)
     * @return list of users
     */
    suspend fun fetchUsers(ids: List<String>): List<User>

    /**
     * Fetches user from network by [id] and store it to cache
     * @throws EntityError.kt in case of any errors (including HTTP errors)
     * @return list of users
     */
    suspend fun fetchUser(id: String): User?
}