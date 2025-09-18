package lk.ijse.bo.impl;

import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.StudentDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentBOImpl implements StudentBO {

    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.STUDENT);

    @Override
    public boolean saveStudent(StudentDTO dto) throws Exception {
        return studentDAO.save(dto);
    }

    @Override
    public boolean updateStudent(StudentDTO dto) throws Exception {
        return studentDAO.update(dto);
    }

    @Override
    public boolean deleteStudent(int id) throws Exception {
        return studentDAO.delete(id);
    }

    @Override
    public List<StudentDTO> getAllStudents() throws Exception {
        return studentDAO.getAll();
    }

    @Override
    public StudentDTO getStudent(int id) throws Exception {
        return studentDAO.get(id);
    }

    @Override
    public List<String> getAllStudentIds() throws Exception {
        List<StudentDTO> allStudents = getAllStudents();
        return allStudents.stream()
                .map(student -> String.valueOf(student.getStudentId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDTO> getStudentsRegisteredInAllCourses() throws Exception {
        return studentDAO.getStudentsRegisteredInAllCourses();
    }

    @Override
    public Map<StudentDTO, List<String>> getStudentsWithCourses() throws Exception {
        return studentDAO.getStudentsWithCourses();
    }
}