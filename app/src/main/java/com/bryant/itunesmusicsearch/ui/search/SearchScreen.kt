import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bryant.itunesmusicsearch.model.ResultsItem
import com.bryant.itunesmusicsearch.ui.player.Player
import com.bryant.itunesmusicsearch.ui.search.MusicViewModel
import com.bryant.itunesmusicsearch.ui.search.SearchFragmentDirections
import com.bryant.itunesmusicsearch.utils.isNetworkAvailable
import com.bryant.itunesmusicsearch.utils.showToast
import timber.log.Timber

@Composable
fun SearchResultScreen(musicViewModel: MusicViewModel, navController: NavController) {
    val searchResult by musicViewModel.searchResult.observeAsState(null)
    val context = LocalContext.current.applicationContext
    LazyColumn {
        items(searchResult ?: emptyList()) { item ->
            SearchItem(item = item, onItemClick = {
                if (isNetworkAvailable(offlineAction = {
                        showToast(context, "offline")
                        Timber.e("offline")
                    })) {
                    val player =
                        Player(getImageUrl(item), item.trackName, item.artistName, item.previewUrl)
                    navController.navigate(SearchFragmentDirections.goPlayer(player))
                }
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItem(item: ResultsItem, onItemClick: (() -> Unit)? = null) {
    Card(
        onClick = {
            Timber.d("SearchInfoItem onclick, resultsItem: $item")
            onItemClick?.invoke()
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                model = getImageUrl(item),
                contentDescription = item.trackName,
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = item.artistName, style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.trackName, style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }
}

fun getImageUrl(infoItem: ResultsItem): String {
    return when {
        infoItem.artworkUrl100.isNotEmpty() -> infoItem.artworkUrl100
        infoItem.artworkUrl60.isNotEmpty() -> infoItem.artworkUrl60
        infoItem.artworkUrl30.isNotEmpty() -> infoItem.artworkUrl30
        else -> ""
    }
}
