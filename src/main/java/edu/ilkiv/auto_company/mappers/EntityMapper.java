package edu.ilkiv.auto_company.mappers;

public interface EntityMapper<D, E> {
    E toEntity(D dto);
    D toDto(E entity);
}