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
        def error = facade.generateLeague(-2, "test").getLeft()
        def error1 = facade.generateLeague(0, "test").getLeft()
        def error2 = facade.generateLeague(2, "test").getLeft()

        then:
        error == GenerationErrors.NOT_ENOUGH_TEAMS
        error1 == GenerationErrors.NOT_ENOUGH_TEAMS
        error2 == GenerationErrors.NOT_ENOUGH_TEAMS
    }
}
