package com.knightshrestha.bookmarks.mainscreen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import com.fresh.materiallinkpreview.parsing.OpenGraphMetaDataProvider
import com.knightshrestha.bookmarks.R
import com.knightshrestha.bookmarks.core.helpers.parseAndFormatTimestamp
import com.knightshrestha.bookmarks.graphql.LiveBookmarkListOrderedByNameSubscription
import java.net.URL

@Composable
fun ListItem(bookmark: LiveBookmarkListOrderedByNameSubscription.Bookmark) {
    OutlinedCard {
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
                    text = "⌛${parseAndFormatTimestamp(bookmark.time.toString())}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(text = bookmark.name, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}


@Composable
fun ListItemOld(bookmark: LiveBookmarkListOrderedByNameSubscription.Bookmark) {


    var metadata by remember {
        mutableStateOf(OpenGraphMetaData())
    }

    LaunchedEffect(Unit) {
        if (metadata.title.isEmpty()) {
            OpenGraphMetaDataProvider().startFetchingMetadataAsync(URL(bookmark.link)).let {
                when (it.isSuccess) {
                    true -> {
                        metadata =
                            it.getOrNull() ?: OpenGraphMetaData(title = "Metadata unavailable")
                    }

                    false -> {

                    }
                }

            }

        }
    }

    OutlinedCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = metadata.imageUrl, contentDescription = null,
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(8.dp)
            ) {
                Text(text = metadata.title)
                Text(text = "${metadata.description}")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            Text(text = "${bookmark.time}")
            Text(text = bookmark.name)
        }

    }

}
