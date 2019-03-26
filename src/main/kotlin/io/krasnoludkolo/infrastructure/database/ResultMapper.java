package io.krasnoludkolo.infrastructure.database;

import io.vavr.collection.List;
import org.jooq.Record;
import org.jooq.Result;

@FunctionalInterface
public interface ResultMapper<T> {

    T map(Record record);

    default List<T> mapAll(Result<Record> result) {
        return List.ofAll(result)
            .map (this::map);
    }

}