package io.krasnoludkolo.results

import io.krasnoludkolo.results.api.League
import java.util.*

fun generateLeague(numberOfTeams: Int):League{
    return League(UUID.randomUUID(),"",HashMap())
}

