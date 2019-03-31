package io.krasnoludkolo.results.domain

import io.krasnoludkolo.results.api.FixtureDTO
import io.krasnoludkolo.results.api.FixtureResult
import java.util.*

data class Fixture(
    val uuid: UUID,
    val host: String,
    val guest: String,
    val matchday: Int,
    val fixtureResult: FixtureResult
) {

    constructor(teams: TwoTeamsSet, matchday: Int) : this(UUID.randomUUID(),teams._1,teams._2,matchday,FixtureResult.NOT_SET)

    private fun containsTeam(name: String): Boolean = (host == name || guest == name)

    fun containsOneOfTeams(fixture: Fixture): Boolean = containsTeam(fixture.host) || containsTeam(fixture.guest)

    fun containsOneOfTeams(fixture: Pair<String,String>): Boolean = containsTeam(fixture.first) || containsTeam(fixture.second)


    fun toDTO(): FixtureDTO = FixtureDTO(uuid, host, guest, matchday, fixtureResult)

    override fun toString(): String {
        return "Fixture(host='$host', guest='$guest')"
    }
}
