package io.krasnoludkolo.infrastructure

import io.vavr.control.Option
import io.vavr.collection.List
import java.util.UUID

interface Repository<T> {

    fun save(uuid: UUID, t: T)

    fun findOne(uuid: UUID): Option<T>

    fun findAll(): List<T>

    fun delete(uuid: UUID)

    fun update(uuid: UUID, t: T)

}
