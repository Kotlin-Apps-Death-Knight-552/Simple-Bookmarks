package com.knightshrestha.bookmarks.mainscreen.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.knightshrestha.bookmarks.core.repository.DataStoreRepository
import com.knightshrestha.bookmarks.graphql.DeleteBookmarkMutation
import com.knightshrestha.bookmarks.graphql.InsertBookmarkMutation
import com.knightshrestha.bookmarks.graphql.LiveBookmarkListSubscription
import com.knightshrestha.bookmarks.graphql.UpdateBookmarkMutation
import com.knightshrestha.bookmarks.mainscreen.converter.toLocalType
import com.knightshrestha.bookmarks.mainscreen.models.Bookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ApolloRepository @Inject constructor(private val apolloClient: ApolloClient, private val dataStoreRepository: DataStoreRepository) :
    BookmarkRepository {
    override fun getBookmarks(): Flow<List<Bookmark>> {
        val response = apolloClient.subscription(LiveBookmarkListSubscription()).toFlow()
            .map { response -> response.data }
            .map { response -> response?.bookmarks ?: emptyList() }
            .map { list ->
                list.map {
                    it.toLocalType()
                }

            }

        return response
    }

    override suspend fun insertBookmark(name: String, link: String): Pair<Boolean, String?> {
        val userID = runBlocking {
            dataStoreRepository.getUserID().first()
        } ?: return Pair(false, "No user id available")

        val action = apolloClient.mutation(InsertBookmarkMutation(
            name = Optional.present(name),
            link = Optional.present(link),
            author_id = Optional.present(userID)
        )).execute()

        if (action.errors != null) return Pair(false, action.errors!!.first().message)

        return Pair(true, null)
    }

    override suspend fun insertBookmarks(bookmarks: List<Bookmark>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateBookmark(bookmark: Bookmark): Pair<Boolean, String?> {
        val action = apolloClient.mutation(UpdateBookmarkMutation(
            id = bookmark.id,
            name = Optional.present(bookmark.name),
            link = Optional.present(bookmark.link)
        )).execute()

        if (action.errors != null) return Pair(false, action.errors!!.first().message)

        return Pair(true, null)
    }

    override suspend fun deleteBookmark(bookmark: Bookmark): Pair<Boolean, String?> {
        val action = apolloClient.mutation(DeleteBookmarkMutation(bookmark.id)).execute()

        if (action.errors != null) return Pair(false, action.errors!!.first().message)

        return Pair(true, null)
    }
}