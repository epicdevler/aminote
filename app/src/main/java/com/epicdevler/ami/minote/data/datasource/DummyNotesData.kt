package com.epicdevler.ami.minote.data.datasource

object DummyNotesData {

        val notes = listOf(
            Note(
                id = 1,
                title = "Lorem Ipsom Meeting",
                content = "Yorem ipsum dolodignissim, metus nec fringilla accumsan, risus sem sollicitudin lacus, ut ",
                formattedDate = "Jun 9, 23",
                tag = ""
            ),
            Note(
                id = 2,
                title = "Lorem Ipsom Meeting",
                content = "Yorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eu turpis molestie, dictum est a, mattis tellus. Sed dignissim, metus nec fringilla accumsan, risus sem sollicitudin lacus, ut ",
                formattedDate = "Jun 9, 23",
            ),
            Note(
                id = 3,
                title = "Some title",
                content = "Something has to happen.",
                formattedDate = "Jun 9, 23",
            ),
            Note(
                id = 4,
                title = "Lorem Ipsom Meeting",
                content = "Yorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eu turpis molestie, dictum est a, mattis tellus. Sed dignissim, metus nec fringilla accumsan, risus sem sollicitudin lacus, ut ",
                formattedDate = "Jun 9, 23",
            ),
        )

        val tags = listOf(
            Tag(
                id = 0,
                title = "MyDairy",
                availableNotes = 6
            ),
            Tag(
                id = 1,
                title = "Work",
                availableNotes = 100
            ),
        )


        data class Tag(
            val id: Int,
            val title: String,
            val availableNotes: Int
        )

        data class Note(
            val id: Int,
            val content: String,
            val title: String,
            val formattedDate: String,
            val tag: String = "Tag"
        )

    }