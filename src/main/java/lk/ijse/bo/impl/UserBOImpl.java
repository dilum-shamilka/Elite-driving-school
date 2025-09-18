package lk.ijse.bo.impl;

import lk.ijse.bo.custom.UserBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.UserDAO;
import lk.ijse.dto.UserDTO;

import java.util.List;

public class UserBOImpl implements UserBO {

    private final UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public boolean saveUser(UserDTO dto) throws Exception {
        return userDAO.save(dto);
    }

    @Override
    public boolean updateUser(UserDTO dto) throws Exception {
        return userDAO.update(dto);
    }

    @Override
    public boolean deleteUser(int id) throws Exception {
        return userDAO.delete(id);
    }

    @Override
    public UserDTO getUser(int id) throws Exception {
        return userDAO.get(id);
    }

    @Override
    public List<UserDTO> getAllUsers() throws Exception {
        return userDAO.getAll();
    }
}