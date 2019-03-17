package io.krasnoludkolo.results

import io.krasnoludkolo.results.api.LeagueDTO
import io.vavr.collection.List
import java.util.*

class League private constructor(val name: String, numberOfTeams: Int) {

    val id: UUID = UUID.randomUUID()
    private val rounds: List<List<Fixture>> = List.empty<List<Fixture>>()

    companion object {
        fun generate(name: String, numberOfTeams: Int) = League(name, numberOfTeams)
    }

    init {
        generateFor(numberOfTeams)
    }

    private fun generateFor(numberOfTeams: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun toDTO(): LeagueDTO {
        return LeagueDTO(id, name, rounds.map { it.map { it.toDTO() } })
    }
}