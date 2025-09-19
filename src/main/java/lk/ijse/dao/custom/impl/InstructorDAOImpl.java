package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SuperDAO;
import lk.ijse.dao.custom.InstructorDAO;
import lk.ijse.dto.InstructorDTO;
import lk.ijse.entity.Instructor;
import lk.ijse.configaration.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class InstructorDAOImpl implements InstructorDAO, SuperDAO {

    @Override
    public boolean save(InstructorDTO dto) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            Instructor instructor = new Instructor(dto.getName(), dto.getEmail(), dto.getPhone(), dto.getAvailability(), dto.getSpecialization());
            session.persist(instructor);
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
    public boolean update(InstructorDTO dto) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            Instructor instructor = session.get(Instructor.class, dto.getInstructorId());
            if (instructor != null) {
                instructor.setName(dto.getName());
                instructor.setEmail(dto.getEmail());
                instructor.setPhoneNumber(dto.getPhone());
                instructor.setAvailability(dto.getAvailability());
                instructor.setSpecialization(dto.getSpecialization());
                session.merge(instructor);
            }
            transaction.commit();
            return instructor != null;
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
    public boolean delete(Integer id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();
            // FIX: Use the 'id' parameter instead of a non-existent 'dto' object
            Instructor instructor = session.get(Instructor.class, id);
            if (instructor != null) {
                session.remove(instructor);
            }
            transaction.commit();
            return instructor != null;
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
    public InstructorDTO get(Integer id) {
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            Instructor instructor = session.get(Instructor.class, id);
            if (instructor != null) {
                return new InstructorDTO(
                        instructor.getInstructorId(),
                        instructor.getName(),
                        instructor.getEmail(),
                        instructor.getPhoneNumber(),
                        instructor.getAvailability(),
                        instructor.getSpecialization()
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
    public List<InstructorDTO> getAll() {
        Session session = null;
        List<InstructorDTO> instructorDTOList = new ArrayList<>();
        try {
            session = FactoryConfiguration.getInstance().getSession();
            Query<Instructor> query = session.createQuery("FROM Instructor", Instructor.class);
            List<Instructor> instructors = query.list();
            for (Instructor instructor : instructors) {
                instructorDTOList.add(new InstructorDTO(
                        instructor.getInstructorId(),
                        instructor.getName(),
                        instructor.getEmail(),
                        instructor.getPhoneNumber(),
                        instructor.getAvailability(),
                        instructor.getSpecialization()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return instructorDTOList;
    }
}