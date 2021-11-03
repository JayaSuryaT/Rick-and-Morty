package com.jayasuryat.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jayasuryat.event.EventListener
import com.jayasuryat.event.noOpEventListener
import com.jayasuryat.home.R
import com.jayasuryat.home.event.HomeEvent


@Composable
fun Home(
    eventListener: EventListener<HomeEvent>,
) {

    Column(
        modifier = Modifier
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            )
    ) {

        HomeItem(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            title = "Characters",
            image = R.drawable.img_rick,
            onClick = { eventListener.onEvent(HomeEvent.OpenCharacters) },
        )

        HomeItem(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            title = "Episodes",
            image = R.drawable.img_episode,
            onClick = { eventListener.onEvent(HomeEvent.OpenEpisodes) },
        )

        HomeItem(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            title = "Locations",
            image = R.drawable.img_location,
            onClick = { eventListener.onEvent(HomeEvent.OpenLocations) },
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Home(noOpEventListener())
}