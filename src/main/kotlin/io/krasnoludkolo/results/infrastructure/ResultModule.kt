package io.krasnoludkolo.results.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.apibuilder.ApiBuilder.*
import io.krasnoludkolo.results.domain.ResultsFacade
import java.util.*

class ResultModule {

    private val leagueRepository = LeagueRepository()
    private val resultsFacade = ResultsFacade.create(leagueRepository)
    private val mapper = ObjectMapper().registerModule(KotlinModule())


    fun api() {
        return path("/results") {
            get(":id") {
                val id = it.pathParam("id")
                it.result(resultsFacade.getLeagueById(UUID.fromString(id)).toString())
            }
            post("/") { ctx ->
                val request = mapper.readValue<NewLeague>(ctx.body())
                val league = resultsFacade.generateLeague(request.numberOfTeams, request.name)
                ctx.result(league.map { it.uuid.toString() }.getOrElseGet { it.toString() })
                ctx.status(league.map { 200 }.getOrElseGet { 400 })
            }
        }
    }

    inner class NewLeague(
        val name: String,
        val numberOfTeams: Int
    )

}