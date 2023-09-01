package com.epicdevler.ami.minote.data.repositories

import android.util.Log
import com.epicdevler.ami.minote.data.datasource.DummyNotesData
import com.epicdevler.ami.minote.data.datasource.DummyNotesData.TagContains
import com.epicdevler.ami.minote.data.datasource.NotesDataSource
import com.epicdevler.ami.minote.data.datasource.TagsDataSource
import com.epicdevler.ami.minote.data.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import kotlin.time.Duration.Companion.seconds

object TagsRepo {

    private const val TAG = "TAGS_REPO"

    private val tagsDS = TagsDataSource
    private val notesDS = NotesDataSource

    private val _tags: MutableStateFlow<Result<List<DummyNotesData.Tag>>> =
        MutableStateFlow(Result.Idle())
    val tags: StateFlow<Result<List<DummyNotesData.Tag>>> = _tags.asStateFlow()

    suspend fun get(): Unit = withContext(Dispatchers.IO) {
        try {
            tagsDS.get().collect {
                _tags.tryEmit(Result.Success(data = it.map { tagDto ->
                    DummyNotesData.Tag(
                        id = tagDto.id.toHexString(),
                        title = tagDto.title,
                        availableNotes = tagDto.notes.size
                    )
                }))
            }

        } catch (e: Exception) {
            _tags.tryEmit(
                Result.Failed(message = e.message ?: e.localizedMessage)
            )
        }
    }


    suspend fun create(title: String): Result<Nothing> = withContext(Dispatchers.IO) {
        try {
            tagsDS.create(title = title.trim())
            Result.Success()
        } catch (e: Exception) {
            Result.Failed(message = e.message ?: e.localizedMessage)
        }
    }


    private val _tagNotes: MutableStateFlow<Result<DummyNotesData.TagNotes>> =
        MutableStateFlow(Result.Idle())

    val tagNotes: StateFlow<Result<DummyNotesData.TagNotes>> = _tagNotes.asStateFlow()

    suspend fun getNotes(
        tagId: String
    ): Unit = withContext(Dispatchers.IO) {
        try {
            notesDS.tagNotes(ObjectId(tagId)).collect { tagSchema ->
                Log.e(TAG, "getNotes: ${tagSchema?.title}")
                Log.e(TAG, "getNotes: ${tagSchema?.notes?.map { it.title }}")
                if (tagSchema != null) {
                    _tagNotes.tryEmit(
                        Result.Success(
                            data = DummyNotesData.TagNotes(
                                id = tagSchema.id.toHexString(),
                                title = tagSchema.title,
                                notes = tagSchema.notes.map { it.asNote }
                            )
                        )
                    )
                } else {
                    _tagNotes.tryEmit(
                        Result.Failed(message = "Requested tag was not found, try again.")
                    )
                }
            }
        } catch (e: Exception) {
            _tagNotes.tryEmit(
                Result.Failed(message = e.message ?: e.localizedMessage)
            )
        }
    }

    fun checkIfInTag(noteId: String): Flow<Result<List<TagContains>>> = callbackFlow {
        withContext(Dispatchers.IO) {
            try {
                tagsDS.get().collect { tags ->
                    val lists = mutableListOf<TagContains>()
                    for (tag in tags) {
                        lists.add(
                            TagContains(
                                id = tag.id.toHexString(),
                                title = tag.title,
                                contains = tag.notes.find { it.id.toHexString() == noteId } != null
                            )
                        )
                    }
                    trySendBlocking(Result.Success(data = lists.toList()))
                }
            } catch (e: Exception) {
                trySendBlocking(Result.Failed(message = e.message ?: e.localizedMessage))
            }
        }
    }

    suspend fun addNoteToTag(tagId: String, noteId: String): Result<Nothing> {
        return withContext(Dispatchers.IO) {
            try {
                tagsDS.addNoteTo(tagId = ObjectId(tagId), noteId = ObjectId(noteId))
                Result.Success()
            } catch (e: Exception) {
                Result.Failed(message = e.message ?: e.localizedMessage)
            }
        }
    }

}

suspend fun dummyDelay(maxLoad: Int = 3) {
    delay(IntRange(0, maxLoad).random().seconds)
}