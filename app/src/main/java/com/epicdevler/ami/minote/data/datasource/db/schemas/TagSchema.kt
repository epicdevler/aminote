package com.epicdevler.ami.minote.data.datasource.db.schemas

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PersistedName
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class TagSchema : RealmObject {
    @PersistedName("_id")
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var title: String = ""
    var createdDate: RealmInstant = RealmInstant.now()
    var notes: RealmList<NoteSchema> = realmListOf()
}