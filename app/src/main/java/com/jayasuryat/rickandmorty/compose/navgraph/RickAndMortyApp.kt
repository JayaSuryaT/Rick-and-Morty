package com.jayasuryat.rickandmorty.compose.navgraph

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jayasuryat.characterdetails.presentation.character.CharacterDetailsViewModel
import com.jayasuryat.characterdetails.presentation.composable.character.CharacterDetailsScreen
import com.jayasuryat.characterdetails.presentation.composable.episode.CharacterEpisodesScreen
import com.jayasuryat.characterdetails.presentation.episodes.CharacterEpisodesViewModel
import com.jayasuryat.characterdetails.presentation.event.CharacterDetailsEvent
import com.jayasuryat.characterdetails.presentation.event.CharacterEpisodesEvent
import com.jayasuryat.characterlist.presentation.CharacterListViewModel
import com.jayasuryat.characterlist.presentation.composable.CharacterListScreen
import com.jayasuryat.characterlist.presentation.event.CharacterListEvent
import com.jayasuryat.episodedetails.presentation.EpisodeDetailsViewModel
import com.jayasuryat.episodedetails.presentation.composable.EpisodeDetailsScreen
import com.jayasuryat.episodedetails.presentation.event.EpisodeDetailsEvent
import com.jayasuryat.episodelist.presentation.EpisodesListViewModel
import com.jayasuryat.episodelist.presentation.composable.EpisodeListScreen
import com.jayasuryat.episodelist.presentation.event.EpisodeListEvent
import com.jayasuryat.event.Event
import com.jayasuryat.home.composable.Home
import com.jayasuryat.home.event.HomeEvent
import com.jayasuryat.locationlist.presentation.LocationListViewModel
import com.jayasuryat.locationlist.presentation.composables.LocationListScreen
import com.jayasuryat.locationlist.presentation.event.LocationListEvent

@Composable
fun RickAndMortyApp() {

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {

        RickAndMortyNavHost()
    }
}

@Composable
private fun RickAndMortyNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.getRoute(),
    ) {

        composable(
            route = Screen.Home.getRoute(),
        ) {
            Home { event ->
                when (event) {
                    HomeEvent.OpenCharacters -> navController.navigate(Screen.CharacterList.getRoute())
                    HomeEvent.OpenEpisodes -> navController.navigate(Screen.EpisodeList.getRoute())
                    HomeEvent.OpenLocations -> navController.navigate(Screen.LocationsList.getRoute())
                }
            }
        }

        composable(
            route = Screen.CharacterList.getRoute(),
        ) {

            val viewModel: CharacterListViewModel = hiltViewModel()

            CharacterListScreen(
                viewModel = viewModel,
                eventListener = { event ->
                    when (event) {
                        is CharacterListEvent.OnBackClicked -> navController.popBackStack()
                        is CharacterListEvent.OpenCharacter -> {
                            val route = Screen.CharacterDetails.getNavigableRoute(event.characterId)
                            navController.navigate(route)
                        }
                    }
                },
            )
        }

        composable(
            route = Screen.CharacterDetails.getRoute(),
            arguments = listOf(
                navArgument(Screen.CharacterDetails.CHARACTER_ID) { type = NavType.LongType }
            )
        ) {

            val characterId = it.arguments
                ?.getLong(Screen.CharacterDetails.CHARACTER_ID)
                ?: throw IllegalArgumentException("Character id cannot be null")

            val viewModel: CharacterDetailsViewModel = hiltViewModel()
            viewModel.getCharacterDetails(characterId)

            CharacterDetailsScreen(
                viewModel = viewModel,
            ) { event ->
                when (event) {
                    is CharacterDetailsEvent.OnBackClicked -> navController.popBackStack()
                    is CharacterDetailsEvent.OpenCharacterEpisodes -> {
                        val route = Screen.CharacterEpisodes.getNavigableRoute(event.characterId)
                        navController.navigate(route)
                    }
                    is CharacterDetailsEvent.OpenLocation -> pendingNavigationImpl(event)
                }
            }
        }

        composable(
            route = Screen.CharacterEpisodes.getRoute(),
            arguments = listOf(
                navArgument(Screen.CharacterDetails.CHARACTER_ID) { type = NavType.LongType }
            )
        ) {

            val characterId = it.arguments
                ?.getLong(Screen.CharacterEpisodes.CHARACTER_ID)
                ?: throw IllegalArgumentException("Character id cannot be null")

            val viewModel: CharacterEpisodesViewModel = hiltViewModel()
            viewModel.loadEpisodes(characterId)

            CharacterEpisodesScreen(
                viewModel = viewModel,
            ) { event ->
                when (event) {
                    is CharacterEpisodesEvent.OnBackClicked -> navController.popBackStack()
                    is CharacterEpisodesEvent.OpenEpisode -> {
                        val route = Screen.EpisodeDetails.getNavigableRoute(event.episodeId)
                        navController.navigate(route)
                    }
                }
            }
        }

        composable(
            route = Screen.EpisodeList.getRoute(),
        ) {

            val viewModel: EpisodesListViewModel = hiltViewModel()

            EpisodeListScreen(
                viewModel = viewModel,
                eventListener = { event ->
                    when (event) {
                        is EpisodeListEvent.OnBackClicked -> navController.popBackStack()
                        is EpisodeListEvent.OpenEpisode -> {
                            val route = Screen.EpisodeDetails.getNavigableRoute(event.episodeId)
                            navController.navigate(route)
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.EpisodeDetails.getRoute(),
            arguments = listOf(
                navArgument(Screen.EpisodeDetails.EPISODE_ID) { type = NavType.LongType }
            )
        ) {

            val episodeId = it.arguments
                ?.getLong(Screen.EpisodeDetails.EPISODE_ID)
                ?: throw IllegalArgumentException("Episode id cannot be null")

            val viewModel: EpisodeDetailsViewModel = hiltViewModel()
            viewModel.loadEpisodeDetails(episodeId = episodeId)

            EpisodeDetailsScreen(
                viewModel = viewModel,
                eventListener = { event ->
                    when (event) {
                        is EpisodeDetailsEvent.OnBackClicked -> navController.popBackStack()
                        is EpisodeDetailsEvent.OpenCharacter -> {
                            val route = Screen.CharacterDetails.getNavigableRoute(event.characterId)
                            navController.navigate(route)
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.LocationsList.getRoute(),
        ) {

            val viewModel: LocationListViewModel = hiltViewModel()

            LocationListScreen(
                viewModel = viewModel,
                eventListener = { event ->
                    when (event) {
                        is LocationListEvent.OnBackClicked -> navController.popBackStack()
                        is LocationListEvent.OpenLocation -> pendingNavigationImpl(event)
                    }
                }
            )
        }
    }
}

private fun pendingNavigationImpl(event: Event) {
    Log.e("RickAndMorty", "Navigation impl pending for event : ${event::class.java}")
}