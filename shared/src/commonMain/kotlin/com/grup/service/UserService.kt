package com.grup.service

import com.grup.exceptions.EmptyArgumentException
import com.grup.exceptions.NotCreatedException
import com.grup.interfaces.IImagesRepository
import com.grup.interfaces.IUserRepository
import com.grup.models.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class UserService : KoinComponent {
    private val userRepository: IUserRepository by inject()
    private val imagesRepository: IImagesRepository by inject()

    suspend fun createMyUser(
        username: String,
        displayName: String,
        venmoUsername: String?,
        profilePicture: ByteArray
    ): User {
        return userRepository.createMyUser(username, displayName, venmoUsername)?.also { user ->
            updateProfilePicture(user, profilePicture)
        } ?: throw NotCreatedException("Error creating user object")
    }

    fun getMyUser(): User? {
        return userRepository.findMyUser()
    }

    suspend fun getUserByUsername(username: String): User? {
        if (username.isBlank()) {
            throw EmptyArgumentException("Please enter a username")
        }
        return userRepository.findUserByUsername(username)
    }

    suspend fun updateUser(user: User, block: User.() -> Unit): User {
        return userRepository.updateUser(user, block)
    }

    suspend fun updateProfilePicture(user: User, profilePicture: ByteArray) {
        if (user.profilePictureURL.isNotBlank()) {
            imagesRepository.deleteProfilePicture(user.profilePictureURL)
        }
        imagesRepository.uploadProfilePicture(user.id, profilePicture).let { profilePictureURL ->
            userRepository.updateUser(user) {
                this.profilePictureURL = profilePictureURL
            }
        }
    }
}