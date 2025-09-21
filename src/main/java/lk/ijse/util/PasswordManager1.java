package lk.ijse.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager1 {

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    public static void main(String[] args) {
        String passwordToHash = "1";
        String hashedPassword = hashPassword(passwordToHash);
        System.out.println("Hashed password for '1': " + hashedPassword);
    }
}