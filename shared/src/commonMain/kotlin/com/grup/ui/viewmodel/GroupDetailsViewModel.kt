package com.grup.ui.viewmodel

import cafe.adriel.voyager.core.model.coroutineScope
import com.grup.models.*
import com.grup.ui.models.TransactionActivity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class GroupDetailsViewModel : LoggedInViewModel() {
    // Hot flow containing User's UserInfos
    private val _myUserInfosFlow = apiServer.getMyUserInfosAsFlow()
    val myUserInfo: StateFlow<UserInfo?> =
        _myUserInfosFlow.map { userInfos ->
            userInfos.find { userInfo ->
                userInfo.groupId == selectedGroup.id
            }
        }.asState()

    // DebtActions belonging to the selectedGroup, mapped to TransactionActivity
    private val _debtActionsFlow = apiServer.getAllDebtActionsAsFlow()
        .map { debtActions ->
            debtActions.filter { debtAction ->
                debtAction.groupId == selectedGroup.id
            }
        }
    private val debtActionsAsTransactionActivity: Flow<List<TransactionActivity>> =
        _debtActionsFlow.map { debtActions ->
            debtActions.filter { debtAction ->
                debtAction.totalAmount > 0
            }.map { debtAction ->
                TransactionActivity.CreateDebtAction(debtAction)
            }
        }

    // SettleActions belonging to the selectedGroup, mapped to TransactionActivity
    private val _settleActionsFlow = apiServer.getAllSettleActionsAsFlow()
        .map { settleActions ->
            settleActions.filter { settleAction ->
                settleAction.groupId == selectedGroup.id
            }
        }
    // Active SettleActions to be displayed, oldest first
    val activeSettleActions: StateFlow<List<SettleAction>> =
        _settleActionsFlow.map { settleActions ->
            settleActions.filter { settleAction ->
                settleAction.remainingAmount > 0
            }.sortedBy { it.date }
        }.asState()

    private val completedSettleActionsAsTransactionActivity: Flow<List<TransactionActivity>> =
        _settleActionsFlow.map { settleActions ->
            settleActions.filter { settleAction ->
                settleAction.remainingAmount == 0.0
            }.map { settleAction ->
                TransactionActivity.CreateSettleAction(settleAction)
            }
        }
    private val settleActionTransactionsAsTransactionActivity: Flow<List<TransactionActivity>> =
        _settleActionsFlow.map { settleActions ->
            settleActions.flatMap { settleAction ->
                settleAction.transactionRecords.filter { transactionRecord ->
                    transactionRecord.isAccepted
                }.map { transactionRecord ->
                    TransactionActivity.SettlePartialSettleAction(
                        settleAction,
                        transactionRecord
                    )
                }
            }
        }

    // Hot flow combining all TransactionActivity flows to be displayed as recent activity in UI
    val groupActivity: StateFlow<List<TransactionActivity>> =
        combine(
            debtActionsAsTransactionActivity,
            completedSettleActionsAsTransactionActivity,
            settleActionTransactionsAsTransactionActivity
        ) { allTransactionActivities: Array<List<TransactionActivity>> ->
            allTransactionActivities.flatMap { it }.sortedByDescending { transactionActivity ->
                transactionActivity.date
            }
        }.asInitialEmptyState()

    // SettleAction
    fun acceptSettleActionTransaction(
        settleAction: SettleAction,
        transactionRecord: TransactionRecord
    ) = coroutineScope.launch {
        apiServer.acceptSettleActionTransaction(settleAction, transactionRecord)
    }
}