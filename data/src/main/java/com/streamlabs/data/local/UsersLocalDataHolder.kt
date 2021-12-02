package com.streamlabs.data.local

import com.streamlabs.data.local.model.User as UserLocal
import com.streamlabs.data.mapper.ModelMapper
import com.streamlabs.entity.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal interface IUserLocalDataHolder {
    suspend fun updateUsers(users: List<User>): Boolean
    fun observeUsers(): Flow<List<User>>
    suspend fun getUsers(): List<User>
    suspend fun getUserById(id: String?): User?
    suspend fun getUserByName(name: String?): User?
}

// implementation can be easily switched to room or realm
internal class UsersLocalDataHolder(
    private val localModelMapper: ModelMapper<User, UserLocal>,
) : IUserLocalDataHolder {

    private val usersFlow = MutableStateFlow<List<UserLocal>>(emptyList())
    private val userIdMap = mutableMapOf<String?, UserLocal>()
    private val userNameMap = mutableMapOf<String?, UserLocal>()

    override suspend fun updateUsers(users: List<User>): Boolean {
        users.forEach {
            userIdMap[it.id] = localModelMapper.fromModel(it)
            userIdMap[it.userName] = localModelMapper.fromModel(it)
        }
        usersFlow.value = userIdMap.values.toList()
        return true
    }

    override fun observeUsers(): Flow<List<User>> = usersFlow.map { users ->
        users.map {
            localModelMapper.toModel(it)
        }
    }

    override suspend fun getUsers(): List<User> =
        usersFlow.value.map { localModelMapper.toModel(it) }

    override suspend fun getUserById(id: String?): User? =
        userIdMap[id]?.let { localModelMapper.toModel(it) }

    override suspend fun getUserByName(name: String?): User? =
        userNameMap[name]?.let { localModelMapper.toModel(it) }
}