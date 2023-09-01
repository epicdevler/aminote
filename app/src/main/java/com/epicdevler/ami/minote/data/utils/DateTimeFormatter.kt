package com.epicdevler.ami.minote.data.utils

import io.realm.kotlin.types.RealmInstant
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

val RealmInstant.shortFormat: String
    get() {
        return SimpleDateFormat("MMM d, yy", Locale.ROOT).format(Date.from(this.toInstant()))
    }


private fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    // The value always lies in the range `-999_999_999..999_999_999`.
    // minus for timestamps before epoch, positive for after
    val nano: Int = this.nanosecondsOfSecond
    return if (sec >= 0) { // For positive timestamps, conversion can happen directly
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        // For negative timestamps, RealmInstant starts from the higher value with negative
        // nanoseconds, while Instant starts from the lower value with positive nanoseconds
        // TODO This probably breaks at edge cases like MIN/MAX
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}