package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.CourseDTO;

import java.util.List;

public interface CourseBO extends SuperBO {
    boolean saveCourse(CourseDTO dto) throws Exception;
    boolean updateCourse(CourseDTO dto) throws Exception;
    boolean deleteCourse(int id) throws Exception;
    List<CourseDTO> getAllCourses() throws Exception;
    CourseDTO getCourse(int id) throws Exception;
}