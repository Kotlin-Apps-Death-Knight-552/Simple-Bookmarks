package com.knightshrestha.bookmarks.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.knightshrestha.bookmarks.graphql.InsertBookmarkMutation
import com.knightshrestha.bookmarks.graphql.LiveBookmarkListSubscription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepository @Inject constructor(private val apolloClient: ApolloClient) {

    fun bookmarkLiveFlow(): Flow<List<LiveBookmarkListSubscription.Bookmark>> {
        return  apolloClient.subscription(LiveBookmarkListSubscription())
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