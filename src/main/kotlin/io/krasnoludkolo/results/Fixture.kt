package io.krasnoludkolo.results

import io.krasnoludkolo.results.api.FixtureDTO
import io.krasnoludkolo.results.api.FixtureResult
import java.util.*

data class Fixture(
    val uuid: UUID,
    val host: String,
    val guest: String,
    val round: Int,
    val leagueUUID: UUID,
    val fixtureResult: FixtureResult
) {

    constructor(host: String,guest: String) : this(UUID.randomUUID(),host,guest,0, UUID.randomUUID(),FixtureResult.NOT_SET)



    fun toDTO(): FixtureDTO = FixtureDTO(uuid,host,guest,round,leagueUUID,fixtureResult)

    override fun toString(): String {
        return "Fixture(host='$host', guest='$guest')"
    }
}
