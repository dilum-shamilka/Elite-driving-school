package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SuperDAO;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.entity.Payment;
import lk.ijse.entity.Student;
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
            if (student == null) return false;

            Payment payment = new Payment(dto.getAmount(), dto.getDate(), dto.getStatus(), student);
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
            if (student == null) return false;

            payment.setAmount(dto.getAmount());
            payment.setDate(dto.getDate());
            payment.setStatus(dto.getStatus());
            payment.setStudent(student);

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

            // Handle the case where the student might be null
            Integer studentId = (payment.getStudent() != null) ? payment.getStudent().getStudentId() : null;
            return new PaymentDTO(payment.getPaymentId(), payment.getAmount(), payment.getDate(), payment.getStatus(), studentId);
        }
    }

    @Override
    public List<PaymentDTO> getAll() {
        List<PaymentDTO> list = new ArrayList<>();
        try (Session session = FactoryConfiguration.getInstance().getSession()) {

            Query<Payment> query = session.createQuery("SELECT p FROM Payment p JOIN FETCH p.student", Payment.class);
            List<Payment> payments = query.list();
            for (Payment payment : payments) {

                list.add(new PaymentDTO(payment.getPaymentId(), payment.getAmount(), payment.getDate(), payment.getStatus(), payment.getStudent().getStudentId()));
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