package lk.ijse.dto;

import java.util.Date;

public class PaymentDTO {
    private int paymentId;
    private double amount;
    private Date date;
    private String status;
    private int studentId;
    private int lessonId; // Add this line

    public PaymentDTO() {}

    // Update the constructor to include lessonId
    public PaymentDTO(int paymentId, double amount, Date date, String status, int studentId, int lessonId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.studentId = studentId;
        this.lessonId = lessonId;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    // Add the new getter and setter for lessonId
    public int getLessonId() { return lessonId; }
    public void setLessonId(int lessonId) { this.lessonId = lessonId; }
}