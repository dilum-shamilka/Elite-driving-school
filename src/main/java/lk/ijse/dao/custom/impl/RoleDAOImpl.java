package lk.ijse.dao.custom.impl;

import lk.ijse.configaration.FactoryConfiguration;
import lk.ijse.dao.SuperDAO;
import lk.ijse.dao.custom.RoleDAO;
import lk.ijse.dto.RoleDTO;
import lk.ijse.entity.Role;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO, SuperDAO {

    @Override
    public boolean save(RoleDTO dto) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Role role = new Role(dto.getRoleName());
            session.persist(role);
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
    public boolean update(RoleDTO dto) {
        Transaction transaction = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();
            Role role = session.get(Role.class, dto.getRoleId());
            if (role != null) {
                role.setRoleName(dto.getRoleName());
                session.merge(role);
            }
            transaction.commit();
            return role != null;
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
            Role role = session.get(Role.class, id);
            if (role != null) {
                session.remove(role);
            }
            transaction.commit();
            return role != null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public RoleDTO get(Integer id) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Role role = session.get(Role.class, id);
            if (role != null) {
                return new RoleDTO(role.getRoleId(), role.getRoleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<RoleDTO> getAll() {
        List<RoleDTO> roleDTOList = new ArrayList<>();
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<Role> query = session.createQuery("FROM Role", Role.class);
            List<Role> roles = query.list();
            for (Role role : roles) {
                roleDTOList.add(new RoleDTO(role.getRoleId(), role.getRoleName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleDTOList;
    }

    @Override
    public RoleDTO getByRoleName(String roleName) {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            // FIX 3: Corrected the HQL query to search for the role by name
            Query<Role> query = session.createQuery("FROM Role WHERE roleName = :roleName", Role.class);
            query.setParameter("roleName", roleName);
            Role role = query.uniqueResult();
            if (role != null) {
                return new RoleDTO(role.getRoleId(), role.getRoleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}