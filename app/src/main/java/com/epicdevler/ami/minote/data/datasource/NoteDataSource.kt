@file:Suppress("LocalVariableName")

package com.epicdevler.ami.minote.data.datasource

import android.util.Log
import com.epicdevler.ami.minote.data.datasource.db.Database
import com.epicdevler.ami.minote.data.datasource.db.schemas.NoteSchema
import com.epicdevler.ami.minote.data.datasource.db.schemas.UpdateNote
import com.epicdevler.ami.minote.data.utils.Result
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

object NoteDataSource {

    private const val TAG = "NOTE_DS"

    private val database = Database.getInstance()

    private var tempNote: NoteSchema? = null

    suspend fun update(note: UpdateNote): Result<Nothing> {
        return withContext(Dispatchers.IO) {
            try {
                database.write {
                    val _note =
                        query<NoteSchema>("_id == $0", ObjectId(note.id)).find().firstOrNull()
                    if (_note != null) {
                        findLatest(_note)?.let {
//                        UPDATE NOTE
                            it.apply {
                                title = note.title
                                content = note.content
                            }
                        }
                    } else {
//                        SAVE THE TEMP NOTE
                        tempNote?.apply { title = note.title; content = note.content }
                        tempNote?.let { copyToRealm(it) }
                        tempNote = null
                    }
                }
                Result.Success()
            } catch (e: Exception) {
                Log.e(TAG, "save: ", e)
                Result.Failed(message = e.message ?: e.localizedMessage)
            }
        }
    }

    suspend fun note(noteId: ObjectId?): Flow<Result<NoteSchema>> = callbackFlow {
        val job = launch(Dispatchers.IO) {
            val id = if (noteId == null) {
                tempNote = NoteSchema()
                tempNote?.id
            } else noteId
            try {
                database.query<NoteSchema>("_id == $0", id).first().asFlow().collect {
                    if (it.obj == null) {
                        trySendBlocking(Result.Success(data = tempNote))
                    } else {
                        trySendBlocking(Result.Success(data = it.obj))
                    }
                }
            } catch (e: Exception) {
                trySendBlocking(Result.Failed(message = e.message ?: e.localizedMessage))
                Log.e(TAG, "note: ", e)
            }
        }
        awaitClose {
            job.cancel()
        }
    }


}


