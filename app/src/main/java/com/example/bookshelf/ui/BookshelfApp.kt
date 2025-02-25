package com.example.bookshelf.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.screens.HomeScreen
import com.example.bookshelf.ui.viewmodel.BookshelfViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.bookshelf.model.BestSellerList

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun BookshelfApp(
    windowSize: WindowWidthSizeClass
){
    val bookshelfViewModel: BookshelfViewModel = viewModel(factory = BookshelfViewModel.Factory)
    val context = LocalContext.current
    Scaffold(
        topBar = {
            BookshelfAppBar(
                onBackButtonClick = {
                    bookshelfViewModel.getListNames()
                    bookshelfViewModel.setTopBarTitle(context.getString(R.string.mainTitle))
                                    },
                title = bookshelfViewModel.topBarTitle.value,
            )
        }
    ) { innerPadding ->
        HomeScreen(
            bookshelfUiState = bookshelfViewModel.bookshelfUiState,
            onListNameClick = { chosenList:BestSellerList ->
                bookshelfViewModel.getListContent(chosenList.listNameEncoded)
                chosenList.listName?.let { bookshelfViewModel.setTopBarTitle(it) }
                              },
            onBookClick = { book:Book ->
                Log.d("BookAdapter", "Clicked book ISBN: ${book.primary_isbn13}")
                bookshelfViewModel.updateLastBook(book)
                bookshelfViewModel.getBookReview(book.primary_isbn13)
                book.title?.let { bookshelfViewModel.setTopBarTitle(it) }
            },
            restorePreviousState = {bookshelfViewModel.restorePreviousState()},
            lastBook = bookshelfViewModel.lastBook,
            contentPadding = innerPadding,
            windowSize = windowSize
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfAppBar(
    onBackButtonClick: () -> Unit,
    title:String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight =FontWeight.ExtraBold
            )
        },
        navigationIcon = if (title != stringResource(R.string.mainTitle)) {
            {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        } else {
            { Box {} }
        },
        /*colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),*/
        modifier = modifier,
    )
}