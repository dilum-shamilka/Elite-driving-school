package lk.ijse.dto;

public class UserDTO {
    private int userId;
    private String username;
    private String email;
    private String password;
    private RoleDTO role; // Changed to RoleDTO
    private String status;

    public UserDTO() {
    }

    public UserDTO(int userId, String username, String email, String password, RoleDTO role, String status) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "userId=" + userId + ", username='" + username + '\'' + ", email='" + email + '\'' + ", password='" + password + '\'' + ", role='" + (role != null ? role.getRoleName() : "null") + '\'' + ", status='" + status + '\'' + '}';
    }
}