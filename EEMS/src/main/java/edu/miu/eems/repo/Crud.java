package edu.miu.eems.repo;

import java.util.List;
import java.util.Optional;

public interface Crud <T, ID> {
    T save(T t);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean deleteById(ID id);
}
