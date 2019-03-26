package io.krasnoludkolo.results.infrastructure.sql

import io.krasnoludkolo.infrastructure.database.ResultMapper
import io.krasnoludkolo.infrastructure.database.SqlFetchQuery
import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.collection.List
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.DSL.table
import java.util.*

class LeagueFindOneQuery(
    private val uuid: UUID,
    private val mapper: ResultMapper<LeagueDTO>
) : SqlFetchQuery<LeagueDTO> {

    override fun execute(create: DSLContext): List<LeagueDTO> {
        val result = create
            .select()
            .from(table(SqlConstants.LEAGUE_TABLE))
            .where(DSL.field("uuid").eq(uuid))
            .limit(1)
            .fetch()
        return List.ofAll(mapper.mapAll(result))
    }

}