package studiokita.model;

import java.time.LocalDate;


public class Transaksi implements java.io.Serializable {
    private Customer customer;
    private Layanan layanan; // Bisa SewaAlat ATAU JasaFoto
    private LocalDate tglInput;

    public Transaksi(Customer customer, Layanan layanan) {
        this.customer = customer;
        this.layanan = layanan;
        this.tglInput = LocalDate.now();
    }

    public Customer getCustomer() { return customer; }
    public Layanan getLayanan() { return layanan; }
    public LocalDate getTglInput() { return tglInput; }
    
    // Memanggil metode polymorphism dari Layanan
    public double getTotalBiaya() { return layanan.hitungBiaya(); }
}