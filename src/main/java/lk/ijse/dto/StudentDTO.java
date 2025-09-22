package lk.ijse.dto;

import java.sql.Date;
import java.util.Objects;

public class StudentDTO {
    private int studentId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Date dob;

    public StudentDTO() {
    }

    public StudentDTO(int studentId, String name, String email, String phone, String address, Date dob) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
    }


    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
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
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return studentId == that.studentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}