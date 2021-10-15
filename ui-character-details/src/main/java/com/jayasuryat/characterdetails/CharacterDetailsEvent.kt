package com.jayasuryat.characterdetails

import androidx.navigation.Navigator

sealed interface CharacterDetailsEvent

class OpenEpisode(
    val episodeId: Long,
    val extras: Navigator.Extras,
) : CharacterDetailsEvent

object NavigateBack : CharacterDetailsEvent