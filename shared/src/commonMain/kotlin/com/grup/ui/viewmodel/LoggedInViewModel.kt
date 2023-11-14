package com.grup.ui.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.grup.APIServer
import com.grup.models.Group
import com.grup.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal abstract class LoggedInViewModel : KoinComponent, ScreenModel {
    private companion object {
        private const val STOP_TIMEOUT_MILLIS: Long = 5000

        private val _selectedGroupMutable: MutableStateFlow<Group?> = MutableStateFlow(null)
    }

    protected val apiServer: APIServer by inject()

    protected open val userObject: User
        get() = apiServer.user

    var selectedGroup: Group?
        get() = _selectedGroupMutable.value
        set(value) { _selectedGroupMutable.value = value }

    protected fun <T> Flow<T>.asState() =
        this.let { flow ->
            runBlocking { flow.first() }.let { initialValue ->
                flow.stateIn(
                    screenModelScope,
                    SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                    initialValue
                )
            }
        }

    protected fun <T> Flow<List<T>>.asInitialEmptyState() =
        this.stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            emptyList()
        )

    protected fun <T> Flow<T>.asNotification(initialValue: T) =
        this.stateIn(
            screenModelScope,
            SharingStarted.Eagerly,
            initialValue
        )
}