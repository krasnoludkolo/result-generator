package io.krasnoludkolo.results.domain

import io.krasnoludkolo.infrastructure.InMemoryRepository
import io.krasnoludkolo.infrastructure.Repository
import io.krasnoludkolo.results.api.GenerationErrors
import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.control.Either
import io.vavr.control.Option
import java.util.*


class ResultsFacade private constructor(val repo: Repository<LeagueDTO>) {


    companion object Factory {

        fun createInMemory(): ResultsFacade = ResultsFacade(InMemoryRepository())

        fun create(repo: Repository<LeagueDTO>) = ResultsFacade(repo)
    }

    fun generateLeague(numberOfTeams: Int, name: String): Either<GenerationErrors, LeagueDTO> {
        if (numberOfTeams < 3) {
            return Either.left(GenerationErrors.NOT_ENOUGH_TEAMS)
        } else if (name.isEmpty()) {
            return Either.left(GenerationErrors.EMPTY_LEAGUE_NAME)
        }
        return League.generate(name, numberOfTeams)
            .peek { repo.save(it.id, it.toDTO()) }
            .map { it.toDTO() }
    }

    fun getLeagueById(id: UUID): Option<LeagueDTO> {
        return repo.findOne(id)
    }

}
