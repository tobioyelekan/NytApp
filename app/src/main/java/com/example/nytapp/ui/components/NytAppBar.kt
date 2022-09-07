package com.example.nytapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun NytAppBar(
    icon: ImageVector? = Icons.Filled.ArrowBack,
    tint: Color = Color.Black,
    title: String = "",
    backgroundColor: Color = MaterialTheme.colors.primary,
    onIconClicked: () -> Unit = { }
) {
    TopAppBar(
        navigationIcon = if (icon == null) {
            null
        } else {
            {
                Icon(
                    tint = tint,
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            onIconClicked()
                        }
                        .padding(horizontal = 12.dp)
                )
            }
        },
        title = {
            Text(text = title)
        },
        backgroundColor = backgroundColor,
        elevation = 0.dp
    )
}