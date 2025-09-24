package lk.ijse.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lessonid")   // ✅ use snake_case for DB column
    private Integer lessonId;

    @Column(name = "date")
    private Date date;

    @Column(name = "time")
    private String time;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public Lesson() {}

    public Lesson(Date date, String time, String status, Instructor instructor, Course course, Student student) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.instructor = instructor;
        this.course = course;
        this.student = student;
    }

    // Getters and Setters
    public Integer getLessonId() { return lessonId; }
    public void setLessonId(Integer lessonId) { this.lessonId = lessonId; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
