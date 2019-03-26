package io.krasnoludkolo.results.infrastructure

import io.krasnoludkolo.infrastructure.database.ResultMapper
import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.collection.List
import org.jooq.Record
import java.util.*

class ResultToLeagueDTOMApper : ResultMapper<LeagueDTO>{

    override fun map(record: Record): LeagueDTO {
        return LeagueDTO(
            record[0] as UUID,
            record[1] as String,
            List.empty()
        )
    }

}