package lk.ijse.dao.custom.impl;

import lk.ijse.configaration.FactoryConfiguration;
import lk.ijse.dao.SuperDAO;
import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.entity.Course;
import lk.ijse.entity.Instructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO, SuperDAO {

    @Override
    public boolean save(CourseDTO dto) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            Instructor instructor = session.get(Instructor.class, dto.getInstructorId());
            if (instructor == null) {
                return false;
            }
            Course course = new Course(dto.getName(), dto.getDuration(), dto.getFee(), instructor);
            session.persist(course);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(CourseDTO dto) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, dto.getCourseId());
            if (course != null) {
                Instructor newInstructor = session.get(Instructor.class, dto.getInstructorId());
                if (newInstructor != null) {
                    course.setName(dto.getName());
                    course.setDuration(dto.getDuration());
                    course.setFee(dto.getFee());
                    course.setInstructor(newInstructor);
                    session.merge(course);
                }
            }
            transaction.commit();
            return course != null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean delete(Integer id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course != null) {
                session.remove(course);
            }
            transaction.commit();
            return course != null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public CourseDTO get(Integer id) {
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            Course course = session.get(Course.class, id);
            if (course != null) {
                Instructor instructor = course.getInstructor();
                String instructorName = (instructor != null) ? instructor.getName() : null;
                return new CourseDTO(course.getCourseId(), course.getName(), course.getDuration(), course.getFee(), instructor.getInstructorId(), instructorName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    @Override
    public List<CourseDTO> getAll() {
        Session session = null;
        List<CourseDTO> courseDTOList = new ArrayList<>();
        try {
            session = FactoryConfiguration.getInstance().getSession();
            Query<Course> courseQuery = session.createQuery("FROM Course", Course.class);
            List<Course> courses = courseQuery.list();

            for (Course course : courses) {
                Instructor instructor = course.getInstructor();
                String instructorName = (instructor != null) ? instructor.getName() : null;
                int instructorId = (instructor != null) ? instructor.getInstructorId() : 0;

                courseDTOList.add(new CourseDTO(
                        course.getCourseId(),
                        course.getName(),
                        course.getDuration(),
                        course.getFee(),
                        instructorId,
                        instructorName
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return courseDTOList;
    }
}