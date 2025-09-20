package lk.ijse.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager2 {

    /**
     * Hashes a plain text password using BCrypt.
     *
     * @param plainTextPassword The password to hash.
     * @return The hashed password.
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Checks a plain text password against a hashed password.
     *
     * @param plainTextPassword The password to check.
     * @param hashedPassword The hashed password to compare against.
     * @return true if the passwords match, false otherwise.
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    /**
     * Main method for generating a sample hashed password.
     * You can use this to generate a hash for a password (e.g., '1')
     * and paste it into the LoginController for testing.
     */
    public static void main(String[] args) {
        String passwordToHash = "123";
        String hashedPassword = hashPassword(passwordToHash);
        System.out.println("Hashed password for '123': " + hashedPassword);
    }
}