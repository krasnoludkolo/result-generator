package io.krasnoludkolo.results.domain

import io.krasnoludkolo.results.api.GenerationErrors
import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.collection.List
import io.vavr.control.Either
import java.util.*

class League private constructor(private val name: String, numberOfTeams: Int) {

    val id: UUID = UUID.randomUUID()
    private val matchDays: List<Matchday>

    companion object {

        fun generate(name: String, numberOfTeams: Int): Either<GenerationErrors, League> {
            if (numberOfTeams % 2 != 0) {
                return Either.left(GenerationErrors.ODD_TEAM_NUMBER)
            }
            return Either.right(League(name, numberOfTeams))
        }
    }

    init {
        matchDays = generateLeagueFor(numberOfTeams)
    }

    private fun generateLeagueFor(numberOfTeams: Int): List<Matchday> {
        fun generateRound(firstIndex: Int, combinations: List<TwoTeamsSet>): List<Matchday> {
            fun generateFrom(matchdayIndex: Int, possibleFixtures: List<TwoTeamsSet>): List<Matchday> {
                return when {
                    possibleFixtures.isEmpty -> List.empty<Matchday>()
                    else -> {
                        val matchday = Matchday.generate(numberOfTeams, matchdayIndex, possibleFixtures)
                        return List.of(matchday)
                            .appendAll(
                                generateFrom(
                                    matchdayIndex.plus(1),
                                    possibleFixtures.removeAll(matchday.getUsedCombinations())
                                )
                            )
                    }
                }
            }
            return generateFrom(firstIndex, combinations)
        }

        val teams = Teams.teams.take(numberOfTeams)
        val combinations = teams.combinations(2).map { TwoTeamsSet(it[0], it[1]) }
        return generateRound(1, combinations)
            .appendAll(
                generateRound(numberOfTeams, combinations)
            )
    }

    fun toDTO(): LeagueDTO {
        return LeagueDTO(id, name, matchDays.map { it.toFixtureDTOList() })
    }
}
