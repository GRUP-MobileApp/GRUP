package com.grup.other

import com.grup.exceptions.NotFoundException
import com.grup.models.realm.RealmDebtAction
import com.grup.models.realm.RealmGroupInvite
import com.grup.models.realm.RealmSettleAction
import com.grup.models.realm.RealmTransactionRecord
import com.grup.models.realm.RealmUserInfo
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.isValid
import io.realm.kotlin.query.RealmQuery
import io.realm.kotlin.types.BaseRealmObject
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun <T : RealmObject> MutableRealm.copyNestedObjectToRealm(instance: T): T =
    copyToRealm(getLatestFields(instance), UpdatePolicy.ERROR)

internal fun <T: BaseRealmObject> MutableRealm.getLatestFields(obj: T): T {
    return try {
        findLatest(obj) ?: throw NotFoundException("Object not found in realm")
    } catch (e: IllegalArgumentException) {
        obj
    }.apply {
        when (this) {
            is RealmGroupInvite -> {
                _inviter = getLatestFields(inviter)
            }
            is RealmSettleAction -> {
                _debteeUserInfo = getLatestFields(debteeUserInfo)
                _transactionRecords.forEachIndexed { i, transactionRecord ->
                    _transactionRecords[i] = getLatestFields(transactionRecord)
                }
            }
            is RealmDebtAction -> {
                _debteeUserInfo = getLatestFields(debteeUserInfo)
                _transactionRecords.forEachIndexed { i, transactionRecord ->
                    _transactionRecords[i] = getLatestFields(transactionRecord)
                }
            }
            is RealmTransactionRecord -> {
                _debtorUserInfo = getLatestFields(debtorUserInfo)
            }
            is RealmUserInfo -> {
                _user = getLatestFields(user)
            }
        }
    }
}

internal fun <T : BaseRealmObject> RealmQuery<T>.toResolvedListFlow(): Flow<List<T>> {
    return this.find().asFlow().map { resultsChange ->
        resultsChange.list.filter { obj ->
            try {
                obj.resolve()
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}

private fun BaseRealmObject.resolve() {
    if (!this.isValid()) {
        throw IllegalStateException("Unmanaged Realm object")
    }
    when (this) {
        is RealmGroupInvite -> {
            inviter.resolve()
        }
        is RealmSettleAction -> {
            debteeUserInfo.resolve()
            _transactionRecords.forEach { transactionRecord ->
                transactionRecord.debtorUserInfo.resolve()
            }
        }
        is RealmDebtAction -> {
            debteeUserInfo.resolve()
            _transactionRecords.forEach { transactionRecord ->
                transactionRecord.debtorUserInfo.resolve()
            }
        }
        is RealmUserInfo -> {
            user.resolve()
        }
        is RealmTransactionRecord -> {
            debtorUserInfo.resolve()
        }
        else -> { }
    }
}
