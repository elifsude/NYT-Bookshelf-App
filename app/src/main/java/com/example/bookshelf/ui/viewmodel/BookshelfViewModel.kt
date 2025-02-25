package com.example.bookshelf.ui.viewmodel

import android.app.Application
import android.net.http.HttpException
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.R
import com.example.bookshelf.data.BookReviewRepository
import com.example.bookshelf.data.ListContentRepository

import com.example.bookshelf.data.ListNamesRepository
import com.example.bookshelf.data.NetworkListContentRepository
import com.example.bookshelf.model.BestSellerResponse
import com.example.bookshelf.model.BookReviewResponse
import com.example.bookshelf.model.ListContentResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.compose.runtime.*
import com.example.bookshelf.model.Book
import com.example.bookshelf.utils.defaultBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


sealed interface BookshelfUiState{
    data class ListNamesSuccess(val response: BestSellerResponse) : BookshelfUiState
    data class ListContentSuccess(val response: ListContentResponse) : BookshelfUiState
    data class BookReviewSuccess(val response: BookReviewResponse) : BookshelfUiState
    object Error : BookshelfUiState
    object  Loading: BookshelfUiState
    object Wait:BookshelfUiState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class BookshelfViewModel(
    private val listNamesRepository: ListNamesRepository,
    private val listContentRepository: ListContentRepository,
    private val bookReviewRepository: BookReviewRepository,
    ): ViewModel() {
    var bookshelfUiState : BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    private val _topBarTitle = mutableStateOf("New York Times Best Sellers")
    val topBarTitle: State<String> get() = _topBarTitle

    private val _listNameForRestoration = mutableStateOf("")
    val listNameForRestoration :State<String> get() = _listNameForRestoration

    private  val _isbnForRestoration = mutableStateOf("")
    val isbnForRestoration : State<String> get() = _isbnForRestoration

    var lastBook : Book = defaultBook

    init{
        CoroutineScope(Dispatchers.Main).launch {
            getListNames()
        }
    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getListNames(){
        viewModelScope.launch {
            bookshelfUiState = BookshelfUiState.Loading
            try {
                bookshelfUiState = BookshelfUiState.ListNamesSuccess(listNamesRepository.getListNames())
            } catch (e: IOException) {
                _listNameForRestoration.value = ""
                _isbnForRestoration.value = ""
                bookshelfUiState = BookshelfUiState.Error
            }catch (e : Exception){
                _listNameForRestoration.value = ""
                _isbnForRestoration.value = ""
                bookshelfUiState = BookshelfUiState.Wait
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getListContent(
        listNameEncoded:String
    ){
        viewModelScope.launch {
            bookshelfUiState = BookshelfUiState.Loading
            try {
                bookshelfUiState = BookshelfUiState.ListContentSuccess(listContentRepository.getListContent(listNameEncoded))

            }catch (e:IOException){
                _listNameForRestoration.value = listNameEncoded
                _isbnForRestoration.value = ""
                bookshelfUiState = BookshelfUiState.Error
            }catch (e:Exception){
                _listNameForRestoration.value = listNameEncoded
                _isbnForRestoration.value = ""
                bookshelfUiState = BookshelfUiState.Wait
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getBookReview(
        isbn:String?
    ){
        viewModelScope.launch {
            bookshelfUiState = BookshelfUiState.Loading
            try {
                val review = bookReviewRepository.getBookReviews(isbn)
                Log.d("BookshelfViewModel", "Book review fetched successfully: $review")
                review.results.forEach{
                    Log.d("Review Content", "Review Summary: ${it.summary}")
                }
                bookshelfUiState = BookshelfUiState.BookReviewSuccess(review)
            }catch (e:IOException){
                _listNameForRestoration.value =""
                if (isbn != null) {
                    _isbnForRestoration.value = isbn
                }
                bookshelfUiState = BookshelfUiState.Error
            }catch (e:Exception){
                _listNameForRestoration.value =""
                if (isbn != null) {
                    _isbnForRestoration.value = isbn
                }
                bookshelfUiState = BookshelfUiState.Wait
            }
        }
    }

    fun setTopBarTitle(name: String) {
        _topBarTitle.value = name
    }

    fun restorePreviousState(){
        Log.d("restorePreviousState", "Function Called - listNameEncoded: ${listNameForRestoration.value}, isbn: ${isbnForRestoration.value}")
        if (listNameForRestoration.value == "" && isbnForRestoration.value ==""){
            getListNames()
        }else if(listNameForRestoration.value != "" && isbnForRestoration.value == ""){
            getListContent(listNameForRestoration.value)
        }else if(listNameForRestoration.value == "" && isbnForRestoration.value != ""){
            getBookReview(isbnForRestoration.value)
        }else{
            getListNames()
        }
    }
    fun updateLastBook(book: Book){
        lastBook = book
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val listNamesRepository = application.container.listNamesRepository
                val listContentRepository = application.container.listContentRepository
                val bookReviewRepository = application.container.bookReviewRepository
                BookshelfViewModel(
                    listNamesRepository = listNamesRepository,
                    listContentRepository=listContentRepository,
                    bookReviewRepository = bookReviewRepository
                    )
            }
        }
    }
}