package lk.ijse.dto;

public class InstructorDTO {
    private int instructorId;
    private String name;
    private String email;
    private String phone;
    private String availability;
    private String specialization;

    public InstructorDTO() {
    }

    public InstructorDTO(int instructorId, String name, String email, String phone, String availability, String specialization) {
        this.instructorId = instructorId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.availability = availability;
        this.specialization = specialization;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}