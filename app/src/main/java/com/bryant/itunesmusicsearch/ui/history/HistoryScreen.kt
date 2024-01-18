import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bryant.itunesmusicsearch.data.history.History
import com.bryant.itunesmusicsearch.ui.search.MusicViewModel
import timber.log.Timber

@Composable
fun HistoryScreen(musicViewModel: MusicViewModel) {
    val histories by musicViewModel.historyList.observeAsState(emptyList())

    if (histories.isNotEmpty()) {
        LazyColumn {
            items(items = histories.reversed(), key = { it.keyword }) { item ->
                HistoryItem(item = item, onItemClick = {
                    musicViewModel.getSearchResult(item.keyword)
                }, onRemove = { keyword ->
                    musicViewModel.removeHistory(keyword)
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(item: History, onItemClick: (() -> Unit), onRemove: (String) -> Unit) {
    val state = rememberDismissState()
    if (state.isDismissed(direction = DismissDirection.EndToStart)) {
        onRemove.invoke(item.keyword)
    }
    SwipeToDismiss(state = state, background = {
        val color by animateColorAsState(
            when (state.dismissDirection) {
                DismissDirection.EndToStart -> Color.Red
                else -> Color.Transparent
            }, label = ""
        )
        val scale by animateFloatAsState(
            targetValue = if (state.targetValue == DismissValue.Default) 0.75f else 1f, label = ""
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(color = color)
                .padding(8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete",
                modifier = Modifier.scale(scale)
            )
        }
    }, dismissContent = {
        Card(
            onClick = {
                Timber.d("HistoryItem onclick, item: $item")
                onItemClick.invoke()
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
    }, modifier = Modifier.fillMaxWidth(), directions = setOf(DismissDirection.EndToStart))
}