package studiokita.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SewaAlat extends Layanan implements java.io.Serializable {
    private String jenisAlat;
    private String namaKamera;
    private LocalDate tglMulai;
    private LocalDate tglKembali; // Tanggal rencana kembali
    private LocalDate tglDikembalikan; // Tanggal aktual kamera dikembalikan (bisa kosong)
    private double tarifPerHari;
    
    // Kita tetapkan denda keterlambatan Rp 50.000 per hari
    private static final double DENDA_PER_HARI = 50000; 

    public SewaAlat(String id, String jenis, String namaKamera, LocalDate mulai, LocalDate kembali, double tarif) {
        super(id);
        this.jenisAlat = jenis;
        this.namaKamera = namaKamera;
        this.tglMulai = mulai;
        this.tglKembali = kembali;
        this.tarifPerHari = tarif;
        this.tglDikembalikan = null; // Default: kamera belum dikembalikan
    }

    // ── LOGIKA DENDA KETERLAMBATAN ─────────────────────────────
    public long hitungHariTelat() {
        LocalDate tanggalPatokan;
        
        // Jika belum dikembalikan, patokannya adalah hari ini
        if (tglDikembalikan == null) {
            tanggalPatokan = LocalDate.now();
        } else {
            // Jika sudah dikembalikan, patokannya adalah tanggal dikembalikan
            tanggalPatokan = tglDikembalikan;
        }

        // Jika patokan lebih dari tanggal rencana kembali, berarti telat!
        if (tanggalPatokan.isAfter(tglKembali)) {
            return ChronoUnit.DAYS.between(tglKembali, tanggalPatokan);
        }
        return 0; // Tidak telat
    }

    public double hitungTotalDenda() {
        return hitungHariTelat() * DENDA_PER_HARI;
    }

    // ── PERHITUNGAN BIAYA (Polymorphism) ───────────────────────
    @Override
    public double hitungBiaya() {
        // Hitung biaya sewa normal
        long hariSewa = ChronoUnit.DAYS.between(tglMulai, tglKembali);
        if (hariSewa <= 0) hariSewa = 1;
        double biayaNormal = hariSewa * tarifPerHari;
        
        // Tambahkan dengan denda (kalau ada)
        return biayaNormal + hitungTotalDenda();
    }

    @Override
    public String getDeskripsi() {
        String teks = jenisAlat + " (" + namaKamera + ") | " + tglMulai + " s/d " + tglKembali;
        if (hitungHariTelat() > 0) {
            teks += " [TELAT " + hitungHariTelat() + " HARI]";
        }
        return teks;
    }

    // ── GETTER & SETTER ────────────────────────────────────────
    public String getJenisAlat() { return jenisAlat; }
    public String getNamaKamera() { return namaKamera; } 
    public LocalDate getTglMulai() { return tglMulai; }
    public LocalDate getTglKembali() { return tglKembali; }
    
    // Untuk mengupdate saat kamera benar-benar dikembalikan
    public LocalDate getTglDikembalikan() { return tglDikembalikan; }
    public void setTglDikembalikan(LocalDate tglDikembalikan) { this.tglDikembalikan = tglDikembalikan; }
}