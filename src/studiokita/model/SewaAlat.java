package studiokita.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SewaAlat extends Layanan implements java.io.Serializable {
    private String jenisAlat;
    private String namaKamera;
    private LocalDate tglMulai;
    private LocalDate tglKembali;
    private double tarifPerHari;

    public SewaAlat(String id, String jenis, String namaKamera, LocalDate mulai, LocalDate kembali, double tarif) {
        super(id);
        this.jenisAlat = jenis;
        this.namaKamera = namaKamera; // <-- Disesuaikan
        this.tglMulai = mulai;
        this.tglKembali = kembali;
        this.tarifPerHari = tarif;
    }

    @Override
    public double hitungBiaya() {
        long hari = ChronoUnit.DAYS.between(tglMulai, tglKembali);
        if (hari <= 0) hari = 1;
        return hari * tarifPerHari;
    }

    @Override
    public String getDeskripsi() {
        return jenisAlat + " (Kamera: " + namaKamera + ") | " + tglMulai + " s/d " + tglKembali;
    }

    public String getJenisAlat() { return jenisAlat; }
    
    public String getNamaKamera() { return namaKamera; } 
    
    public LocalDate getTglMulai() { return tglMulai; }
    public LocalDate getTglKembali() { return tglKembali; }
}