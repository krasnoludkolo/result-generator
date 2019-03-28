package io.krasnoludkolo.results.domain

import io.krasnoludkolo.results.api.GenerationErrors
import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.collection.List
import io.vavr.control.Either
import java.util.*

class League private constructor(private val name: String, numberOfTeams: Int) {

    val id: UUID = UUID.randomUUID()
    private var matchdays: List<List<Fixture>> = List.empty<List<Fixture>>()

    companion object {

        fun generate(name: String, numberOfTeams: Int): Either<GenerationErrors, League> {
            if (numberOfTeams % 2 != 0) {
                return Either.left(GenerationErrors.ODD_TEAM_NUMBER)
            }
            return Either.right(League(name, numberOfTeams))
        }
    }

    init {
        generateLeagueFor(numberOfTeams)
    }

    private fun generateLeagueFor(numberOfTeams: Int) {
        fun generateRound(firstIndex: Int, combinations: List<Fixture>): List<List<Fixture>> {
            fun generateFrom(matchdayIndex: Int, combinations: List<Fixture>): List<List<Fixture>> {
                return when {
                    combinations.isEmpty -> List.empty<List<Fixture>>()
                    else -> {
                        val matchday = generateMatchday(combinations, numberOfTeams / 2)
                        return List.of(matchday.map { it.copy(matchday = matchdayIndex) })
                            .appendAll(generateFrom(matchdayIndex.plus(1),combinations.removeAll(matchday)))
                    }
                }
            }
            return generateFrom(firstIndex,combinations)
        }

        val teams = Teams.teams.take(numberOfTeams)
        val combinations = teams.combinations(2).map { Fixture(it[0], it[1]) }
        matchdays = generateRound(1, combinations)
            .appendAll(
                generateRound(numberOfTeams, combinations)
            )
    }

    private fun generateMatchday(fixtures: List<Fixture>, fixturesInRound: Int): List<Fixture> {
        fun generateRound(alreadyTaken: List<Fixture>, availableFixtures: List<Fixture>): List<Fixture> {
            if (alreadyTaken.size() == fixturesInRound) {
                return alreadyTaken
            } else if (availableFixtures.size() == 1) {
                if (alreadyTaken.size() + 1 == fixturesInRound) {
                    return alreadyTaken.append(availableFixtures.head())
                }
                return List.empty()
            } else if (availableFixtures.size() == 0) {
                return List.empty()
            }
            return availableFixtures
                .find { fix: Fixture ->
                    !generateRound(
                        alreadyTaken.append(fix),
                        availableFixtures.removeAll { f -> f.containsOneOfTeams(fix) }).isEmpty
                }
                .map {
                    generateRound(
                        alreadyTaken.append(it),
                        availableFixtures.removeAll { f -> f.containsOneOfTeams(it) })
                }
                .getOrElse { List.empty<Fixture>() }

        }

        return generateRound(List.empty(), fixtures)

    }

    fun toDTO(): LeagueDTO {
        return LeagueDTO(id, name, matchdays.map { it.map { it.toDTO() } })
    }
}
