package com.epicdevler.ami.minote.data.utils

sealed class Result<D>(
    val data: D? = null,
    val message: String = ""
) {
    class Idle<D> : Result<D>()
    class Success<D>(data: D? = null, message: String = "") : Result<D>(data, message)
    class Failed<D>(data: D? = null, message: String = "") : Result<D>(data, message)
}