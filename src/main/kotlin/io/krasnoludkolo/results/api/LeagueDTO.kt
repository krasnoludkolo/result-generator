package io.krasnoludkolo.results.api

import java.util.*
import io.vavr.collection.List

data class LeagueDTO(
    val uuid: UUID,
    val name: String,
    val rounds: List<List<FixtureDTO>>
)


