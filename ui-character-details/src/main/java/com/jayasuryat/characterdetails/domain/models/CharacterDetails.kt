package com.jayasuryat.characterdetails.domain.models

import com.jayasuryat.characterdetails.CharacterDetailsDomainModel


data class CharacterDetails(
    val id: String,
    val name: String,
    val image: String,
    val status: Status,
    val species: Species,
    val type: String?,
    val gender: Gender,
    val location: Location?,
    val origin: Location?,
) : CharacterDetailsDomainModel {

    // region : Enums
    enum class Gender(val value: String) {
        Female("Female"),
        Male("Male"),
        Genderless("Genderless"),
        Unknown("unknown");

        companion object {

            fun enumFrom(value: String): Gender {
                return values().firstOrNull { it.name.equals(value, false) } ?: Unknown
            }
        }
    }

    enum class Species(val value: String) {
        Alien("Alien"),
        Human("Human"),
        Humanoid("Humanoid"),
        Other("Other");

        companion object {

            fun enumFrom(value: String): Species {
                return values().firstOrNull { it.name.equals(value, false) } ?: Other
            }
        }
    }

    enum class Status(val value: String) {
        Alive("Alive"),
        Dead("Dead"),
        Unknown("unknown");

        companion object {

            fun enumFrom(value: String): Status {
                return values().firstOrNull { it.name.equals(value, false) } ?: Unknown
            }
        }
    }

    // endregion
}

data class Location(
    val id: String?,
    val name: String?,
    val type: String?,
    val dimension: String?,
)
