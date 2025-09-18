package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO; // Redundant import, as it will be moved to this package.
import lk.ijse.bo.impl.UserBOImpl;
import lk.ijse.bo.impl.StudentBOImpl;
import lk.ijse.bo.impl.InstructorBOImpl;
import lk.ijse.bo.impl.CourseBOImpl;
import lk.ijse.bo.impl.LessonBOImpl;
import lk.ijse.bo.impl.PaymentBOImpl;
import lk.ijse.bo.impl.RoleBOImpl;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return (boFactory == null) ? (boFactory = new BOFactory()) : boFactory;
    }

    public enum BOTypes {
        USER,
        STUDENT,
        INSTRUCTOR,
        COURSE,
        LESSON,
        PAYMENT,
        ROLE
    }

    public SuperBO getBO(BOTypes boType) {
        switch (boType) {
            case USER:
                return new UserBOImpl();
            case STUDENT:
                return new StudentBOImpl();
            case INSTRUCTOR:
                return new InstructorBOImpl();
            case COURSE:
                return new CourseBOImpl();
            case LESSON:
                return new LessonBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case ROLE:
                return new RoleBOImpl();
            default:
                return null;
        }
    }
}