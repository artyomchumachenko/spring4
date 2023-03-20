package ru.study.tasklist.model.dto.common;

import ru.study.tasklist.model.domain.common.AbstractEntity;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface MapperFactory {

    <R extends EntityRequest, M extends AbstractEntity> Consumer<List<R>> createCollectionMapper(Collection<M> entities, Supplier<M> creator, BiFunction<R, M, M> mapper);

}
