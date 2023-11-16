package com.knightshrestha.bookmarks.mainscreen.converter

import com.knightshrestha.bookmarks.graphql.LiveBookmarkListSubscription
import com.knightshrestha.bookmarks.mainscreen.models.Bookmark

fun LiveBookmarkListSubscription.Bookmark.toLocalType(): Bookmark {

    return Bookmark(
        id = id.toString(),
        name = name,
        link = link,
        time = time.toString()
    )

}