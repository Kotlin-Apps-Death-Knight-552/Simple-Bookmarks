package com.knightshrestha.bookmarks.mainscreen.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.knightshrestha.bookmarks.graphql.InsertBookmarkMutation
import com.knightshrestha.bookmarks.graphql.LiveBookmarkListOrderedByDateSubscription
import com.knightshrestha.bookmarks.graphql.LiveBookmarkListOrderedByNameSubscription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepository @Inject constructor(private val apolloClient: ApolloClient) {

    fun bookmarksByTime(): Flow<List<LiveBookmarkListOrderedByNameSubscription.Bookmark>> {
        return apolloClient.subscription(LiveBookmarkListOrderedByDateSubscription())
            .toFlow()
            .map { response -> response.data }
            .map { data -> data?.bookmarks ?: emptyList() }
            .map { list ->
                list.map { bookmark ->
                    LiveBookmarkListOrderedByNameSubscription.Bookmark(
                        bookmark.id,
                        bookmark.link,
                        bookmark.name,
                        bookmark.time,
                        bookmark.__typename
                    )

                }
            }

    }

    fun bookmarksByName(): Flow<List<LiveBookmarkListOrderedByNameSubscription.Bookmark>> {
        return apolloClient.subscription(LiveBookmarkListOrderedByNameSubscription())
            .toFlow()
            .map { response -> response.data }
            .map { data -> data?.bookmarks ?: emptyList() }
    }


    suspend fun insertBookmark(link: String, name: String, userId: String): String? {
        val response = apolloClient.mutation(
            InsertBookmarkMutation(
                name = Optional.present(name),
                link = Optional.present(link),
                author_id = Optional.present(userId)
            )
        ).execute()

        response.let {
            if (it.errors != null && it.errors!!.isNotEmpty()) {
                return it.errors!![0].message
            }
            return null
        }
    }

}