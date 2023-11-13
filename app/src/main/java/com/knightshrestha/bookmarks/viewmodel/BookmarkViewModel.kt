package com.knightshrestha.bookmarks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knightshrestha.bookmarks.repository.BookmarkRepository
import com.knightshrestha.bookmarks.state.BookmarkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
//    private val dataStoreRepository: DataStoreRepository

) : ViewModel() {
    private val _bookmarkState = MutableStateFlow(BookmarkState())
    private var _bookmarkList = bookmarkRepository.bookmarkLiveFlow()

    val state = combine(
    _bookmarkState,
    _bookmarkList,

    ) { bookmarkState, bookmarks ->
        bookmarkState.copy(
            bookmarks = bookmarks,

        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), BookmarkState())


}