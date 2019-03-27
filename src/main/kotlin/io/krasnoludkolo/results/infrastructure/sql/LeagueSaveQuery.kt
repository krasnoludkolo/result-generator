package io.krasnoludkolo.results.infrastructure.sql

import io.krasnoludkolo.infrastructure.database.SqlExecuteQuery
import io.krasnoludkolo.results.api.LeagueDTO
import org.jooq.DSLContext
import org.jooq.impl.DSL.*

class LeagueSaveQuery(private val entity:LeagueDTO):SqlExecuteQuery {

    override fun execute(create: DSLContext) {
        saveLeague(create)
        saveFixtures(create)
    }

    private fun saveLeague(create: DSLContext) {
        create
            .insertInto(table(SqlConstants.LEAGUE_TABLE))
            .columns(field("uuid"), field("name"))
            .values(entity.uuid, entity.name)
            .execute()
    }

    private fun saveFixtures(create: DSLContext) {
        entity.rounds.flatMap { it }.forEach {
            create
                .insertInto(table(SqlConstants.FIXTURE_TABLE))
                .columns(field("uuid"), field("host"), field("guest"))
                .values(it.uuid, it.host, it.guest)
                .execute()
        }
    }
}