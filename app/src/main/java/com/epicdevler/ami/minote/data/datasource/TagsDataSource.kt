package com.epicdevler.ami.minote.data.datasource

import android.util.Log
import com.epicdevler.ami.minote.data.datasource.db.Database
import com.epicdevler.ami.minote.data.datasource.db.schemas.NoteSchema
import com.epicdevler.ami.minote.data.datasource.db.schemas.TagSchema
import com.epicdevler.ami.minote.data.utils.DataConflict
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

object TagsDataSource {

    private val database = Database.getInstance()
    private const val TAG = "TAGS_DS"


    suspend fun get(): Flow<List<TagSchema>> {
        return withContext(Dispatchers.IO) {
            database.query<TagSchema>().sort("createdDate", sortOrder = Sort.DESCENDING).asFlow().map { it.list }
        }
    }

    suspend fun create(title: String) {
        withContext(Dispatchers.IO) {
            val checkResult = database.query<TagSchema>("title == $0", title).find().firstOrNull()
            if (checkResult == null || !checkResult.title.equals(title, true)) {
                database.write {
                    val a = copyToRealm(TagSchema().apply {
                        this@apply.title = title
                    })
                    Log.e(TAG, "create: ${a.title}")
                }
            } else {
                throw DataConflict(message = "Can't create $title, tag already exists.")
            }

        }
    }

    suspend fun addNoteTo(tagId: ObjectId, noteId: ObjectId) {
        withContext(Dispatchers.IO) {
            var removeFromOthers = false
            database.write {
                val note = query<NoteSchema>("_id == $0", noteId).find().first()
                findLatest(
                    query<TagSchema>("_id == $0", tagId).find().first()
                )?.apply {
                    val tagSchema = this
                    val foundNote = notes.find { it.id == noteId }
                    if (foundNote == null) {
                        notes.add(note)
                        findLatest(note)?.apply {
                            tag = tagSchema
                        }
                        removeFromOthers = true
                    } else {
                        val filtered = notes.filter { it.id != note.id }
                        notes = realmListOf<NoteSchema>().apply { addAll(filtered) }
                        findLatest(note)?.apply {
                            tag = null
                        }
                    }
                }
                if (removeFromOthers) {
                    query<TagSchema>("_id != $0", tagId).find().forEach {
                        findLatest(it)?.apply {
                            val filtered = notes.filter { _note -> _note.id != note.id }
                            notes = realmListOf<NoteSchema>().apply { addAll(filtered) }
                        }
                    }
                }
            }
        }
    }


}