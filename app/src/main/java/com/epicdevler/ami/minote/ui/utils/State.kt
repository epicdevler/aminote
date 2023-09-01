package com.epicdevler.ami.minote.ui.utils

import android.content.Context
import androidx.annotation.StringRes

sealed interface State<T> {

    class Idle<T> : State<T>

    class Loading<T>(val message: UiText = UiText.None) : State<T>

    class Error<T>(
        val reason: Reason,
        val message: UiText = UiText.None
    ) : State<T> {
        enum class Reason {
            EmptyData, CouldNotSaveNote, UnClassified, EmptyField
        }
    }

    class Success<T>(
        val data: T,
        val message: UiText = UiText.None
    ) : State<T>

}

@Suppress("LocalVariableName")
sealed class UiText(
    val message: String? = null,
    @StringRes val resId: Int? = null,
    val formatArgs: Array<out Any> = emptyArray()
) {

    object None : UiText()
    class NetworkString(message: String) : UiText(message = message)
    class ResString(@StringRes resId: Int, vararg formatArgs: Any) :
        UiText(resId = resId, formatArgs = formatArgs)

    fun UiText.value(context: Context? = null): String {
        return when (this) {
            is NetworkString -> {
                message ?: context?.getString(resId!!, *formatArgs) ?: ""
            }

            is ResString -> {
                val transformedArgs = formatArgs.map {
                    try {
                        context?.getString(it as Int)
                    } catch (_: Exception) {
                        it
                    }
                }.toTypedArray()
                context?.getString(resId!!, *transformedArgs) ?: message ?: ""
            }

            is None -> ""
        }
    }
}