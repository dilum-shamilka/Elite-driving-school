package lk.ijse.dao.custom.impl;

import lk.ijse.configaration.FactoryConfiguration;
import lk.ijse.dao.SuperDAO;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Course;
import lk.ijse.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDAOImpl implements StudentDAO, SuperDAO {

    @Override
    public boolean save(StudentDTO dto) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Student student = new Student(dto.getName(), dto.getEmail(), dto.getPhone(), dto.getAddress(), dto.getDob());
            session.persist(student);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(StudentDTO dto) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, dto.getStudentId());
            if (student != null) {
                student.setName(dto.getName());
                student.setEmail(dto.getEmail());
                student.setPhone(dto.getPhone());
                student.setAddress(dto.getAddress());
                student.setDob(dto.getDob());
                session.merge(student);
            }
            transaction.commit();
            return student != null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.remove(student);
            }
            transaction.commit();
            return student != null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public StudentDTO get(Integer id) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Student student = session.get(Student.class, id);
            if (student != null) {
                return new StudentDTO(
                        student.getStudentId(),
                        student.getName(),
                        student.getEmail(),
                        student.getPhone(),
                        student.getAddress(),
                        student.getDob()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<StudentDTO> getAll() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            List<Student> students = query.list();
            return students.stream()
                    .map(student -> new StudentDTO(
                            student.getStudentId(),
                            student.getName(),
                            student.getEmail(),
                            student.getPhone(),
                            student.getAddress(),
                            student.getDob()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<StudentDTO> getStudentsRegisteredInAllCourses() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {

            long totalCourses = session.createQuery("SELECT COUNT(c) FROM Course c", Long.class).uniqueResult();

            String hql = "SELECT s FROM Student s JOIN s.courses c GROUP BY s.studentId, s.name, s.email, s.phone, s.address, s.dob HAVING COUNT(c) = :totalCourses";

            Query<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("totalCourses", totalCourses);
            List<Student> students = query.list();

            return students.stream()
                    .map(student -> new StudentDTO(
                            student.getStudentId(),
                            student.getName(),
                            student.getEmail(),
                            student.getPhone(),
                            student.getAddress(),
                            student.getDob()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Map<StudentDTO, List<String>> getStudentsWithCourses() throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {

            String hql = "SELECT s, c.name FROM Student s JOIN s.courses c ORDER BY s.studentId";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> results = query.list();

            return results.stream()
                    .collect(Collectors.groupingBy(
                            result -> {
                                Student student = (Student) result[0];
                                return new StudentDTO(
                                        student.getStudentId(),
                                        student.getName(),
                                        student.getEmail(),
                                        student.getPhone(),
                                        student.getAddress(),
                                        student.getDob()
                                );
                            },
                            Collectors.mapping(result -> (String) result[1], Collectors.toList())
                    ));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error retrieving students with courses", e);
        }
    }
}