package io.krasnoludkolo.results.api

import io.krasnoludkolo.results.temp.api.NewTempFixtureDTO
import io.vavr.collection.List
import java.util.*

data class LeagueDTO(
    val uuid: UUID,
    val name: String,
    val rounds: List<List<FixtureDTO>>
) {
    fun insertFixture(request: NewTempFixtureDTO): LeagueDTO {
        val rounds =
            rounds.zipWithIndex().map { if (it._2 + 1 == request.round) it._1.append(FixtureDTO(request)) else it._1() }
        return LeagueDTO(uuid, name, rounds)
    }
}


