package io.krasnoludkolo.results

import io.krasnoludkolo.results.api.GenerationErrors
import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.collection.List
import io.vavr.control.Either
import java.util.*

class League private constructor(private val name: String, numberOfTeams: Int) {

    val id: UUID = UUID.randomUUID()
    private val rounds: List<List<Fixture>> = List.empty<List<Fixture>>()

    companion object {
        fun generate(name: String, numberOfTeams: Int): Either<GenerationErrors,League> {
            if (numberOfTeams % 2 != 0) {
                return Either.left(GenerationErrors.ODD_TEAM_NUMBER)
            }
            return Either.right(League(name, numberOfTeams))
        }
    }

    init {
        generateFor(numberOfTeams)
    }

    private fun generateFor(numberOfTeams: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun toDTO(): LeagueDTO {
        return LeagueDTO(id, name, rounds.map { it.map { it.toDTO() } })
    }
}