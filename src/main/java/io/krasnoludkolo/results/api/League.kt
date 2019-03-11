package io.krasnoludkolo.results.api

import java.util.*

data class League(
    val uuid: UUID,
    val name: String,
    val rounds: Map<Int, List<Fixture>>
)

data class Fixture(
    val uuid: UUID,
    val host: String,
    val guest: String,
    val round: Int,
    val leagueUUID: UUID,
    val fixtureResult: FixtureResult
)

enum class FixtureResult {
    NOT_SET, HOST_WON, GUEST_WON, DRAW;

    fun fromResult(hostGoals: Int, guestGoals: Int): FixtureResult {
        return if (hostGoals > guestGoals) {
            FixtureResult.HOST_WON
        } else if (hostGoals < guestGoals) {
            FixtureResult.GUEST_WON
        } else {
            FixtureResult.DRAW
        }
    }
}
