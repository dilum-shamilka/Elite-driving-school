package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.RoleDTO;

import java.util.List;

public interface RoleBO extends SuperBO {
    List<RoleDTO> getAllRoles() throws Exception;
    boolean saveRole(RoleDTO dto) throws Exception;
    boolean updateRole(RoleDTO dto) throws Exception;
    boolean deleteRole(int roleId) throws Exception;
    RoleDTO getByRoleName(String roleName) throws Exception;
}