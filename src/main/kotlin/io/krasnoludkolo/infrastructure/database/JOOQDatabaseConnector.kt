package io.krasnoludkolo.infrastructure.database

import io.krasnoludkolo.infrastructure.Repository
import io.vavr.collection.List
import io.vavr.control.Option
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.SQLException
import java.util.UUID
import java.util.logging.Level
import java.util.logging.Logger

abstract class JOOQDatabaseConnector<T> : Repository<T> {

    private val dbConnectionInfo = DatabaseConnectionInfo()

    override fun save(uuid: UUID, t: T) {
        try {
            val connection = dbConnectionInfo.createConnection()
            connection.use{
                val ctx = DSL.using(it, SQLDialect.POSTGRES)
                saveQuery(uuid,t).execute(ctx)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
            throw IllegalStateException(e.message)
        }

    }

    override fun findOne(uuid: UUID): Option<T> {
        try {
            dbConnectionInfo.createConnection().use { connection ->
                val ctx = DSL.using(connection, SQLDialect.POSTGRES)
                return findOneQuery(uuid).execute(ctx).headOption()
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
        }

        return Option.none()
    }


    override fun findAll(): List<T> {
        try {
            dbConnectionInfo.createConnection().use { connection ->
                val ctx = DSL.using(connection, SQLDialect.POSTGRES)
                return findAllQuery().execute(ctx)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
            throw IllegalStateException(e.message)
        }

    }

    override fun delete(uuid: UUID) {
        try {
            dbConnectionInfo.createConnection().use { connection ->
                val create = DSL.using(connection, SQLDialect.POSTGRES)
                deleteQuery(create, uuid).execute(create)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
            throw IllegalStateException(e.message)
        }

    }

    override fun update(uuid: UUID, t: T) {
        try {
            dbConnectionInfo.createConnection().use { connection ->
                val create = DSL.using(connection, SQLDialect.POSTGRES)
                updateQuery(t, uuid).execute(create)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
        }

    }


    abstract fun saveQuery(uuid: UUID, t: T): SqlExecuteQuery
    abstract fun findOneQuery(uuid: UUID): SqlFetchQuery<T>
    abstract fun findAllQuery(): SqlFetchQuery<T>
    abstract fun deleteQuery(create: DSLContext, uuid: UUID): SqlExecuteQuery
    abstract fun updateQuery(t: T, uuid: UUID): SqlExecuteQuery

    companion object {

        private val LOGGER = Logger.getLogger(JOOQDatabaseConnector::class.java.name)
    }

}