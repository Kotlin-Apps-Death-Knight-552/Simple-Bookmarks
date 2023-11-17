package com.knightshrestha.bookmarks.mainscreen.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_list")
data class Bookmark(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val link: String,
    val time: String
)
