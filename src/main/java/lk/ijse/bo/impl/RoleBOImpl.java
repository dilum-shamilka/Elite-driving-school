package lk.ijse.bo.impl;

import lk.ijse.bo.custom.RoleBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.RoleDAO;
import lk.ijse.dto.RoleDTO;

import java.util.List;

public class RoleBOImpl implements RoleBO {
    private final RoleDAO roleDAO = (RoleDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ROLE);

    @Override
    public List<RoleDTO> getAllRoles() throws Exception {
        return roleDAO.getAll();
    }

    @Override
    public boolean saveRole(RoleDTO dto) throws Exception {
        return roleDAO.save(dto);
    }

    @Override
    public boolean updateRole(RoleDTO dto) throws Exception {
        return roleDAO.update(dto);
    }

    @Override
    public boolean deleteRole(int roleId) throws Exception {
        return roleDAO.delete(roleId);
    }

    @Override
    public RoleDTO getByRoleName(String roleName) throws Exception {
        return roleDAO.getByRoleName(roleName);
    }
}