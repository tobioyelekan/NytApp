package com.example.nytapp.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.nytapp.ui.theme.BorderGrey
import com.example.nytapp.ui.theme.Grey

@Composable
fun TopStoryItem(
    modifier: Modifier = Modifier,
    imgUrl: String,
    title: String,
    author: String,
    onClick: () -> Unit
) {

    Box(modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(Grey)
        .clickable { onClick() }
        .padding(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .size(70.dp)
                    .border(border = BorderStroke(1.dp, BorderGrey)),
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(data = imgUrl),
                contentDescription = null
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = author,
                    style = MaterialTheme.typography.body2.copy(
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun TopStoryItemPreview() {
    Box(Modifier.padding(20.dp)) {
        TopStoryItem(
            imgUrl = "img",
            title = "Title",
            author = "By",
        ) {}
    }
}