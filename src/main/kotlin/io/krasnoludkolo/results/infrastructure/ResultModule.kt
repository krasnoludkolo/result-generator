package io.krasnoludkolo.results.infrastructure

import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.krasnoludkolo.results.domain.ResultsFacade
import java.util.*

class ResultModule {

    private val resultsFacade: ResultsFacade =
        ResultsFacade.createInMemory()

    fun api(){
        return path("/results"){
            get(":id"){
                val id = it.pathParam("id")
                it.result(resultsFacade.getLeagueById(UUID.fromString(id)).toString())
            }
        }
    }

}