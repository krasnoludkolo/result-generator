package io.krasnoludkolo.results

import io.krasnoludkolo.infrastructure.InMemoryRepository
import io.krasnoludkolo.infrastructure.Repository
import io.krasnoludkolo.results.api.GenerationErrors
import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.control.Either
import io.vavr.control.Option
import java.util.*


class ResultsFacade private constructor(val repo: Repository<League>) {


    companion object Factory {

        fun createInMemory(): ResultsFacade = ResultsFacade(InMemoryRepository())

    }

    fun generateLeague(numberOfTeams: Int, name: String): Either<GenerationErrors, LeagueDTO> {

        if(numberOfTeams<3){
            return Either.left(GenerationErrors.NOT_ENOUGH_TEAMS)
        }

        val league = League.generate(name, numberOfTeams)
        repo.save(league.id,league)
        return Either.right(league.toDTO())
    }

    fun getLeagueById(id: UUID): Option<LeagueDTO> {
        return repo.findOne(id).map { it.toDTO() }
    }

}
