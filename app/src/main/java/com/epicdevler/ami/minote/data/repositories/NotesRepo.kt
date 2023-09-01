package com.epicdevler.ami.minote.data.repositories

import android.util.Log
import com.epicdevler.ami.minote.data.datasource.DummyNotesData
import com.epicdevler.ami.minote.data.datasource.NoteDataSource
import com.epicdevler.ami.minote.data.datasource.NotesDataSource
import com.epicdevler.ami.minote.data.datasource.db.schemas.NoteSchema
import com.epicdevler.ami.minote.data.datasource.db.schemas.UpdateNote
import com.epicdevler.ami.minote.data.utils.Result
import com.epicdevler.ami.minote.data.utils.shortFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

object NotesRepo {

    private const val TAG = "NOTES_REPO"

    private val notesDS: NotesDataSource = NotesDataSource
    private val noteDS: NoteDataSource = NoteDataSource

    private val _notes: MutableStateFlow<Result<List<DummyNotesData.Note>>> =
        MutableStateFlow(Result.Idle())
    val notes: StateFlow<Result<List<DummyNotesData.Note>>> = _notes.asStateFlow()

    suspend fun get(): Unit = withContext(Dispatchers.IO) {
        Log.e(TAG, "get: Called")
        try {
            notesDS.notes().collect {
                _notes.tryEmit(
                    Result.Success(
                        data = it.map { schema ->
                            schema.asNote
                        }
                    )
                )
                it.forEach { note ->
                    Log.e(TAG, "get: ${note.title} -> ${note.tag?.title}")

                }
            }
        } catch (e: Exception) {
            _notes.tryEmit(Result.Failed(message = e.message ?: e.localizedMessage))
        }
    }

    private val _note: MutableStateFlow<Result<DummyNotesData.Note>> =
        MutableStateFlow(Result.Idle())
    val note: StateFlow<Result<DummyNotesData.Note>> = _note.asStateFlow()
    suspend fun get(noteId: String?): Unit = withContext(Dispatchers.IO) {
        try {
            noteDS.note(noteId?.let { ObjectId(it) }).collect { noteResult ->
                when (noteResult) {
                    is Result.Idle -> Unit
                    is Result.Failed -> _note.tryEmit(Result.Failed(message = noteResult.message))
                    is Result.Success -> {
                        val noteSchema = noteResult.data!!
                        _note.tryEmit(
                            Result.Success(
                                data = noteSchema.asNote
                            )
                        )
                    }
                }
            }

        } catch (e: Exception) {
            _notes.tryEmit(
                Result.Failed(message = e.message ?: e.localizedMessage)
            )
        }
    }

    suspend fun save(note: DummyNotesData.Note): Result<Nothing> {
        return noteDS.update(
            UpdateNote(
                id = note.id,
                title = note.title,
                content = note.content,
            )
        )

    }
}


val NoteSchema.asNote: DummyNotesData.Note
    get() {
        return DummyNotesData.Note(
            id = id.toHexString(),
            content = content,
            title = title,
            formattedDate = createdDate.shortFormat,
            tag = DummyNotesData.NoteTag(
                id = tag?.id?.toHexString() ?: "",
                title = tag?.title ?: ""
            )
        )
    }