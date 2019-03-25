package io.krasnoludkolo.infrastructure

import io.vavr.collection.List
import io.vavr.control.Option
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

abstract class JOOQDatabaseConnector<T> : Repository<T> {

    private val dbConnectionInfo = DatabaseConnectionInfo()

    override fun save(uuid: UUID, t: T) {
        try {
            val connection = dbConnectionInfo.createConnection()
            connection.use{
                val create = DSL.using(it, SQLDialect.POSTGRES)
                saveQuery(create, t)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
            throw IllegalStateException(e.message)
        }

    }

    override fun findOne(uuid: UUID): Option<T> {
        try {
            dbConnectionInfo.createConnection().use { connection ->
                val create = DSL.using(connection, SQLDialect.POSTGRES)
                val result = findOneQuery(create, uuid)
                val entity = convertRecordToEntity(result[0])
                return Option.of(entity)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
        }

        return Option.none()
    }


    override fun findAll(): List<T> {
        try {
            dbConnectionInfo.createConnection().use { connection ->
                val create = DSL.using(connection, SQLDialect.POSTGRES)
                val result = findAllQuery(create)
                return convertToBetList(result)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
            throw IllegalStateException(e.message)
        }

    }


    private fun convertToBetList(result: Result<Record>): List<T> {
        var resultList = List.empty<T>()
        for (record in result) {
            val entity = convertRecordToEntity(record)
            resultList = resultList.append(entity)
        }
        return resultList
    }


    override fun delete(uuid: UUID) {
        try {
            dbConnectionInfo.createConnection().use { connection ->
                val create = DSL.using(connection, SQLDialect.POSTGRES)
                deleteQuery(create, uuid)
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
                updateQuery(create, t, uuid)
            }
        } catch (e: SQLException) {
            LOGGER.log(Level.SEVERE, e.message)
        }

    }


    protected abstract fun saveQuery(create: DSLContext, entity: T)

    protected abstract fun findOneQuery(create: DSLContext, uuid: UUID): Result<Record>

    protected abstract fun convertRecordToEntity(record: Record): T

    protected abstract fun findAllQuery(create: DSLContext): Result<Record>

    protected abstract fun deleteQuery(create: DSLContext, uuid: UUID)

    protected abstract fun updateQuery(create: DSLContext, entity: T, uuid: UUID)

    companion object {

        private val LOGGER = Logger.getLogger(JOOQDatabaseConnector::class.java.name)
    }

}