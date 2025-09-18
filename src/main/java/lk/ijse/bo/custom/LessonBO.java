package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.LessonDTO;

import java.util.List;

public interface LessonBO extends SuperBO {
    boolean saveLesson(LessonDTO dto) throws Exception;
    boolean updateLesson(LessonDTO dto) throws Exception;
    boolean deleteLesson(int id) throws Exception;
    List<LessonDTO> getAllLessons() throws Exception;
    LessonDTO getLesson(int id) throws Exception;
}