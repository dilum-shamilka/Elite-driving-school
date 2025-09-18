package lk.ijse.dao;

import lk.ijse.dao.custom.impl.*;
import lk.ijse.dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return (daoFactory == null) ? (daoFactory = new DAOFactory()) : daoFactory;
    }

    public enum DAOTypes {
        USER,
        STUDENT,
        COURSE,
        INSTRUCTOR,
        LESSON,
        PAYMENT,
        instructor, ENROLLMENT, ROLE // Added new type
    }

    public SuperDAO getDAO(DAOTypes daoType) {
        switch (daoType) {
            case USER:
                return new UserDAOImpl();
            case INSTRUCTOR:
                return new InstructorDAOImpl();
            case STUDENT:
                return new StudentDAOImpl();
            case COURSE:
                return new CourseDAOImpl();
            case LESSON:
                return new LessonDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case ROLE: // Added case for ROLE
                return new RoleDAOImpl();
            default:
                return null;
        }
    }
}