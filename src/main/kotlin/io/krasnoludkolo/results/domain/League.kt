package io.krasnoludkolo.results.domain

import io.krasnoludkolo.results.api.GenerationErrors
import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.collection.List
import io.vavr.control.Either
import java.util.*

class League private constructor(private val name: String, numberOfTeams: Int) {

    private val id: UUID = UUID.randomUUID()
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

        var combinations = teams.combinations(2).map { Fixture(it[0], it[1]) }

        var i = 1
        while (!combinations.isEmpty) {
            val round = generateRound(combinations, numberOfTeams / 2)
            combinations = combinations.removeAll(round)
            round.map { it.round == i }
            rounds = rounds.append(round)
            i++
        }

        rounds = rounds.appendAll(rounds)
    }

    private fun generateRound(fixtures: List<Fixture>, fixturesInRound: Int): List<Fixture> {
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
                        !generateRound(alreadyTaken.append(fix), availableFixtures.removeAll { f -> f.containsOneOfTeams(fix) }).isEmpty
                    }
                    .map {
                        generateRound(alreadyTaken.append(it), availableFixtures.removeAll { f -> f.containsOneOfTeams(it) })
                    }
                    .getOrElse { List.empty<Fixture>() }

        }

        return generateRound(List.empty(), fixtures)

    }


    fun toDTO(): LeagueDTO {
        return LeagueDTO(id, name, rounds.map { it.map { it.toDTO() } })
    }
}
