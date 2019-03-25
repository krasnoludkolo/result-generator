package io.krasnoludkolo

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.krasnoludkolo.results.infrastructure.ResultModule

fun main(args: Array<String>) {

    val app = Javalin.create().start(7000)
    val module = ResultModule()


    app.routes {
        module.api()
        path("/status"){
            get("/"){
                it.result("Live")
                it.status(200)
            }
        }
    }


}
