package lk.ijse.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private Date date;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lessonid", referencedColumnName = "lessonid", nullable = false) // ✅ FIX
    private Lesson lesson;

    public Payment() {}

    public Payment(Double amount, Date date, String status, Student student, Lesson lesson) {
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.student = student;
        this.lesson = lesson;
    }

    // Getters and Setters
    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }
}
