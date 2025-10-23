package edu.miu.eems.repo;

import java.util.List;
import java.util.Optional;

public interface ICrud<T, ID> {
    T add(T t);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean deleteById(ID id);
    void update(T t);

}
