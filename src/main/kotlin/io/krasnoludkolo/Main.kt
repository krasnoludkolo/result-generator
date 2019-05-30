package io.krasnoludkolo

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.krasnoludkolo.results.infrastructure.ResultModule
import io.krasnoludkolo.results.temp.TempModule

fun main() {

    val app = Javalin.create().start(getHerokuAssignedPort())
    val module = ResultModule()
    val tempModule = TempModule()

    app.routes {
        module.api()
        tempModule.api()
        statusEndpoint()
    }


}

private fun statusEndpoint() {
    path("/status") {
        get("/") {
            it.result("Live")
            it.status(200)
        }
    }
}

fun getHerokuAssignedPort(): Int {
    val port = System.getenv()["PORT"]
    if (port != null) {
        return Integer.parseInt(port)
    }
    return 7000

}
