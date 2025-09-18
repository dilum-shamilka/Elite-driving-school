package lk.ijse.bo.impl;

import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.dto.PaymentDTO;
import java.util.List;

public class PaymentBOImpl implements PaymentBO {
    private final PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PAYMENT);

    @Override
    public boolean savePayment(PaymentDTO dto) throws Exception {
        return paymentDAO.save(dto);
    }

    @Override
    public boolean updatePayment(PaymentDTO dto) throws Exception {
        return paymentDAO.update(dto);
    }

    @Override
    public boolean deletePayment(int id) throws Exception {
        return paymentDAO.delete(id);
    }

    @Override
    public List<PaymentDTO> getAllPayments() throws Exception {
        return paymentDAO.getAll();
    }

    @Override
    public PaymentDTO getPayment(int id) throws Exception {
        return paymentDAO.get(id);
    }
}