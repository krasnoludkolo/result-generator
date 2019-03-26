package io.krasnoludkolo.infrastructure.database

import io.vavr.collection.List
import org.jooq.DSLContext

interface SqlFetchQuery<T> {

    fun execute(create: DSLContext): List<T>

}