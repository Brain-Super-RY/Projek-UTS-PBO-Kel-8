package studiokita.model;

public class Customer extends User {
    private String noTelepon;
    private String alamat;

    public Customer(String username, String password, String namaLengkap, String email, String noTelepon, String alamat) {
        // Memanggil constructor dari class User dengan Role "CUSTOMER"
        super(username, password, namaLengkap, email, "CUSTOMER");
        this.noTelepon = noTelepon;
        this.alamat = alamat;
    }

    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    @Override
    public void displayRole() {
        System.out.println("=== ROLE : PELANGGAN ===");
        System.out.println("Nama Pelanggan : " + getNamaLengkap());
    }

    @Override
    public void displayInfo() {
        System.out.println("\n=== INFORMASI CUSTOMER ===");
        System.out.println("Username: " + getUsername());
        System.out.println("Nama Lengkap: " + getNamaLengkap());
        System.out.println("Email: " + getEmail());
        System.out.println("No Telepon: " + noTelepon);
        System.out.println("Alamat: " + alamat);
    }
}