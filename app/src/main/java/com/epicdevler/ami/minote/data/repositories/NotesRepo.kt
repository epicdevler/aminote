package com.epicdevler.ami.minote.data.repositories

import com.epicdevler.ami.minote.data.datasource.DummyNotesData
import com.epicdevler.ami.minote.data.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

object NotesRepo {

    private val _notes: MutableStateFlow<Result<List<DummyNotesData.Note>>> =
        MutableStateFlow(Result.Idle())

    val notes: StateFlow<Result<List<DummyNotesData.Note>>> = _notes.asStateFlow()

    suspend fun get(): Unit = withContext(Dispatchers.IO) {
        try {
            delay(IntRange(0, 8).random().seconds)
            _notes.tryEmit(Result.Success(data = DummyNotesData.notes))
        } catch (e: Exception) {
            _notes.tryEmit(
                Result.Failed(message = e.message ?: e.localizedMessage)
            )
        }
    }
}
