package DAO;

import java.util.List;

public interface crudOperator<T> {
    List<T> findAll();
    List<T> saveAll(List<T> toSave);
    T save(T toSave);
}
