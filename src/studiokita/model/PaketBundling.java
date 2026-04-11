package studiokita.model;

import java.io.Serializable;

/**
 * ============================================================
 * SUBCLASS: PaketBundling extends Layanan
 * Konsep: Inheritance + Polymorphism
 * ============================================================
 */
public class PaketBundling extends Layanan implements Serializable {

    // ── ENUM JENIS PAKET ─────────────────────────────────────
    public enum JenisPaket {
        WEDDING_FULL("Wedding Full Package", 5_000_000),
        PREWEDDING_BASIC("Prewedding Package", 2_000_000),
        CONTENT_CREATOR("Content Creator Package", 1_500_000),
        GRADUATION_PACK("Graduation Package", 1_000_000),
        EVENT_DOC("Event Documentation", 3_000_000);

        private final String namaPaket;
        private final double hargaDasar;

        JenisPaket(String namaPaket, double hargaDasar) {
            this.namaPaket = namaPaket;
            this.hargaDasar = hargaDasar;
        }

        public String getNamaPaket() { return namaPaket; }
        public double getHargaDasar() { return hargaDasar; }
    }

    // ── ATRIBUT ──────────────────────────────────────────────
    private JenisPaket jenisPaket;
    private int durasiHari;
    private boolean includeVideo;
    private boolean includeCetak;
    private int jumlahFile;

    // ── CONSTRUCTOR (Sudah disesuaikan dengan class Layanan aslimu)
    public PaketBundling(String id, JenisPaket jenisPaket, int durasiHari, boolean includeVideo, boolean includeCetak, int jumlahFile) {
        super(id); // Wajib panggil constructor bapaknya
        this.jenisPaket = jenisPaket;
        this.durasiHari = durasiHari;
        this.includeVideo = includeVideo;
        this.includeCetak = includeCetak;
        this.jumlahFile = jumlahFile;
    }

    // ── GETTER & SETTER ──────────────────────────────────────
    public JenisPaket getJenisPaket() { return jenisPaket; }
    public void setJenisPaket(JenisPaket jenisPaket) { this.jenisPaket = jenisPaket; }

    public int getDurasiHari() { return durasiHari; }
    public void setDurasiHari(int durasiHari) { this.durasiHari = durasiHari; }

    public boolean isIncludeVideo() { return includeVideo; }
    public void setIncludeVideo(boolean includeVideo) { this.includeVideo = includeVideo; }

    public boolean isIncludeCetak() { return includeCetak; }
    public void setIncludeCetak(boolean includeCetak) { this.includeCetak = includeCetak; }

    public int getJumlahFile() { return jumlahFile; }
    public void setJumlahFile(int jumlahFile) { this.jumlahFile = jumlahFile; }

    // ── PERHITUNGAN (Polymorphism) ───────────────────────────
    @Override
    public double hitungBiaya() {
        if (jenisPaket == null) return 0;

        double total = jenisPaket.getHargaDasar();
        total += durasiHari * 200_000;

        if (includeVideo) total += 500_000;
        if (includeCetak) total += 300_000;

        return total;
    }

    // ── DETAIL OUTPUT (Diganti jadi getDeskripsi) ────────────
    @Override
    public String getDeskripsi() {
        String nama = (jenisPaket != null) ? jenisPaket.getNamaPaket() : "Unknown";
        String videoStr = includeVideo ? "Ya" : "Tidak";
        String cetakStr = includeCetak ? "Ya" : "Tidak";
        
        return "Paket Bundling: " + nama + " | Durasi: " + durasiHari + " hari | Video: " + videoStr + " | Cetak: " + cetakStr;
    }
}