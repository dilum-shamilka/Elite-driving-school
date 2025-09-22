package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.entity.Payment;
import lk.ijse.entity.Student;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(PaymentDTO dto, Session session) {
        Student student = session.get(Student.class, dto.getStudentId());
        if (student == null) {
            System.err.println("Error: Student with ID " + dto.getStudentId() + " not found.");
            return false;
        }
        Payment payment = new Payment(dto.getAmount(), dto.getDate(), dto.getStatus(), student);
        session.persist(payment);
        return true;
    }

    @Override
    public boolean update(PaymentDTO dto, Session session) {
        Payment payment = session.get(Payment.class, dto.getPaymentId());
        if (payment == null) return false;

        Student student = session.get(Student.class, dto.getStudentId());
        if (student == null) return false;

        payment.setAmount(dto.getAmount());
        payment.setDate(dto.getDate());
        payment.setStatus(dto.getStatus());
        payment.setStudent(student);

        session.merge(payment);
        return true;
    }

    @Override
    public boolean delete(Integer id, Session session) {
        Payment payment = session.get(Payment.class, id);
        if (payment == null) return false;
        session.remove(payment);
        return true;
    }

    @Override
    public PaymentDTO get(Integer id, Session session) {
        Payment payment = session.get(Payment.class, id);
        if (payment == null) return null;

        Integer studentId = (payment.getStudent() != null) ? payment.getStudent().getStudentId() : null;
        return new PaymentDTO(payment.getPaymentId(), payment.getAmount(), payment.getDate(), payment.getStatus(), studentId);
    }

    @Override
    public List<PaymentDTO> getAll(Session session) {
        List<PaymentDTO> list = new ArrayList<>();
        Query<Payment> query = session.createQuery("SELECT p FROM Payment p JOIN FETCH p.student", Payment.class);
        List<Payment> payments = query.list();
        for (Payment payment : payments) {
            list.add(new PaymentDTO(payment.getPaymentId(), payment.getAmount(), payment.getDate(), payment.getStatus(), payment.getStudent().getStudentId()));
        }
        return list;
    }
}
