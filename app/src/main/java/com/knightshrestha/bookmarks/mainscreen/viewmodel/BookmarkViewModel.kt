package com.knightshrestha.bookmarks.mainscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knightshrestha.bookmarks.core.helpers.SortType
import com.knightshrestha.bookmarks.core.helpers.parseDate
import com.knightshrestha.bookmarks.core.repository.DataStoreRepository
import com.knightshrestha.bookmarks.mainscreen.repository.ApolloRepository
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
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val remoteRepository: ApolloRepository,
    private val dataStoreRepository: DataStoreRepository

) : ViewModel() {
    private val _bookmarkState = MutableStateFlow(BookmarkState())
    private val _sortType = MutableStateFlow(SortType.TIME)


    @OptIn(ExperimentalCoroutinesApi::class)
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
        }

    val state = combine(_bookmarkState, _bookmarks, _sortType) {bookmarkState, groupedBookmarks, sortType->
        bookmarkState.copy(
            bookmarks = groupedBookmarks,
            sortedBy = sortType
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), BookmarkState())

}