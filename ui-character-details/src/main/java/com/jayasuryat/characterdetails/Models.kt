package com.jayasuryat.characterdetails


internal sealed interface CharacterDetailsModel

internal interface CharacterDetailsDto : CharacterDetailsModel
internal interface CharacterDetailsEntity : CharacterDetailsModel
internal interface CharacterDetailsDomainModel : CharacterDetailsModel
