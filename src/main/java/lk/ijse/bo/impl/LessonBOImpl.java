package lk.ijse.bo.impl;

import lk.ijse.bo.custom.LessonBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.LessonDAO;
import lk.ijse.dto.LessonDTO;

import java.util.List;

public class LessonBOImpl implements LessonBO {

    private final LessonDAO lessonDAO = (LessonDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.LESSON);

    @Override
    public boolean saveLesson(LessonDTO dto) throws Exception {
        return lessonDAO.save(dto);
    }

    @Override
    public boolean updateLesson(LessonDTO dto) throws Exception {
        return lessonDAO.update(dto);
    }

    @Override
    public boolean deleteLesson(int id) throws Exception {
        return lessonDAO.delete(id);
    }

    @Override
    public List<LessonDTO> getAllLessons() throws Exception {
        return lessonDAO.getAll();
    }

    @Override
    public LessonDTO getLesson(int id) throws Exception {
        return lessonDAO.get(id);
    }
}