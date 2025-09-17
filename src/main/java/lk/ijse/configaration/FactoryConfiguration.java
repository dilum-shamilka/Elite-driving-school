package lk.ijse.configaration;

import lk.ijse.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;

    private FactoryConfiguration() {
        try {
            Properties properties = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("hibernate.properties")) {
                if (input == null) {
                    throw new RuntimeException("hibernate.properties file not found in classpath.");
                }
                properties.load(input);
            }

            Configuration configuration = new Configuration();
            configuration.setProperties(properties)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Role.class)
                    .addAnnotatedClass(Student.class)
                    .addAnnotatedClass(Payment.class)
                    .addAnnotatedClass(Lesson.class)
                    .addAnnotatedClass(Instructor.class)
                    .addAnnotatedClass(Course.class);

            sessionFactory = configuration.buildSessionFactory();
            System.out.println("✅ Hibernate SessionFactory created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Hibernate configuration failed", e);
        }
    }

    public static FactoryConfiguration getInstance() {
        if (factoryConfiguration == null) {
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}