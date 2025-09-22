package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.PaymentDTO;
import org.hibernate.Session;

public interface PaymentDAO extends CrudDAO<PaymentDTO, Integer> {

    boolean save(PaymentDTO dto, Session session);
    boolean update(PaymentDTO dto, Session session);
    boolean delete(Integer id, Session session);
    PaymentDTO get(Integer id, Session session);
}