package io.krasnoludkolo.infrastructure

import io.vavr.control.Option
import io.vavr.collection.List
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap


class InMemoryRepository<T> : Repository<T> {

    private val map = ConcurrentHashMap<UUID, T>()

    override fun save(uuid: UUID, t: T) {
        map[uuid] = t
    }

    override fun findOne(uuid: UUID): Option<T> {
        return Option.of(map[uuid])
    }

    override fun findAll(): List<T> {
        return List.ofAll(map.values)
    }

    override fun delete(uuid: UUID) {
        map.remove(uuid)
    }

    override fun update(uuid: UUID, t: T) {
        map[uuid] = t
    }

}