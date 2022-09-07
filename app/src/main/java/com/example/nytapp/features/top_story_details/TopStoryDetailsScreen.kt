package com.example.nytapp.features.top_story_details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.nytapp.data.model.TopStoryData
import com.example.nytapp.helpers.Route
import com.example.nytapp.ui.components.NytAppBar
import com.example.nytapp.ui.theme.NytAppTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun TopStoryDetailsScreen(
    navController: NavHostController,
    viewModel: TopStoryDetailViewModel = hiltViewModel()
) {

    val viewState by viewModel.viewState.collectAsState()

    TopStoryDetailsContent(
        viewState = viewState,
        onBackPressed = { navController.popBackStack() },
        onSeeMoreClicked = { url ->
            url?.let {
                navController.navigate(
                    "${Route.storyMoreContent}/${
                        URLEncoder.encode(
                            it,
                            StandardCharsets.UTF_8.toString()
                        )
                    }"
                )
            }
        }
    )
}

@Composable
fun TopStoryDetailsContent(
    viewState: TopStoryDetailViewModel.ViewState,
    onBackPressed: () -> Unit,
    onSeeMoreClicked: (String?) -> Unit
) {
    NytAppTheme {
        Scaffold(
            topBar = {
                Column {
                    NytAppBar(
                        title = "Story Details",
                        tint = Color.White
                    ) {
                        onBackPressed()
                    }
                    if (viewState.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop,
                    painter = rememberImagePainter(data = viewState.storyDetails?.imgUrl),
                    contentDescription = null
                )

                Column(Modifier.padding(20.dp)) {
                    Text(
                        text = viewState.storyDetails?.title ?: "",
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = viewState.storyDetails?.author ?: "",
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = viewState.storyDetails?.summary ?: "",
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = { onSeeMoreClicked(viewState.storyDetails?.moreContentUrl) }) {
                        Text("Click here to see more")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTopStoryDetail() {
    TopStoryDetailsContent(
        viewState = TopStoryDetailViewModel.ViewState(
            storyDetails = TopStoryData(
                title = "Story title",
                author = "Author",
                summary = "Summary",
                imgUrl = "",
                moreContentUrl = ""
            )
        ),
        onBackPressed = { },
        onSeeMoreClicked = {})
}