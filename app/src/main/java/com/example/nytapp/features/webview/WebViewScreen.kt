package com.example.nytapp.features.webview

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nytapp.ui.components.NytAppBar
import com.example.nytapp.ui.theme.NytAppTheme

@Composable
fun WebViewScreen(
    navController: NavHostController,
    viewModel: WebViewModel = hiltViewModel()
) {
    val url by viewModel.url.collectAsState()

    WebViewContent(
        url = url,
        onCloseClicked = {
            navController.popBackStack()
        })
}

@Composable
fun WebViewContent(
    url: String,
    onCloseClicked: () -> Unit
) {
    NytAppTheme {
        var isLoading by rememberSaveable { mutableStateOf(false) }

        Scaffold(
            topBar = {
                Column {
                    NytAppBar(
                        icon = Icons.Filled.Close,
                        title = "More Content",
                        tint = Color.White
                    ) {
                        onCloseClicked()
                    }
                    if (isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }) {
            AndroidView(factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                    }
                    loadUrl(url)
                }
            }, update = { webView ->
                webView.loadUrl(url)
            })
        }
    }
}

@Preview
@Composable
fun PreviewWebViewContent() {
    WebViewContent(url = "https://developer.android.com/") {}
}