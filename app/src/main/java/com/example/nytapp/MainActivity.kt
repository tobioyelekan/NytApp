package com.example.nytapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nytapp.features.all_top_stories.AllStoriesScreen
import com.example.nytapp.features.top_story_details.TopStoryDetailsScreen
import com.example.nytapp.features.webview.WebViewScreen
import com.example.nytapp.helpers.NavArgs
import com.example.nytapp.helpers.Route
import com.example.nytapp.ui.theme.NytAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NytAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Route.allStories) {
                    composable(Route.allStories) {
                        AllStoriesScreen(navController = navController)
                    }

                    composable(
                        "${Route.storyDetails}/{${NavArgs.storyId}}",
                    ) {
                        TopStoryDetailsScreen(navController = navController)
                    }

                    composable("${Route.storyMoreContent}/{${NavArgs.storyUrl}}") {
                        WebViewScreen(navController = navController)
                    }
                }
            }
        }
    }
}