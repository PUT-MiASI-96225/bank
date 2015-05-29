package pl.mefiu.bank;

import java.util.List;

public abstract class RepositoryImpl {

    public abstract <T> List<T> read(String query, Object[] params);

    public abstract void update(Object entity);

    public abstract void delete(Object entity);

}