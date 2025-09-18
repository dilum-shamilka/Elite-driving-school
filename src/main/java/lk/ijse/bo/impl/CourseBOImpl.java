package lk.ijse.bo.impl;

import lk.ijse.bo.custom.CourseBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.dto.CourseDTO;

import java.util.List;

public class CourseBOImpl implements CourseBO {
    private final CourseDAO courseDAO = (CourseDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.COURSE);

    @Override
    public boolean saveCourse(CourseDTO dto) throws Exception {
        return courseDAO.save(dto);
    }

    @Override
    public boolean updateCourse(CourseDTO dto) throws Exception {
        return courseDAO.update(dto);
    }

    @Override
    public boolean deleteCourse(int id) throws Exception {
        return courseDAO.delete(id);
    }

    @Override
    public List<CourseDTO> getAllCourses() throws Exception {
        return courseDAO.getAll();
    }

    @Override
    public CourseDTO getCourse(int id) throws Exception {
        return courseDAO.get(id);
    }
}