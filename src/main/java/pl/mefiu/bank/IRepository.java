package pl.mefiu.bank;

import java.util.List;

public interface IRepository<T> {

    List<T> read(String query, Object[] params);

    void update(T entity);

    void delete(T entity);

}
