package com.example.nytapp.features.all_top_stories

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nytapp.data.model.TopStoryData
import com.example.nytapp.helpers.Route
import com.example.nytapp.ui.components.NytAppBar
import com.example.nytapp.ui.components.TopStoryItem
import com.example.nytapp.ui.theme.NytAppTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collect

@Composable
fun AllStoriesScreen(
    navController: NavHostController,
    viewModel: AllStoriesViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventActions.collect { action ->
            when (action) {
                is AllStoriesViewModel.EventActions.ShowMessage -> {
                    Toast.makeText(context, action.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    AllStoriesContent(
        viewState = viewState,
        onRefresh = {
            viewModel.refresh()
        },
        onStoryItemClicked = {
            navController.navigate("${Route.storyDetails}/$it")
        }
    )
}

@Composable
fun AllStoriesContent(
    viewState: AllStoriesViewModel.ViewState,
    onRefresh: () -> Unit,
    onStoryItemClicked: (id: String) -> Unit
) {
    NytAppTheme {
        Scaffold(
            topBar = {
                Column {
                    NytAppBar(title = "Top Stories", icon = null)
                    if (viewState.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = it)
            ) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = false),
                    onRefresh = { onRefresh() }) {
                    LazyColumn(
                        state = rememberLazyListState(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        items(viewState.topStories) { data ->
                            TopStoryItem(
                                imgUrl = data.imgUrl,
                                title = data.title,
                                author = data.author,
                            ) {
                                onStoryItemClicked(data.id.toString())
                            }
                        }
                    }
                }

                if (viewState.topStories.isEmpty()) {
                    Text(
                        text = "No stories",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAllStoriesContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        AllStoriesContent(
            viewState = AllStoriesViewModel.ViewState(
                topStories = listOf(
                    TopStoryData(
                        title = "title",
                        author = "By someone",
                        summary = "",
                        imgUrl = "https://static01.nyt.com/images/2022/09/03/obituaries/02Ehrenreich1-sub/02Ehrenreich1-sub-superJumbo.jpg",
                        moreContentUrl = ""
                    ),
                    TopStoryData(
                        title = "title",
                        author = "By someone",
                        summary = "",
                        imgUrl = "https://static01.nyt.com/images/2022/09/03/obituaries/02Ehrenreich1-sub/02Ehrenreich1-sub-superJumbo.jpg",
                        moreContentUrl = ""
                    )
                )
            ),
            onRefresh = {}
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAllStoriesContentEmptyState() {
    Box(modifier = Modifier.fillMaxSize()) {
        AllStoriesContent(
            viewState = AllStoriesViewModel.ViewState(),
            onRefresh = {}
        ) {}
    }
}