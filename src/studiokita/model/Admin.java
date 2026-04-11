package studiokita.model;

public class Admin extends User {
    private String adminLevel;

    public Admin(String username, String password, String namaLengkap, String email, String adminLevel) {
        // Memanggil constructor dari class User dengan Role "ADMIN"
        super(username, password, namaLengkap, email, "ADMIN");
        this.adminLevel = adminLevel;
    }

    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }

    @Override
    public void displayRole() {
        System.out.println("=== ROLE : ADMIN ===");
        System.out.println("Nama Admin : " + getNamaLengkap());
        System.out.println("Level Admin : " + adminLevel);
    }

    @Override
    public void displayInfo() {
        System.out.println("\n=== INFORMASI ADMIN ===");
        System.out.println("Username: " + getUsername());
        System.out.println("Nama Lengkap: " + getNamaLengkap());
        System.out.println("Email: " + getEmail());
        System.out.println("Level: " + adminLevel);
    }

    @Override
    public boolean login(String inputUsername, String inputPassword) {
        boolean isValid = super.login(inputUsername, inputPassword);
        if (isValid) {
            System.out.println("Login Admin berhasil!! Selamat datang, " + getNamaLengkap());
        } else {
            System.out.println("Login Admin gagal, username atau password salah.");
        }
        return isValid;
    }
}