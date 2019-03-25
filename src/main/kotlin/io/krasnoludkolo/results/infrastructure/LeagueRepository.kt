package io.krasnoludkolo.results.infrastructure

import io.krasnoludkolo.infrastructure.JOOQDatabaseConnector
import io.krasnoludkolo.results.api.FixtureDTO
import io.krasnoludkolo.results.api.LeagueDTO
import io.krasnoludkolo.results.domain.League
import io.vavr.collection.List
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import java.util.*

import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table

class LeagueRepository: JOOQDatabaseConnector<LeagueDTO>() {

    val LEAGUE_TABLE = "league"
    val FIXTURE_TABLE = "fixture"

    override fun saveQuery(create: DSLContext, entity: LeagueDTO) {
        create
            .insertInto(table(LEAGUE_TABLE))
            .columns(field("uuid"),field("name"))
            .values(entity.uuid,entity.name)
        entity.rounds.flatMap { it }.forEach{
            create
                .insertInto(table(FIXTURE_TABLE))
                .columns(field("uuid"),field("host"),field("guest"))
                .values(it.uuid,it.host,it.guest)
        }
    }

    override fun findOneQuery(create: DSLContext, uuid: UUID): Result<Record> {
        return create
            .select()
            .from(table(LEAGUE_TABLE))
            .where(field("uuid").eq(uuid))
            .limit(1)
            .fetch()
    }

    override fun convertRecordToEntity(record: Record): LeagueDTO {
        return LeagueDTO(
            record[0] as UUID,
            record[1] as String,
            record[2] as List<List<FixtureDTO>>
        )
    }

    override fun findAllQuery(create: DSLContext): Result<Record> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteQuery(create: DSLContext, uuid: UUID) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateQuery(create: DSLContext, entity: LeagueDTO, uuid: UUID) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}