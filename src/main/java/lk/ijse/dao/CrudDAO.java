package lk.ijse.dao;

import java.io.Serializable;
import java.util.List;

public interface CrudDAO<T, ID extends Serializable> extends SuperDAO {
    boolean save(T entity) throws Exception;
    boolean update(T entity) throws Exception;
    boolean delete(ID id) throws Exception;
    T get(ID id) throws Exception;
    List<T> getAll() throws Exception;
}