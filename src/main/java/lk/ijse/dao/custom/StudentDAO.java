package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.dto.StudentDTO;

import java.util.List;
import java.util.Map;

public interface StudentDAO extends SuperDAO {
    boolean save(StudentDTO dto);
    boolean update(StudentDTO dto);
    boolean delete(Integer id);
    StudentDTO get(Integer id);
    List<StudentDTO> getAll();
    List<StudentDTO> getStudentsRegisteredInAllCourses();
    Map<StudentDTO, List<String>> getStudentsWithCourses() throws Exception;
}