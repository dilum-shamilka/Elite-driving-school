package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SuperDAO;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.entity.Payment;
import lk.ijse.entity.Student;
import lk.ijse.entity.Lesson; // Add this line
import lk.ijse.configaration.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO, SuperDAO {

    @Override
    public boolean save(PaymentDTO dto) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, dto.getStudentId());
            Lesson lesson = session.get(Lesson.class, dto.getLessonId()); // Add this line
            if (student == null || lesson == null) return false;

            // Update the constructor call
            Payment payment = new Payment(dto.getAmount(), dto.getDate(), dto.getStatus(), student, lesson);
            session.persist(payment);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(PaymentDTO dto) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Payment payment = session.get(Payment.class, dto.getPaymentId());
            if (payment == null) return false;

            Student student = session.get(Student.class, dto.getStudentId());
            Lesson lesson = session.get(Lesson.class, dto.getLessonId()); // Add this line
            if (student == null || lesson == null) return false;

            payment.setAmount(dto.getAmount());
            payment.setDate(dto.getDate());
            payment.setStatus(dto.getStatus());
            payment.setStudent(student);
            payment.setLesson(lesson); // Add this line

            session.merge(payment);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Payment payment = session.get(Payment.class, id);
            if (payment == null) return false;
            session.remove(payment);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PaymentDTO get(Integer id) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Payment payment = session.get(Payment.class, id);
            if (payment == null) return null;

            Integer studentId = (payment.getStudent() != null) ? payment.getStudent().getStudentId() : null;
            Integer lessonId = (payment.getLesson() != null) ? payment.getLesson().getLessonId() : null; // Add this line

            // Update the constructor call
            return new PaymentDTO(payment.getPaymentId(), payment.getAmount(), payment.getDate(), payment.getStatus(), studentId, lessonId);
        }
    }

    @Override
    public List<PaymentDTO> getAll() {
        List<PaymentDTO> list = new ArrayList<>();
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            // Update the query to join the Lesson table as well
            Query<Payment> query = session.createQuery("SELECT p FROM Payment p JOIN FETCH p.student JOIN FETCH p.lesson", Payment.class);
            List<Payment> payments = query.list();
            for (Payment payment : payments) {
                // Update the constructor call
                list.add(new PaymentDTO(payment.getPaymentId(), payment.getAmount(), payment.getDate(), payment.getStatus(), payment.getStudent().getStudentId(), payment.getLesson().getLessonId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean save(PaymentDTO dto, Session session) {
        return false;
    }

    @Override
    public boolean update(PaymentDTO dto, Session session) {
        return false;
    }

    @Override
    public boolean delete(Integer id, Session session) {
        return false;
    }

    @Override
    public PaymentDTO get(Integer id, Session session) {
        return null;
    }
}