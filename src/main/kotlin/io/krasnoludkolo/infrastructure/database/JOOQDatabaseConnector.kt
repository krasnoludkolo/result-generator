package io.krasnoludkolo.infrastructure.database

import io.krasnoludkolo.infrastructure.Repository
import io.vavr.collection.List
import io.vavr.control.Option
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

abstract class JOOQDatabaseConnector<T> : Repository<T> {

    companion object {
        private val LOGGER = Logger.getLogger(JOOQDatabaseConnector::class.java.name)
    }

    private val dbConnectionInfo = DatabaseConnectionInfo()

    override fun save(uuid: UUID, t: T) {
        executeQuery(saveQuery(uuid,t))
    }

    override fun findOne(uuid: UUID): Option<T> {
        return executeFetchQuery(findOneQuery(uuid)).headOption()
    }

    override fun findAll(): List<T> {
        return executeFetchQuery(findAllQuery())
    }

    override fun delete(uuid: UUID) {
        executeQuery(deleteQuery(uuid))
    }


    override fun update(uuid: UUID, t: T) {
        executeQuery(updateQuery(t,uuid))
    }

    private fun executeQuery(query: SqlExecuteQuery){
        try {
            val connection = dbConnectionInfo.createConnection()
            connection.use{
                val ctx = DSL.using(it, SQLDialect.POSTGRES)
                query.execute(ctx)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
        }
    }

    private fun executeFetchQuery(query: SqlFetchQuery<T>):List<T>{
        try {
            val connection = dbConnectionInfo.createConnection()
            connection.use{
                val ctx = DSL.using(it, SQLDialect.POSTGRES)
                return query.execute(ctx)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
        }
        return List.empty<T>()
    }


    abstract fun saveQuery(uuid: UUID, t: T): SqlExecuteQuery
    abstract fun findOneQuery(uuid: UUID): SqlFetchQuery<T>
    abstract fun findAllQuery(): SqlFetchQuery<T>
    abstract fun deleteQuery(uuid: UUID): SqlExecuteQuery
    abstract fun updateQuery(t: T, uuid: UUID): SqlExecuteQuery

}