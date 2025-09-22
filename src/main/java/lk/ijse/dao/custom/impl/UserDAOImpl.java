package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SuperDAO;
import lk.ijse.dao.custom.UserDAO;
import lk.ijse.dto.RoleDTO;
import lk.ijse.dto.UserDTO;
import lk.ijse.entity.User;
import lk.ijse.entity.Role;
import lk.ijse.configaration.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO, SuperDAO {

    @Override
    public boolean save(UserDTO dto) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            Role role = session.get(Role.class, dto.getRole().getRoleId());
            if (role == null) {
                return false;
            }
            User user = new User(dto.getUsername(), dto.getEmail(), dto.getPassword(), role, dto.getStatus());
            session.persist(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(UserDTO dto) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, dto.getUserId());
            if (user != null) {
                user.setUsername(dto.getUsername());
                user.setEmail(dto.getEmail());
                user.setPassword(dto.getPassword());

                Role role = session.get(Role.class, dto.getRole().getRoleId());
                if (role == null) {
                    return false;
                }
                user.setRole(role);
                user.setStatus(dto.getStatus());
                session.merge(user);
            }
            transaction.commit();
            return user != null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean delete(Integer userId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
            return user != null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public UserDTO get(Integer userId) {
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            User user = session.get(User.class, userId);
            if (user != null) {
                return new UserDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword(),
                        new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName()),
                        user.getStatus()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    @Override
    public List<UserDTO> getAll() {
        Session session = null;
        List<UserDTO> userDTOList = new ArrayList<>();
        try {
            session = FactoryConfiguration.getInstance().getSession();
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> users = query.list();
            for (User user : users) {
                userDTOList.add(new UserDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword(),
                        new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName()),
                        user.getStatus()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return userDTOList;
    }
}