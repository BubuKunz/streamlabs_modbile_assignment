package com.streamlabs.data.repo

import com.streamlabs.data.api.UsersRemoteDataHolder
import com.streamlabs.data.local.UsersLocalDataHolder
import com.streamlabs.entity.data.IUserRepository
import com.streamlabs.entity.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class UserRepository(
    private val usersLocalDataHolder: UsersLocalDataHolder,
    private val usersRemoteDataHolder: UsersRemoteDataHolder,
) : IUserRepository {

    override suspend fun getUsers(
        fetch: Boolean,
        ids: List<String>
    ): Flow<List<User>> {
        return if (fetch) {
            usersLocalDataHolder.observeUsers()
                .map { users ->
                    users.filter { ids.contains(it.id) }
                }
                .combine(
                    flow {
                        fetchUsers(ids)
                        emit(Unit)
                    }
                ) { videos, _ ->
                    videos
                }
        } else {
            usersLocalDataHolder.observeUsers()
        }
    }

    override suspend fun fetchUsers(ids: List<String>): List<User> {
        val apiUsers = usersRemoteDataHolder.getUsers(ids.toTypedArray())
        usersLocalDataHolder.updateUsers(apiUsers)
        return apiUsers
    }

    override suspend fun fetchUser(id: String): User? {
        val apiUser = usersRemoteDataHolder.getUserById(id)?.copy(id = id)
        apiUser?.let {
            usersLocalDataHolder.updateUsers(listOf(apiUser))
        }
        return apiUser
    }
}