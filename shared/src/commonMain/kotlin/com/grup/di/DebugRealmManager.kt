package com.grup.di

import com.grup.exceptions.EntityAlreadyExistsException
import com.grup.exceptions.login.InvalidEmailPasswordException
import com.grup.interfaces.DBManager
import io.realm.kotlin.mongodb.*
import io.realm.kotlin.mongodb.exceptions.BadRequestException
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException

internal class DebugRealmManager private constructor(): RealmManager(true) {
    companion object {
        suspend fun silentSignIn(): DBManager? {
            return debugApp.currentUser?.let {
                DebugRealmManager().apply { open() }
            }
        }

        suspend fun loginEmailPassword(email: String, password: String): DBManager {
            try {
                return loginRealmManager(Credentials.emailPassword(email, password))
            } catch (e: InvalidCredentialsException) {
                throw InvalidEmailPasswordException()
            }
        }

        suspend fun registerEmailPassword(email: String, password: String): DBManager {
            try {
                debugApp.emailPasswordAuth.registerUser(email, password)
            } catch (e: UserAlreadyExistsException) {
                throw EntityAlreadyExistsException("Email already exists")
            } catch (e: BadRequestException) {
                // TODO: Bad email/bad password exception
            }
            return loginEmailPassword(email, password)
        }

        private suspend fun loginRealmManager(credentials: Credentials): DBManager {
            debugApp.login(credentials)
            return DebugRealmManager().apply { open() }
        }
    }
}
