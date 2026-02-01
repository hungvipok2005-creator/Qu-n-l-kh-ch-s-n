package dao;

import java.util.List;

public interface BaseDAO<T> {
    void insert(T obj);
    void update(T obj);
    void delete(String id);
    List<T> getAll();
    T getById(String id);
    List<T> search(String keyword);
}
