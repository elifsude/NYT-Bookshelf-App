package com.example.bookshelf.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.model.BestSellerList
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.BookReview
import com.example.bookshelf.ui.viewmodel.BookshelfUiState
import kotlinx.coroutines.delay
import java.time.format.TextStyle



@Composable
fun HomeScreen(
    windowSize: WindowWidthSizeClass,
    bookshelfUiState: BookshelfUiState,
    onListNameClick: (BestSellerList) -> Unit,
    onBookClick: (Book) -> Unit,
    lastBook: Book,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    restorePreviousState: () -> Unit,
    modifier: Modifier = Modifier,
){
    when(bookshelfUiState){
        is BookshelfUiState.Loading -> LoadingScreen()
        is BookshelfUiState.ListNamesSuccess -> BestSellerLists(
            bestSellerListList = bookshelfUiState.response.results,
            onItemClick = onListNameClick,
            contentPadding = contentPadding
            )
        is BookshelfUiState.BookReviewSuccess -> {
            if(windowSize == WindowWidthSizeClass.Expanded){
                DetailScreenExpanded(
                    lastBook,
                    bookshelfUiState.response.results,
                    contentPadding = contentPadding,)
            }else{
                DetailScreen(
                    lastBook,
                    bookshelfUiState.response.results,
                    contentPadding = contentPadding,)
            }
        }
        is BookshelfUiState.Error -> ErrorScreen(retryAction = restorePreviousState)
        is BookshelfUiState.ListContentSuccess -> ListContentScreen(
            books =  bookshelfUiState.response.results.books,
            onItemClick = onBookClick,
            contentPadding = contentPadding
        )
        is BookshelfUiState.Wait -> WaitingScreen(restorePreviousState)
    }
}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier.fillMaxSize()) {
    Image(
        modifier = modifier.fillMaxSize(),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}
@Composable
fun ErrorScreen(
    retryAction:()->Unit,
    modifier:Modifier = Modifier.fillMaxSize(),
    ){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction){
            Text(stringResource(R.string.retry))
        }
    }
}
@Composable
fun WaitingScreen(
    retryAction: () -> Unit
) {
    var count by remember { mutableStateOf(60) }
    LaunchedEffect(Unit) {
        for (i in 60 downTo 1) {
            delay(1000)
            count = i - 1
        }
//        if (count == 0){
//            restorePreviousState()
//        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$count",
                fontSize = 50.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.wait_screen_text)
            )
            if(count == 0){
                Button(onClick = retryAction){
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}

@Composable
fun BestSellerLists(
    bestSellerListList: List<BestSellerList>,
    onItemClick: (BestSellerList) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    val sortedList = bestSellerListList.sortedBy { it.displayName }
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        ) {
        items(sortedList){ it ->
            BestSellerListsItem(
                responseText = it.displayName,
                onItemClick = {onItemClick(it)}
            )
        }
    }
}

@Composable
fun BestSellerListsItem(
    responseText: String,
    modifier: Modifier = Modifier.padding(2.dp),
    onItemClick:() -> Unit
){
    Card(
        modifier = modifier,
        onClick = {onItemClick()}
        ){
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(
                text = responseText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

        }
    }
}

@Composable
fun ListContentScreen(
    books : List<Book>,
    onItemClick: (Book) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding : PaddingValues = PaddingValues(0.dp)
){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.padding(horizontal = 4.dp),
        contentPadding = contentPadding
    ) {
        items(books){ it ->
            BookItem(
                imgSrc = it.book_image,
                title = it.title,
                onItemClick = {onItemClick(it)
                    Log.d("BookshelfViewModel", "Fetching review for ISBN: ${it.primary_isbn13}")
                }
            )
        }
    }
}

@Composable
fun BookItem(
    imgSrc : String? ,
    title: String?,
    modifier: Modifier = Modifier.padding(2.dp),
    onItemClick:() -> Unit
){
    Card(
        modifier = modifier.clip(RectangleShape),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {onItemClick()}
    ){
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(imgSrc)
                .build(),
            error = painterResource(R.drawable.ic_connection_error),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = modifier.aspectRatio(0.7f).clip(RectangleShape)
        )
    }
}

@Composable
fun DetailScreenExpanded(
    book: Book,
    reviews: List<BookReview>,
    modifier: Modifier=Modifier.fillMaxSize(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    val context = LocalContext.current
    Row(modifier = modifier.padding(16.dp),){
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.book_image)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_connection_error),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(2f)
                .aspectRatio(0.6f)
                .shadow(6.dp)
                .padding(top = contentPadding.calculateTopPadding())
        )
        Spacer(Modifier.width(16.dp))
        LazyColumn(
            modifier = Modifier.weight(3f),
            contentPadding = contentPadding,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                book.title?.let {
                    Text(
                        text = it,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                }

                book.description?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                    )
                }

                Text(
                    text = "Price: $${book.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Color.Green
                )

                Text(
                    text = "Buy here: ${book.amazon_product_url}",
                    fontSize = 14.sp,
                    color = Color.Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(book.amazon_product_url))
                            startActivity(context, intent, null)
                        }
                        .padding(4.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Reviews:",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

        if (reviews.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_reviews),
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(reviews) { review ->
                DetailScreenItem(
                    review,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                    //.padding(12.dp)
                )
            }
        }
        }
    }
}

@Composable
fun DetailScreen(
    book: Book,
    reviews: List<BookReview>,
    modifier: Modifier = Modifier.fillMaxSize(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = contentPadding,
    ) {
        item {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.book_image)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_connection_error),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.6f)
                    .shadow(6.dp)
            )

            book.title?.let {
                Text(
                    text = it,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }

            book.description?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
            }

            Text(
                text = "Price: $${book.price}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Green
            )

            Text(
                text = "Buy here: ${book.amazon_product_url}",
                fontSize = 14.sp,
                color = Color.Blue,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(book.amazon_product_url))
                        startActivity(context, intent, null)
                    }
                    .padding(4.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Reviews:",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        if (reviews.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_reviews),
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(reviews) { review ->
                DetailScreenItem(
                    review,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        //.padding(12.dp)
                )
            }
        }
    }
}


@Composable
fun DetailScreenItem(
    review: BookReview,
    modifier: Modifier = Modifier,
){
    val context = LocalContext.current
    Column (modifier = modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = review.reviewAuthor,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        if(review.summary != ""){
            //Spacer(modifier.height(2.dp))
            Text(
                text = review.summary,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
        Text(
            text = review.url,
            fontSize = 14.sp,
            color = Color.Blue,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(review.url))
                    startActivity(context, intent, null)
                }
                .padding(4.dp)
        )
    }
}
