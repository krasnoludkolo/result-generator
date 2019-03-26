package io.krasnoludkolo.results.infrastructure

import io.krasnoludkolo.infrastructure.database.JOOQDatabaseConnector
import io.krasnoludkolo.infrastructure.database.ResultMapper
import io.krasnoludkolo.infrastructure.database.SqlExecuteQuery
import io.krasnoludkolo.infrastructure.database.SqlFetchQuery
import io.krasnoludkolo.results.api.LeagueDTO
import io.krasnoludkolo.results.infrastructure.sql.LeagueFindOneQuery
import io.krasnoludkolo.results.infrastructure.sql.LeagueSaveQuery
import io.vavr.collection.List
import org.jooq.DSLContext
import org.jooq.Record
import java.util.*

class LeagueRepository : JOOQDatabaseConnector<LeagueDTO>() {

    val mapper = ResultMapper {
        LeagueDTO(
            it[0] as UUID,
            it[1] as String,
            List.empty()
        )
    }

    override fun saveQuery(uuid: UUID, t: LeagueDTO): SqlExecuteQuery {
        return LeagueSaveQuery(t)
    }

    override fun findOneQuery(uuid: UUID): SqlFetchQuery<LeagueDTO> {
        return LeagueFindOneQuery(uuid, mapper)
    }

    override fun findAllQuery(): SqlFetchQuery<LeagueDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteQuery(create: DSLContext, uuid: UUID): SqlExecuteQuery {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateQuery(t: LeagueDTO, uuid: UUID): SqlExecuteQuery {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}