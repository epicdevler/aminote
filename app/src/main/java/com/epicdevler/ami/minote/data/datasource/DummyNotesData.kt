package com.epicdevler.ami.minote.data.datasource

import io.realm.kotlin.types.RealmInstant

object DummyNotesData {

    val notes = listOf(
        NoteDto(
            id = "1",
            title = "Lorem Ipsom Meeting",
            content = "Yorem ipsum dolodignissim, metus nec fringilla accumsan, risus sem sollicitudin lacus, ut ",
            formattedDate = "Jun 9, 23",
            tagId = "0"
        ),
        NoteDto(
            id = "2",
            title = "Lorem Ipsom Meeting",
            content = "Yorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eu turpis molestie, dictum est a, mattis tellus. Sed dignissim, metus nec fringilla accumsan, risus sem sollicitudin lacus, ut ",
            formattedDate = "Jun 9, 23",
            tagId = "0"
        ),
        NoteDto(
            id = "3",
            title = "Some title",
            content = "Something has to happen.",
            formattedDate = "Jun 9, 23",
            tagId = "1"
        ),
        NoteDto(
            id = "4",
            title = "Lorem Ipsom Meeting",
            content = "Yorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eu turpis molestie, dictum est a, mattis tellus. Sed dignissim, metus nec fringilla accumsan, risus sem sollicitudin lacus, ut ",
            formattedDate = "Jun 9, 23",
            tagId = "1"
        ),
    )



    data class Tag(
        val id: String,
        val title: String,
        val availableNotes: Int
    )
    data class TagContains(
        val id: String,
        val title: String,
        val contains: Boolean
    )
    data class TagNotes(
        val id: String,
        val title: String,
        val notes: List<Note>
    )

    data class NoteTag(
        val id: String = "",
        val title: String = "",
    )

    data class Note(
        val id: String,
        override var content: String,
        override var title: String,
        val formattedDate: String,
        override var tag: NoteTag = NoteTag()
    ) : BaseNote(title = title, content = content, tag = tag, formattedCreateDate = formattedDate)


    data class TagDto(
        val id: String,
        val title: String,
    )

    data class NoteDto(
        val id: String,
        val content: String,
        val title: String,
        val formattedDate: String,
        val tagId: String
    )

}

open class BaseNote(
    open var title: String,
    open var content: String,
    open val tag: DummyNotesData.NoteTag = DummyNotesData.NoteTag(),
    open var formattedCreateDate: String
)