package krasnoludkolo.results

import io.krasnoludkolo.results.ResultsFacade
import io.krasnoludkolo.results.api.FixtureDTO
import io.krasnoludkolo.results.api.GenerationErrors
import io.vavr.collection.List
import spock.lang.Specification

class ResultsFacadeKtTest extends Specification {

    def "Should generate league without exceptions"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def league = facade.generateLeague(4, "test")

        then:
        league.isRight()
    }

    def "League name should not be empty"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def error = facade.generateLeague(4, "").getLeft()

        then:
        error == GenerationErrors.EMPTY_LEAGUE_NAME
    }

    def "Should get already generated league by uuid"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def league = facade.generateLeague(4, "test").get()

        then:
        facade.getLeagueById(league.uuid).get() == league
    }

    def "Number of teams should not be less then 3"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def error = facade.generateLeague(n, "test").getLeft()

        then:
        error == GenerationErrors.NOT_ENOUGH_TEAMS

        where:
        n << [-2, -1, 0, 1, 2]
    }

    def "Number of teams should not be odd"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def error = facade.generateLeague(n, "test").getLeft()

        then:
        error == GenerationErrors.ODD_TEAM_NUMBER

        where:
        n << [3, 5, 7, 11, 17]
    }


    def "League should have 2(n-1) rounds"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def league = facade.generateLeague(n, "test").get()

        def map = league.getRounds().map { it.map { f -> f.host + ":" + f.guest } }
        then:
        map.size() == expected

        where:
        n  | expected
        4  | 6
        6  | 10
        8  | 14
        10 | 18
        16 | 30
    }

    def "Team should have n-1 distinct rivals"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def league = facade.generateLeague(n, "test").get()

        then:
        getTeamRivals('a', league.rounds).distinct().size() == expected

        where:
        n  | expected
        4  | 3
        6  | 5
        10 | 9
        16 | 15
    }

    def "Team should have 2(n-1) fixtures"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def league = facade.generateLeague(n, "test").get()

        then:
        league.rounds.flatMap { it.filter { isInFixture('a', it) } }.size() == expected

        where:
        n  | expected
        4  | 6
        6  | 10
        10 | 18
        16 | 30
    }

    def "in each round team should have max one fixture"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def league = facade.generateLeague(n, "test").get()

        then:

        league.rounds != List.empty()

        league.rounds
                .map { it.flatMap { List.of(it.host, it.guest) } }
                .forEach {
                    assert it.size() == it.distinct().size()
                }

        where:
        n  | _
        4  | _
        6  | _
        10 | _
        16 | _

    }


    def getTeamRivals(String name, List<List<FixtureDTO>> rounds) {
        return rounds
                .flatMap { it.filter { isInFixture(name, it) } }
                .map { getRival(name, it) }
    }

    def isInFixture(String name, FixtureDTO fixture) {
        return fixture.host == name || fixture.guest == name
    }

    def getRival(String name, FixtureDTO fixture) {
        return fixture.guest == name ? fixture.host : fixture.guest
    }

}
