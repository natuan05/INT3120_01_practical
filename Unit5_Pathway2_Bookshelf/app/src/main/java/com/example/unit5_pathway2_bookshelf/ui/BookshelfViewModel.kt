package com.example.unit5_pathway2_bookshelf.ui // Thay bằng package của bạn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unit5_pathway2_bookshelf.BookshelfApplication
import com.example.unit5_pathway2_bookshelf.data.BooksRepository
import kotlinx.coroutines.launch
import java.io.IOException

class BookshelfViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    init {
        getBooks("history")
    }

    fun getBooks(query: String) {
        viewModelScope.launch {
            bookshelfUiState = BookshelfUiState.Loading
            bookshelfUiState = try {
                BookshelfUiState.Success(booksRepository.getBookPhotos(query))
            } catch (e: IOException) {
                BookshelfUiState.Error
            }
        }
    }

    /**
     * Factory để tạo ViewModel với booksRepository được tiêm vào.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val booksRepository = application.container.booksRepository
                BookshelfViewModel(booksRepository = booksRepository)
            }
        }
    }
}