package io.krasnoludkolo.results.api

import java.util.*

data class FixtureDTO(
    val uuid: UUID,
    val host: String,
    val guest: String,
    val round: Int,
    val leagueUUID: UUID,
    val fixtureResult: FixtureResult
)
