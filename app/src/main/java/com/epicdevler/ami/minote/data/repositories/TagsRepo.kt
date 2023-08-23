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

object TagsRepo {

    private val _tags: MutableStateFlow<Result<List<DummyNotesData.Tag>>> =
        MutableStateFlow(Result.Idle())

    val tags: StateFlow<Result<List<DummyNotesData.Tag>>> = _tags.asStateFlow()

    suspend fun get(): Unit = withContext(Dispatchers.IO) {
        try {
            delay(IntRange(0, 8).random().seconds)
            _tags.tryEmit(Result.Success(data = DummyNotesData.tags))
        } catch (e: Exception) {
            _tags.tryEmit(
                Result.Failed(message = e.message ?: e.localizedMessage)
            )
        }
    }
}
