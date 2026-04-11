package studiokita.model;

public class JasaFoto extends Layanan implements java.io.Serializable{
    private String paket;
    private int durasiJam;
    private int jumlahFotoEdit;
    private String fotografer;
    private String tglSesi;
    private double hargaDasar;

    public JasaFoto(String id, String paket, int durasi, int fotoEdit, String fotografer, String tglSesi, double harga) {
        super(id);
        this.paket = paket;
        this.durasiJam = durasi;
        this.jumlahFotoEdit = fotoEdit;
        this.fotografer = fotografer;
        this.tglSesi = tglSesi;
        this.hargaDasar = harga;
    }

    @Override
    public double hitungBiaya() {
        return hargaDasar + (durasiJam * 100_000) + (jumlahFotoEdit * 15_000);
    }

    @Override
    public String getDeskripsi() {
        return paket + " | " + durasiJam + " Jam | Fotografer: " + fotografer;
    }

    public String getPaket() { return paket; }
    public int getDurasiJam() { return durasiJam; }
    public String getTglSesi() { return tglSesi; }
    public int getJumlahFotoEdit() { return jumlahFotoEdit; }
    public String getFotografer() { return fotografer; } // <- Ini yang bikin error tadi
}