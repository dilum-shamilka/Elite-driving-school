package lk.ijse.util;

import lk.ijse.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration cfg = new Configuration();
            cfg.configure("/hibernate.cfg.xml"); // make sure your cfg XML is correct
            cfg.addAnnotatedClass(User.class);
            cfg.addAnnotatedClass(Role.class);
            cfg.addAnnotatedClass(Student.class);
            cfg.addAnnotatedClass(Course.class);
            cfg.addAnnotatedClass(Instructor.class);
            cfg.addAnnotatedClass(Lesson.class);
            cfg.addAnnotatedClass(Payment.class);
            return cfg.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to build SessionFactory");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
