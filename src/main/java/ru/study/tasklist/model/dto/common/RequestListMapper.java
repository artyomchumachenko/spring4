package ru.study.tasklist.model.dto.common;

import ru.study.tasklist.model.domain.common.AbstractEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RequestListMapper<R extends EntityRequest, M extends AbstractEntity> implements Consumer<List<R>> {
    private final Collection<M> entities;
    private final Map<UUID, M> byIdCache;
    private final Supplier<M> creator;
    private final BiFunction<R, M, M> mapper;

    public RequestListMapper(Collection<M> entities, Supplier<M> creator, BiFunction<R, M, M> mapper) {
        this.entities = entities;
        this.byIdCache = entities.stream().collect(Collectors.toMap(AbstractEntity::getId, e -> e));
        this.creator = creator;
        this.mapper = mapper;

    }

    @Override
    public void accept(List<R> requests) {

        var mappedEntities = requests.stream().map(r -> {
            M entity;
            if (r.id() == null) {
                entity = creator.get();
            } else {
                entity = Optional.ofNullable(byIdCache.get(r.id())).orElseThrow(() ->
                        new IllegalArgumentException(String.format("Invalid subEntity %s! id is not found.", r.id())));
            }
            return mapper.apply(r, entity);
        }).toList();


        entities.clear();
        entities.addAll(mappedEntities);
    }
}
