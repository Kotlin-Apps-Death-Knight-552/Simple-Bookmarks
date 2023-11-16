package com.knightshrestha.bookmarks.mainscreen.models

import androidx.room.PrimaryKey

data class Bookmark(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val link: String,
    val time: String
)
