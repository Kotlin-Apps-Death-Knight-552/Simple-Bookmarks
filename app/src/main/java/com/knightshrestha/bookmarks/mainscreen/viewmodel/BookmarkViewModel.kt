package com.knightshrestha.bookmarks.mainscreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knightshrestha.bookmarks.core.helpers.SortType
import com.knightshrestha.bookmarks.mainscreen.repository.BookmarkRepository
import com.knightshrestha.bookmarks.core.repository.DataStoreRepository
import com.knightshrestha.bookmarks.mainscreen.state.BookmarkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val dataStoreRepository: DataStoreRepository

) : ViewModel() {
    private val _bookmarkState = MutableStateFlow(BookmarkState())
    private val _sortType = MutableStateFlow(SortType.NAME)


    @OptIn(ExperimentalCoroutinesApi::class)
    private var _bookmarkList = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.NAME -> bookmarkRepository.bookmarksByName()
                SortType.TIME -> bookmarkRepository.bookmarksByTime()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())



    val state = combine(
        _bookmarkState,
        _bookmarkList,
        _sortType
    ) { bookmarkState, bookmarks, sortType ->
        bookmarkState.copy(
            bookmarks = bookmarks,
            sortType = sortType

        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), BookmarkState())


    fun switchSortType() {
        when (_sortType.value) {
            SortType.NAME -> {
                _sortType.update {
                    SortType.TIME
                }
            }
            SortType.TIME -> {
                _sortType.update {
                    SortType.NAME
                }
            }
        }

    }

    fun insertBookmark(name: String, link: String) {
        viewModelScope.launch {
            val userID = runBlocking {
                dataStoreRepository.getUserID().first()
            }
            if (userID.isNullOrBlank()) {
                bookmarkRepository.insertBookmark(
                    link, name, userID!!
                ).isNullOrBlank().let {
                    when (it) {
                        true -> {
                            Log.d("Insert", "Successful")
                        }
                        false -> {}
                    }
                }
            }
        }
    }



}