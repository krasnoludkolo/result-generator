package io.krasnoludkolo.results.infrastructure.sql

import io.krasnoludkolo.infrastructure.database.SqlFetchQuery
import io.krasnoludkolo.results.api.LeagueDTO
import org.jooq.DSLContext

class LeagueFindAllQuery:SqlFetchQuery<List<LeagueDTO>>{

    override fun execute(create: DSLContext): io.vavr.collection.List<List<LeagueDTO>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}