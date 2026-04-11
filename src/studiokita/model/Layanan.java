package studiokita.model;

public abstract class Layanan implements java.io.Serializable{
    private String idLayanan;

    public Layanan(String idLayanan) {
        this.idLayanan = idLayanan;
    }
    
    public String getIdLayanan() { return idLayanan; }

    // Polymorphism: Method yang wajib di-override oleh kelas anak
    public abstract double hitungBiaya();
    public abstract String getDeskripsi();
}