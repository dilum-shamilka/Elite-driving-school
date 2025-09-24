package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SuperDAO;
import lk.ijse.dao.custom.LessonDAO;
import lk.ijse.dto.LessonDTO;
import lk.ijse.entity.Lesson;
import lk.ijse.entity.Instructor;
import lk.ijse.entity.Course;
import lk.ijse.entity.Student;
import lk.ijse.configaration.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class LessonDAOImpl implements LessonDAO, SuperDAO {

    @Override
    public boolean save(LessonDTO dto) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();


            Instructor instructor = session.get(Instructor.class, dto.getInstructorId());
            Course course = session.get(Course.class, dto.getCourseId());
            Student student = session.get(Student.class, dto.getStudentId());

            if (instructor == null || course == null || student == null) {
                return false;
            }

            Lesson lesson = new Lesson(dto.getDate(), dto.getTime(), dto.getStatus(), instructor, course, student);
            session.persist(lesson);
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
    public boolean update(LessonDTO dto) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            Lesson lesson = session.get(Lesson.class, dto.getLessonId());
            if (lesson != null) {

                Instructor newInstructor = session.get(Instructor.class, dto.getInstructorId());
                Course newCourse = session.get(Course.class, dto.getCourseId());
                Student newStudent = session.get(Student.class, dto.getStudentId());

                if (newInstructor == null || newCourse == null || newStudent == null) {
                    return false; // Related entity not found
                }

                lesson.setDate(dto.getDate());
                lesson.setTime(dto.getTime());
                lesson.setStatus(dto.getStatus());
                lesson.setInstructor(newInstructor);
                lesson.setCourse(newCourse);
                lesson.setStudent(newStudent);
                session.merge(lesson);
            }
            transaction.commit();
            return lesson != null;
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
            Lesson lesson = session.get(Lesson.class, id);
            if (lesson != null) {
                session.remove(lesson);
            }
            transaction.commit();
            return lesson != null;
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
    public LessonDTO get(Integer id) {
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            Lesson lesson = session.get(Lesson.class, id);
            if (lesson != null) {
                return new LessonDTO(
                        lesson.getLessonId(),
                        lesson.getDate(),
                        lesson.getTime(),
                        lesson.getStatus(),
                        lesson.getInstructor().getInstructorId(),
                        lesson.getCourse().getCourseId(),
                        lesson.getStudent().getStudentId()
                );
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
    public List<LessonDTO> getAll() {
        Session session = null;
        List<LessonDTO> lessonDTOList = new ArrayList<>();
        try {
            session = FactoryConfiguration.getInstance().getSession();
            Query<Lesson> query = session.createQuery("FROM Lesson", Lesson.class);
            List<Lesson> lessons = query.list();
            for (Lesson lesson : lessons) {
                lessonDTOList.add(new LessonDTO(
                        lesson.getLessonId(),
                        lesson.getDate(),
                        lesson.getTime(),
                        lesson.getStatus(),
                        lesson.getInstructor().getInstructorId(),
                        lesson.getCourse().getCourseId(),
                        lesson.getStudent().getStudentId()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return lessonDTOList;
    }
}