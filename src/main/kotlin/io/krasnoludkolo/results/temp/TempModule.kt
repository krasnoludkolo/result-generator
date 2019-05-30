package io.krasnoludkolo.results.temp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.apibuilder.ApiBuilder
import io.krasnoludkolo.infrastructure.InMemoryRepository
import io.krasnoludkolo.results.api.FixtureDTO
import io.krasnoludkolo.results.api.FixtureResult
import io.krasnoludkolo.results.api.LeagueDTO
import io.krasnoludkolo.results.temp.api.NewTempFixtureDTO
import io.vavr.collection.List
import io.vavr.jackson.datatype.VavrModule
import java.util.*

class TempModule {

    private val repository = InMemoryRepository<LeagueDTO>()
    private val mapper = ObjectMapper().registerModule(KotlinModule()).registerModule(VavrModule())
    private val uuid = UUID.fromString("e5a5703a-05e9-41a8-9e62-f84d88542278")

    init {
        val firstRound = List.of(
            FixtureDTO(UUID.randomUUID(), "a", "b", 1, FixtureResult.NOT_SET)
        )
        repository.save(uuid, LeagueDTO(uuid, "test", List.of(firstRound)))
    }

    fun api() {
        return ApiBuilder.path("temp/") {
            ApiBuilder.get("league/:id") {
                val id = it.pathParam("id")
                it.result(mapper.writeValueAsString(repository.findOne(UUID.fromString(id)).get()))
            }
            ApiBuilder.get("/uuid") {
                it.result(uuid.toString())
            }
            ApiBuilder.post("/") { ctx ->
                val request = mapper.readValue<NewTempFixtureDTO>(ctx.body())
                val league = repository.findOne(uuid).get()!!
                val updatedLeague = league.insertFixture(request)
                repository.update(uuid, updatedLeague)
                ctx.result(mapper.writeValueAsString(updatedLeague))
                ctx.status(200)
            }
        }


    }

}