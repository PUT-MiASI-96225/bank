package pl.mefiu.bank;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static final SessionFactory wbkSessionFactory;

    private static final SessionFactory pkoSessionFactory;

    static {
        try {
            wbkSessionFactory = new Configuration().configure("wbkconfig.cfg.xml").buildSessionFactory();

            pkoSessionFactory = new Configuration().configure("pkoconfig.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getWbkSession() throws HibernateException {
        return wbkSessionFactory.openSession();
    }

    public static Session getPkoSession() throws HibernateException {
        return pkoSessionFactory.openSession();
    }

}
