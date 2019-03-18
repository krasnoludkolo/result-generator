package io.krasnoludkolo.results

import io.krasnoludkolo.results.api.GenerationErrors
import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.Tuple
import io.vavr.collection.List
import io.vavr.collection.Map
import io.vavr.control.Either
import java.util.*

class League private constructor(private val name: String, numberOfTeams: Int) {

    val id: UUID = UUID.randomUUID()
    private var rounds: List<List<Fixture>> = List.empty<List<Fixture>>()

    companion object {

        fun generate(name: String, numberOfTeams: Int): Either<GenerationErrors, League> {
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
        val teams = Teams.teams.take(numberOfTeams)

        var combinations = teams.combinations(2).map { Fixture(it[0],it[1]) }

        println(combinations)

        while (combinations.size()>0){
            rounds = rounds
                .append(combinations.take(numberOfTeams/2))
                .append(combinations.takeRight(numberOfTeams/2))
            combinations = combinations.drop(numberOfTeams/2).dropRight(numberOfTeams/2)
        }

        rounds = rounds.appendAll(rounds)
    }


        fun toDTO(): LeagueDTO {
            return LeagueDTO(id, name, rounds.map { it.map { it.toDTO() } })
        }
    }
