package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.InstructorDTO;

import java.util.List;

public interface InstructorBO extends SuperBO {
    boolean saveInstructor(InstructorDTO dto) throws Exception;
    boolean updateInstructor(InstructorDTO dto) throws Exception;
    boolean deleteInstructor(int id) throws Exception;
    List<InstructorDTO> getAllInstructors() throws Exception;
    InstructorDTO getInstructor(int id) throws Exception;
}