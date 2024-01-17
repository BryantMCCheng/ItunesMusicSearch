import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bryant.itunesmusicsearch.data.history.History
import com.bryant.itunesmusicsearch.ui.search.MusicViewModel
import timber.log.Timber

@Composable
fun HistoryScreen(musicViewModel: MusicViewModel) {
    val histories by musicViewModel.historyList.observeAsState(null)

    if (!histories.isNullOrEmpty()) {
        LazyColumn {
            items(
                (histories ?: emptyList()).asReversed()
            ) { item ->
                HistoryItem(item = item, onItemClick = {
                    musicViewModel.getSearchResult(item.keyword)
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(item: History, onItemClick: (() -> Unit)? = null) {
    Card(
        onClick = {
            Timber.d("HistoryItem onclick, item: $item")
            onItemClick?.invoke()
        }, modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.keyword, style = MaterialTheme.typography.titleLarge
            )
        }
    }
}