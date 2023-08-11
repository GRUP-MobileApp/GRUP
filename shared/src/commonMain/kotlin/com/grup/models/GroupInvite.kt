package com.grup.models

abstract class GroupInvite internal constructor() : BaseEntity() {
    companion object {
        const val PENDING = "PENDING"
    }

    abstract val inviter: User
    abstract val inviteeId: String
    abstract val groupId: String
    abstract val groupName: String
    abstract val date: String
    abstract val dateAccepted: String
}
