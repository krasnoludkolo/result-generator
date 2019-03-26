package io.krasnoludkolo.infrastructure.database

import org.jooq.DSLContext

interface SqlExecuteQuery{

    fun execute(create: DSLContext)

}