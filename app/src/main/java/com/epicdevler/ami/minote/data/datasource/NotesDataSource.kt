package com.epicdevler.ami.minote.data.datasource

import android.util.Log
import com.epicdevler.ami.minote.data.datasource.db.Database
import com.epicdevler.ami.minote.data.datasource.db.schemas.NoteSchema
import com.epicdevler.ami.minote.data.datasource.db.schemas.TagSchema
import com.epicdevler.ami.minote.data.utils.Result
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

object NotesDataSource {

    private const val TAG = "NOTES_DS"

    private val database = Database.getInstance()

    private var notesJob: Job? = null
    private val _notes: MutableStateFlow<Result<List<NoteSchema>>> =
        MutableStateFlow(Result.Idle())
    val notes: StateFlow<Result<List<NoteSchema>>> = _notes.asStateFlow()
    /*
        suspend fun notes(): Unit = withContext(Dispatchers.IO) {
            notesJob = launch {
                try {
                    database.query<NoteSchema>().asFlow().collect {
                        val notesSchema = it.list.map {schema ->
                            DummyNotesData.Note(
                                id = schema.id.toHexString(),
                                content = schema.content,
                                title = schema.title,
                                formattedDate = schema.createdDate.shortFormat
                            )
                        }
                        _notes.tryEmit(Result.Success(data = notesSchema))
                        Log.e(TAG, "notes: ${notesSchema.map { schema -> schema.title }}")
                    }
                } catch (e: Exception) {
                    _notes.tryEmit(
                        Result.Failed(
                            message = e.message ?: e.localizedMessage
                        )
                    )
                    Log.e(TAG, "notes: ", e)
                }
            }
        }*/


    suspend fun notes(): Flow<List<NoteSchema>> = withContext(Dispatchers.IO) {
        database.query<NoteSchema>().sort("createdDate", sortOrder = Sort.DESCENDING).asFlow().map {
            it.list
        }
    }

    suspend fun tagNotes(
        noteId: ObjectId
    ): Flow<TagSchema?> = withContext(Dispatchers.IO) {
        database.query<TagSchema>("_id == $0", noteId).first().asFlow().map { it.obj }

    }


    private fun close() {
        notesJob?.cancel()
    }


}