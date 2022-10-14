package com.grup.objects

import IdSerializer
import io.realm.kotlin.types.ObjectId
import kotlinx.serialization.Serializable

@Serializable(with = IdSerializer::class)
class Id() {
    private var id: ObjectId = ObjectId.create()
    constructor(stringId: String): this() {
        this.id = ObjectId.from(stringId)
    }
    override fun toString() = id.toString()
}