package com.epicdevler.ami.minote.data.datasource.db.schemas

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PersistedName
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class NoteSchema : RealmObject {
    @PersistedName("_id")
    @PrimaryKey
    var id: ObjectId = ObjectId()

    var title: String = ""
    var content: String = ""

    var createdDate: RealmInstant = RealmInstant.now()

    var tag: TagSchema? = null
}
data class NewNote(
    val title: String,
    val content: String,
    val createDate: RealmInstant
)
data class UpdateNote(
    val id: String,
    val title: String,
    val content: String
)

