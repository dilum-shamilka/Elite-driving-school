package lk.ijse.bo.impl;

import lk.ijse.bo.custom.InstructorBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.InstructorDAO;
import lk.ijse.dto.InstructorDTO;

import java.util.List;

public class InstructorBOImpl implements InstructorBO {

    private final InstructorDAO instructorDAO = (InstructorDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.INSTRUCTOR);

    @Override
    public boolean saveInstructor(InstructorDTO dto) throws Exception {
        return instructorDAO.save(dto);
    }

    @Override
    public boolean updateInstructor(InstructorDTO dto) throws Exception {
        return instructorDAO.update(dto);
    }

    @Override
    public boolean deleteInstructor(int id) throws Exception {
        return instructorDAO.delete(id);
    }

    @Override
    public List<InstructorDTO> getAllInstructors() throws Exception {
        return instructorDAO.getAll();
    }

    @Override
    public InstructorDTO getInstructor(int id) throws Exception {
        return instructorDAO.get(id);
    }
}