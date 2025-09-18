package lk.ijse.dto.tm;

public class CourseTM {
    private int courseId;
    private String name;
    private int duration;
    private double fee;
    private int instructorId;
    private String instructorName;

    public CourseTM() {
    }

    public CourseTM(int courseId, String name, int duration, double fee, int instructorId, String instructorName) {
        this.courseId = courseId;
        this.name = name;
        this.duration = duration;
        this.fee = fee;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
    }

    // Getters and Setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
}