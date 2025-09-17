package lk.ijse.dto;

import java.util.Date;

public class LessonDTO {
    private int lessonId;
    private Date date;
    private String time;
    private String status;
    private int instructorId;
    private int courseId;
    private int studentId;

    public LessonDTO() {}

    public LessonDTO(int lessonId, Date date, String time, String status, int instructorId, int courseId, int studentId) {
        this.lessonId = lessonId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.instructorId = instructorId;
        this.courseId = courseId;
        this.studentId = studentId;
    }

    // Getters and Setters
    public int getLessonId() { return lessonId; }
    public void setLessonId(int lessonId) { this.lessonId = lessonId; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
}