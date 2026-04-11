package studiokita;

import studiokita.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    // ── DATABASE LOKAL: List Users dan List Transaksi ──────────
    public static List<User> daftarUser = new ArrayList<>();
    public static List<Transaksi> daftarTransaksi = new ArrayList<>();

    // ── Sesi Login Aktif ───────────────────────────────────────
    public static User userLoginSekarang = null;

    private static int sewaCounter = 1;
    private static int jasaCounter = 1;

    public static String nextSewaId() {
        return String.format("SW%03d", sewaCounter++);
    }

    public static String nextJasaId() {
        return String.format("JF%03d", jasaCounter++);
    }

    // ── Tarif Sewa Alat ───────────────────────────────────────
    public static final String[] JENIS_ALAT = {
        "Kamera DSLR", "Kamera Mirrorless", "Lensa Wide", "Lensa Tele",
        "Flash Eksternal", "Tripod", "Softbox", "Background Studio", "Drone"
    };
    public static final double[] TARIF_ALAT = {
        250_000, 300_000, 150_000, 175_000,
        75_000, 50_000, 80_000, 60_000, 400_000
    };

    public static double getTarifAlat(String jenis) {
        for (int i = 0; i < JENIS_ALAT.length; i++) {
            if (JENIS_ALAT[i].equals(jenis)) {
                return TARIF_ALAT[i];
            }
        }
        return 0;
    }

    // ── Tarif Jasa Foto ───────────────────────────────────────
    public static final String[] PAKET_FOTO = {
        "Paket Prewedding", "Paket Wedding", "Paket Wisuda",
        "Paket Keluarga", "Paket Pasfoto / ID", "Paket Produk"
    };
    public static final double[] HARGA_PAKET = {
        1_500_000, 3_500_000, 750_000,
        500_000, 150_000, 850_000
    };

    public static double getHargaDasarPaket(String paket) {
        for (int i = 0; i < PAKET_FOTO.length; i++) {
            if (PAKET_FOTO[i].equals(paket)) {
                return HARGA_PAKET[i];
            }
        }
        return 0;
    }

    // ── SISTEM LOGIN ──────────────────────────────────────────
    public static void inisialisasiDataSistem() {
        if (daftarUser.isEmpty()) {
            // Akun Default jika database masih kosong
            daftarUser.add(new Admin("admin", "admin123", "Budi Admin", "admin@studio.com", "SUPER_ADMIN"));
            daftarUser.add(new Customer("user", "user123", "Siti Pelanggan", "siti@mail.com", "0812345678", "Jl. Melati 1"));
        }
    }

    public static boolean cekLogin(String username, String password) {
        // --- TAMBAHAN PELINDUNG ---
        // Jika daftar user kosong (misal karena file datanya baru dihapus),
        // paksa aplikasi membuat akun admin default!
        if (daftarUser.isEmpty()) {
            inisialisasiDataSistem();
        }

        // Cek kecocokan username dan password
        for (User u : daftarUser) {
            if (u.login(username, password)) {
                userLoginSekarang = u;
                return true;
            }
        }
        return false;
    }

    public static void logout() {
        userLoginSekarang = null;
    }

    // ── FITUR DATABASE (SERIALIZATION) ────────────────────────
    public static void simpanData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data_studio.dat"))) {
            oos.writeObject(daftarTransaksi);
            oos.writeObject(daftarUser); // Menyimpan akun user
        } catch (Exception e) {
            System.out.println("Gagal menyimpan data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void muatData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data_studio.dat"))) {
            daftarTransaksi = (ArrayList<Transaksi>) ois.readObject();
            daftarUser = (ArrayList<User>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Membuat database lokal baru...");
            inisialisasiDataSistem(); // Buat akun default jika file baru dibikin
        }
    }
}
