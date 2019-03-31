package io.krasnoludkolo.results.domain

import io.krasnoludkolo.results.api.FixtureDTO
import io.vavr.collection.List

class Matchday private constructor(
    numberOfTeams: Int,
    private val matchdayIndex: Int,
    combinations: List<TwoTeamsSet>
) {

    private val fixtures: List<Fixture>
    private val fixturesInRound = numberOfTeams / 2

    companion object {

        fun generate(numberOfTeams: Int, matchdayIndex: Int, combinations: List<TwoTeamsSet>): Matchday =
            Matchday(numberOfTeams, matchdayIndex, combinations)
    }

    init {
        fixtures = generateMatchday(combinations).map { Fixture(it, matchdayIndex) }
    }

    private fun generateMatchday(fixtures: List<TwoTeamsSet>): List<TwoTeamsSet> {
        return generateRound(List.empty(), fixtures)
    }

    private fun generateRound(
        alreadyTaken: List<TwoTeamsSet>,
        availableFixtures: List<TwoTeamsSet>
    ): List<TwoTeamsSet> {
        return when {
            availableFixtures.size() == 1 ->
                if (alreadyTaken.size() + 1 == fixturesInRound)
                    alreadyTaken.append(availableFixtures.head())
                else List.empty()

            else -> availableFixtures
                .find { canGenerate(alreadyTaken, it, availableFixtures) }
                .map {
                    generateRound(
                        alreadyTaken.append(it),
                        availableFixtures.reject { f -> f.containsOneOf(it) })
                }
                .getOrElse { List.empty<TwoTeamsSet>() }
        }
    }

    private fun canGenerate(
        alreadyTaken: List<TwoTeamsSet>,
        fix: TwoTeamsSet,
        availableFixtures: List<TwoTeamsSet>
    ): Boolean {
        return !generateRound(
            alreadyTaken.append(fix),
            availableFixtures.reject { fix.containsOneOf(it) }).isEmpty
    }

    fun getUsedCombinations(): List<TwoTeamsSet> {
        return fixtures.map { TwoTeamsSet(it.host, it.guest) }
    }

    fun toFixtureDTOList(): List<FixtureDTO> {
        return fixtures.map { it.toDTO() }
    }

}
