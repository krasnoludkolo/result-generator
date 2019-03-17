package krasnoludkolo.results

import io.krasnoludkolo.results.ResultsFacade
import io.krasnoludkolo.results.api.GenerationErrors
import spock.lang.Specification

class ResultsFacadeKtTest extends Specification {

    def "Generate league"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def league = facade.generateLeague(3, "test")

        then:
        league.isRight()
    }

    def "Get generated league"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def league = facade.generateLeague(3, "test").get()

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


    def "League should have 2(n-1) rounds"() {
        given:
        def facade = ResultsFacade.@Factory.createInMemory()

        when:
        def league = facade.generateLeague(n, "test").get()

        then:
        league.getRounds().size() == expected

        where:
        n  | expected
        3  | 4
        4  | 6
        5  | 8
        16 | 30
    }


}
