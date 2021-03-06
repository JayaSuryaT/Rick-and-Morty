package com.jayasuryat.episodelist.presentation.composable

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.jayasuryat.episodelist.R
import com.jayasuryat.episodelist.presentation.EpisodeListData
import com.jayasuryat.sharedcomposable.composable.TopBar
import com.jayasuryat.themepreview.PreviewTheme


@Composable
internal fun EpisodeListBody(
    episodes: List<EpisodeListData>,
    onBackClicked: () -> Unit,
    onEpisodeClicked: (episode: EpisodeListData.Episode) -> Unit,
    onSeasonClicked: (episode: EpisodeListData.Season) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .padding(
                top = 16.dp,
                start = 24.dp,
                end = 24.dp,
            ),
        verticalArrangement = Arrangement.Top,
    ) {

        TopBar(
            title = stringResource(R.string.episodes),
            icon = Icons.Filled.KeyboardArrowLeft,
            onIconClicked = onBackClicked,
        )

        EpisodeList(
            episodes = episodes,
            onEpisodeClicked = onEpisodeClicked,
            onSeasonClicked = onSeasonClicked,
        )
    }
}

@Composable
private fun EpisodeList(
    episodes: List<EpisodeListData>,
    modifier: Modifier = Modifier,
    onEpisodeClicked: (episode: EpisodeListData.Episode) -> Unit,
    onSeasonClicked: (episode: EpisodeListData.Season) -> Unit,
) {

    LazyColumn(
        modifier = modifier,
    ) {

        items(
            items = episodes,
            key = { episode -> episode.id }
        ) { item ->

            when (item) {
                is EpisodeListData.Episode -> {
                    EpisodeListItemEpisode(
                        episode = item,
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp
                            ),
                        onEpisodeClicked = onEpisodeClicked,
                    )
                }
                is EpisodeListData.Season -> {
                    EpisodeListItemEpisodeSeason(
                        season = item,
                        modifier = Modifier
                            .padding(top = 16.dp),
                        onSeasonClicked = onSeasonClicked,
                    )
                }
            }
        }
    }
}

@Preview(name = "Episode list screen [light]")
@Preview(
    name = "Episode list screen [dark]",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun Preview(
    @PreviewParameter(EpisodesParameterProvider::class) episodes: List<EpisodeListData>,
) {
    PreviewTheme {
        EpisodeListBody(
            episodes = episodes,
            onBackClicked = {},
            onEpisodeClicked = {},
            onSeasonClicked = {},
        )
    }
}


