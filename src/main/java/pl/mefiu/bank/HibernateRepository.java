package pl.mefiu.bank;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

@SuppressWarnings("unchecked")
public final class HibernateRepository extends RepositoryImpl {

    public HibernateRepository(Session session) {
        setSession(session);
    }

    @Override
    public <T> List<T> read(String query, Object[] params) {
        session.beginTransaction();
        List<T> res;
        if(params == null) {
            res = (List<T>) session.createQuery(query).list();
        } else {
            Query q = session.createQuery(query);
            for(int i = 0; i < params.length; i++) {
                q.setParameter(i, params[0]);
            }
            res = (List<T>) q.list();
        }
        session.getTransaction().commit();
        session.close();
        return res;
    }

    public void update(Object entity) {
        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Object entity) {
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
        session.close();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        if(session != null) {
            this.session = session;
        } else {
            throw new IllegalArgumentException("session cannot be null!");
        }
    }

    private Session session;

}