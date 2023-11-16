package com.knightshrestha.bookmarks.mainscreen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.knightshrestha.bookmarks.R
import com.knightshrestha.bookmarks.core.helpers.parseAndFormatTimestamp
import com.knightshrestha.bookmarks.mainscreen.models.Bookmark

@Composable
fun ListItem(bookmark: Bookmark) {
    OutlinedCard(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.rich_folder),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "⌛${parseAndFormatTimestamp(bookmark.time)}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(text = bookmark.name, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
