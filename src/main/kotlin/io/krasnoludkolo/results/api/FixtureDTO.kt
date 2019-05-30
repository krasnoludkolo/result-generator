package io.krasnoludkolo.results.api

import io.krasnoludkolo.results.temp.api.NewTempFixtureDTO
import java.util.*

data class FixtureDTO(
    val uuid: UUID,
    val host: String,
    val guest: String,
    val round: Int,
    val fixtureResult: FixtureResult
) {
    constructor(from: NewTempFixtureDTO) :
            this(UUID.randomUUID(), from.host, from.guest, from.round, FixtureResult.NOT_SET)
}
