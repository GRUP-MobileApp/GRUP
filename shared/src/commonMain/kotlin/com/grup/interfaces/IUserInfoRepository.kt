package com.grup.interfaces

import com.grup.models.UserInfo
import kotlinx.coroutines.flow.Flow

internal interface IUserInfoRepository : IRepository {
    fun createUserInfo(userInfo: UserInfo): UserInfo?

    fun findUserInfoByUser(userId: String, groupId: String): UserInfo?
    fun findMyUserInfosAsFlow(userId: String): Flow<List<UserInfo>>
    fun findAllUserInfosAsFlow(): Flow<List<UserInfo>>

    fun updateUserInfo(userInfo: UserInfo, block: (UserInfo) -> Unit): UserInfo?
}
