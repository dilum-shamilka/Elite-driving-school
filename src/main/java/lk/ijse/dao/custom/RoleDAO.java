package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.RoleDTO;

public interface RoleDAO extends CrudDAO<RoleDTO, Integer> {
    RoleDTO getByRoleName(String roleName) throws Exception;
}