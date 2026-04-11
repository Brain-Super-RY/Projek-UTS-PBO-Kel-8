package studiokita.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    // Encapsulation: atribut di-private
    private String username;
    private String password;
    private String namaLengkap;
    private String email;
    private String role; // Untuk membedakan ADMIN atau CUSTOMER

    public User() {}

    public User(String username, String password, String namaLengkap, String email, String role) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.role = role;
    }

    // Getter & Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Method Login
    public boolean login(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    // Method Abstract (Harus di-override di Admin dan Customer)
    public abstract void displayRole();
    
    public void displayInfo() {
        System.out.println("Nama Lengkap: " + namaLengkap);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
    }
}