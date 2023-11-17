package com.knightshrestha.bookmarks.mainscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knightshrestha.bookmarks.core.helpers.SortType
import com.knightshrestha.bookmarks.core.helpers.parseDate
import com.knightshrestha.bookmarks.core.repository.DataStoreRepository
import com.knightshrestha.bookmarks.mainscreen.events.BookmarkEvent
import com.knightshrestha.bookmarks.mainscreen.repository.ApolloRepository
import com.knightshrestha.bookmarks.mainscreen.repository.RoomRepository
import com.knightshrestha.bookmarks.mainscreen.state.BookmarkState
import com.knightshrestha.bookmarks.mainscreen.state.GroupedBookmarks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val remoteRepository: ApolloRepository,
    private val localRepository: RoomRepository,
    private val dataStoreRepository: DataStoreRepository

) : ViewModel() {
    private val _bookmarkState = MutableStateFlow(BookmarkState())
    private val _sortType = MutableStateFlow(SortType.NAME)
    private var _bookmarks = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.NAME -> remoteRepository.getBookmarks().map { bookmarkList ->
                    bookmarkList.groupBy { it.name.first() }
                        .toSortedMap()
                        .map {
                            GroupedBookmarks(
                                label = it.key.toString(),
                                items = it.value
                            )
                        }
                }

                SortType.TIME -> remoteRepository.getBookmarks().map { bookmarkList ->
                    bookmarkList.groupBy { parseDate(it.time) }
                        .toSortedMap(Comparator.reverseOrder())
                        .map {
                            GroupedBookmarks(
                                label = it.key.toString(),
                                items = it.value
                            )
                        }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(
        _bookmarkState,
        _bookmarks,
        _sortType
    ) { bookmarkState, groupedBookmarks, sortType ->
        bookmarkState.copy(
            bookmarks = groupedBookmarks,
            sortedBy = sortType
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), BookmarkState())

    fun onBookmarkEvent(event: BookmarkEvent) {
        when (event) {
            is BookmarkEvent.DeleteBookmark -> {
                viewModelScope.launch {
                    val response = remoteRepository.deleteBookmark(
                        bookmark = event.bookmark
                    )
                    if (response.first) {
                        _bookmarkState.update {
                            it.copy(
                                isInserting = false,
                                snackMessage = "Delete Successful "
                            )
                        }
                    } else {
                        _bookmarkState.update {
                            it.copy(
                                snackMessage = "${response.second}"
                            )
                        }
                    }
                }
            }

            BookmarkEvent.HideDialog -> {
                _bookmarkState.update { it.copy(
                    isInserting = false
                ) }
            }
            BookmarkEvent.InsertBookmark -> {
                val name = state.value.name
                val link = state.value.name
                if (name.isBlank() || link.isBlank()) return
                viewModelScope.launch {
                    val response = remoteRepository.insertBookmark(
                        name = name, link = link
                    )
                    if (response.first) {
                        _bookmarkState.update {
                            it.copy(
                                isInserting = false,
                                name = "",
                                link = "",
                                snackMessage = "Insert Successful "
                            )
                        }
                    } else {
                        _bookmarkState.update {
                            it.copy(
                                snackMessage = "${response.second}"
                            )
                        }
                    }
                }
            }

            is BookmarkEvent.SetLink -> {
                _bookmarkState.update { it.copy(
                    link = event.link
                ) }
            }
            is BookmarkEvent.SetName -> {
                _bookmarkState.update { it.copy(
                    name = event.name
                ) }
            }
            is BookmarkEvent.SortBookmarks -> {
                _sortType.update {
                    when (it) {
                        SortType.NAME -> SortType.TIME
                        SortType.TIME -> SortType.NAME
                    }
                }
            }
            BookmarkEvent.ToggleSaveBookmark -> {
                _bookmarkState.update { it.copy(
                    isInserting = true
                ) }
            }
            is BookmarkEvent.UpdateBookmark -> {

                val name = event.bookmark.name
                val link = event.bookmark.link
                if (name.isBlank() || link.isBlank()) return
                viewModelScope.launch {
                    val response = remoteRepository.updateBookmark(
                        bookmark = event.bookmark
                    )
                    if (response.first) {
                        _bookmarkState.update {
                            it.copy(
                                isInserting = false,
                                name = "",
                                link = "",
                                snackMessage = "Update Successful "
                            )
                        }
                    } else {
                        _bookmarkState.update {
                            it.copy(
                                snackMessage = "${response.second}"
                            )
                        }
                    }
                }
            }
        }
    }

}