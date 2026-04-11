package studiokita.gui;

import studiokita.model.*;
import studiokita.DataStore;
import studiokita.Navigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/** Panel CRUD Sewa Alat — input data customer + sewa alat. */
public class SewaPanel extends JPanel {

    private final Navigator nav;

    // ── Tabel ─────────────────────────────────────────────────
    private JTable            tabel;
    private DefaultTableModel model;

    // ── Form ──────────────────────────────────────────────────
    private JTextField        txtNama;
    private JTextField        txtTelepon;
    private JComboBox<String> cmbAlat;
    private JTextField        txtKamera;
    private JTextField        txtTglMulai;
    private JTextField        txtTglKembali;
    private JLabel            lblBiaya;

    // ── State ─────────────────────────────────────────────────
    private int barisDipilih = -1;

    public SewaPanel(Navigator nav) {
        this.nav = nav;
        setBackground(UIKit.BG);
        setLayout(new BorderLayout());
        add(UIKit.pageHeader("🎒", "Sewa Alat",
                "Input & kelola data penyewaan alat fotografi", UIKit.ORANGE), BorderLayout.NORTH);
        add(buildTengah(), BorderLayout.CENTER);
        add(buildForm(),   BorderLayout.EAST);
        add(buildBottom(), BorderLayout.SOUTH);
    }

    // ── Method Styling Form (Teks Terang, BG Gelap) ───────────
    private void styleInput(JComponent c) {
        c.setBackground(UIKit.FIELD);
        c.setForeground(UIKit.TEXT);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIKit.BORDER),
                new EmptyBorder(6, 10, 6, 10)));
        if (c instanceof JTextField) {
            ((JTextField) c).setCaretColor(UIKit.TEXT);
        }
    }

    // ── Panel Tengah: Tabel ───────────────────────────────────
    private JPanel buildTengah() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(UIKit.BG);
        p.setBorder(new EmptyBorder(16, 20, 10, 10));

        String[] cols = {"ID", "Nama Customer", "Telepon", "Jenis Alat", "Nama Kamera", "Tgl Mulai", "Tgl Kembali", "Total Biaya"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(model);
        UIKit.styleTable(tabel, UIKit.ORANGE);
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiFormDariBaris();
        });

        // ── Tombol CRUD ───────────────────────────────────────
        JPanel tombolRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tombolRow.setBackground(UIKit.BG);

        JButton btnSimpan  = UIKit.btn("💾 Simpan",  UIKit.GREEN);
        JButton btnUpdate  = UIKit.btn("✎ Update",   UIKit.BLUE);
        JButton btnRemove  = UIKit.btn("✕ Hapus",    UIKit.RED);
        // TOMBOL BARU: SET DIKEMBALIKAN (Warna Ungu biar beda)
        JButton btnKembali = UIKit.btn("✔ Set Dikembalikan", new Color(140, 50, 200)); 
        JButton btnReset   = UIKit.btn("↺ Reset",    new Color(65, 65, 95));

        btnSimpan.addActionListener(e  -> aksiSimpan());
        btnUpdate.addActionListener(e  -> aksiUpdate());
        btnRemove.addActionListener(e  -> aksiHapus());
        btnKembali.addActionListener(e -> aksiSetDikembalikan()); // <-- Panggil fungsi baru
        btnReset .addActionListener(e  -> aksiReset());

        tombolRow.add(btnSimpan);
        tombolRow.add(btnUpdate);
        tombolRow.add(btnRemove);
        tombolRow.add(btnKembali); // <-- Masukkan ke panel
        tombolRow.add(btnReset);

        p.add(UIKit.scroll(tabel), BorderLayout.CENTER);
        p.add(tombolRow,           BorderLayout.SOUTH);
        return p;
    }

    // ── Form Panel Kanan ──────────────────────────────────────
    private JPanel buildForm() {
        JPanel p = UIKit.formPanel(270);

        JLabel formTitle = UIKit.heading("Form Sewa Alat", UIKit.ORANGE, UIKit.FONT_H3);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(formTitle);
        p.add(UIKit.gap(16));

        p.add(UIKit.label("Nama Customer *"));
        p.add(UIKit.gap(2));
        txtNama = new JTextField();
        styleInput(txtNama);
        p.add(txtNama);
        p.add(UIKit.gap(5));

        p.add(UIKit.label("No. Telepon"));
        p.add(UIKit.gap(2));
        txtTelepon = new JTextField();
        styleInput(txtTelepon);
        p.add(txtTelepon);
        p.add(UIKit.gap(5));

        UIKit.addSep(p);
        p.add(UIKit.gap(14));

        p.add(UIKit.label("Jenis Alat *"));
        p.add(UIKit.gap(2));
        cmbAlat = UIKit.combo(DataStore.JENIS_ALAT);
        cmbAlat.addActionListener(e -> updateBiaya());
        p.add(cmbAlat);
        p.add(UIKit.gap(5));

        p.add(UIKit.label("Nama Kamera / Alat"));
        p.add(UIKit.gap(2));
        txtKamera = new JTextField();
        styleInput(txtKamera); 
        p.add(txtKamera);
        p.add(UIKit.gap(5));

        p.add(UIKit.label("Tanggal Mulai (yyyy-MM-dd) *"));
        p.add(UIKit.gap(2));
        txtTglMulai = new JTextField();
        txtTglMulai.setText(LocalDate.now().toString());
        styleInput(txtTglMulai); 
        txtTglMulai.getDocument().addDocumentListener(docListener());
        p.add(txtTglMulai);
        p.add(UIKit.gap(5));

        p.add(UIKit.label("Tanggal Kembali (yyyy-MM-dd) *"));
        p.add(UIKit.gap(2));
        txtTglKembali = new JTextField();
        txtTglKembali.setText(LocalDate.now().plusDays(3).toString());
        styleInput(txtTglKembali); 
        txtTglKembali.getDocument().addDocumentListener(docListener());
        p.add(txtTglKembali);
        p.add(UIKit.gap(5));

        UIKit.addSep(p);
        p.add(UIKit.gap(5));
        p.add(UIKit.label("Estimasi Total:"));
        p.add(UIKit.gap(4));
        lblBiaya = UIKit.previewBiaya("Rp 0");
        p.add(lblBiaya);
        p.add(Box.createVerticalGlue());

        updateBiaya();
        return p;
    }

    // ── Bottom Bar ────────────────────────────────────────────
    private JPanel buildBottom() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(UIKit.SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIKit.BORDER),
                new EmptyBorder(10, 20, 10, 20)));

        JButton btnSelesai = UIKit.btn("✔ Selesai → Dashboard", UIKit.GREEN);
        btnSelesai.addActionListener(e -> nav.goTo("dashboard"));

        JButton btnBack = UIKit.btn("← Kembali ke Input Menu", new Color(65, 65, 95));
        btnBack.addActionListener(e -> nav.goTo("inputmenu"));

        p.add(btnSelesai);
        p.add(btnBack);
        return p;
    }

    // ── Aksi CRUD ────────────────────────────────────────────

    private void aksiSimpan() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama customer wajib diisi.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            LocalDate mulai = LocalDate.parse(txtTglMulai.getText().trim());
            LocalDate balik = LocalDate.parse(txtTglKembali.getText().trim());
            
            String usernameAuto = nama.replaceAll("\\s+","").toLowerCase();
            Customer cust = new Customer(usernameAuto, "12345", nama, "-", txtTelepon.getText().trim(), "-");
            
            String jenis = (String) cmbAlat.getSelectedItem();
            double tarif = DataStore.getTarifAlat(jenis);
            
            SewaAlat alat = new SewaAlat(DataStore.nextSewaId(), jenis, txtKamera.getText().trim(), mulai, balik, tarif);
            Transaksi trx = new Transaksi(cust, alat);
            
            DataStore.daftarTransaksi.add(trx);
            DataStore.simpanData();
            
            muatTabel();
            aksiReset();
            JOptionPane.showMessageDialog(this,
                    "Data sewa disimpan!\nTotal: Rp " + String.format("%,.0f", trx.getTotalBiaya()),
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah (yyyy-MM-dd).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiUpdate() {
        if (barisDipilih < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel yang ingin diupdate.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            LocalDate mulai = LocalDate.parse(txtTglMulai.getText().trim());
            LocalDate balik = LocalDate.parse(txtTglKembali.getText().trim());
            String idLayanan = model.getValueAt(barisDipilih, 0).toString();
            
            for (int i = 0; i < DataStore.daftarTransaksi.size(); i++) {
                Transaksi t = DataStore.daftarTransaksi.get(i);
                if (t.getLayanan().getIdLayanan().equals(idLayanan)) {
                    
                    String nama = txtNama.getText().trim();
                    String usernameAuto = nama.replaceAll("\\s+","").toLowerCase();
                    Customer custBaru = new Customer(usernameAuto, "12345", nama, "-", txtTelepon.getText().trim(), "-");
                    
                    String jenis = (String) cmbAlat.getSelectedItem();
                    double tarif = DataStore.getTarifAlat(jenis);
                    
                    SewaAlat alatBaru = new SewaAlat(idLayanan, jenis, txtKamera.getText().trim(), mulai, balik, tarif);
                    
                    // Cek jika alat sebelumnya sudah dikembalikan, bawa datanya ke update
                    if (t.getLayanan() instanceof SewaAlat) {
                        alatBaru.setTglDikembalikan(((SewaAlat) t.getLayanan()).getTglDikembalikan());
                    }

                    Transaksi trxBaru = new Transaksi(custBaru, alatBaru);
                    DataStore.daftarTransaksi.set(i, trxBaru); 
                    break;
                }
            }
            
            DataStore.simpanData(); 
            muatTabel();
            aksiReset();
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah (yyyy-MM-dd).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiHapus() {
        if (barisDipilih < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel yang ingin dihapus.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Hapus data sewa ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            String idLayanan = model.getValueAt(barisDipilih, 0).toString();
            DataStore.daftarTransaksi.removeIf(t -> t.getLayanan().getIdLayanan().equals(idLayanan));
            
            DataStore.simpanData(); 
            muatTabel();
            aksiReset();
        }
    }

    // ── FITUR BARU: SET BARANG DIKEMBALIKAN ───────────────────
    private void aksiSetDikembalikan() {
        if (barisDipilih < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel yang barangnya sudah dikembalikan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String idLayanan = model.getValueAt(barisDipilih, 0).toString();
        
        for (Transaksi t : DataStore.daftarTransaksi) {
            if (t.getLayanan().getIdLayanan().equals(idLayanan) && t.getLayanan() instanceof SewaAlat) {
                SewaAlat alat = (SewaAlat) t.getLayanan();
                
                // Cek apakah sudah pernah di-set dikembalikan
                if (alat.getTglDikembalikan() != null) {
                    JOptionPane.showMessageDialog(this, "Kamera ini sudah dikembalikan pada tanggal: " + alat.getTglDikembalikan(), "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                int ok = JOptionPane.showConfirmDialog(this, "Tandai kamera ini SELESAI / TELAH DIKEMBALIKAN hari ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    // Set tanggal dikembalikan menjadi hari ini
                    alat.setTglDikembalikan(LocalDate.now());
                    
                    DataStore.simpanData(); // Simpan perubahan
                    muatTabel();
                    aksiReset();
                    
                    // Cek apakah dia kena denda atau tidak
                    if (alat.hitungHariTelat() > 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Pelanggan terlambat mengembalikan selama " + alat.hitungHariTelat() + " hari!\n" +
                            "Total Denda: Rp " + String.format("%,.0f", alat.hitungTotalDenda()) + "\n" +
                            "Sistem telah menambahkan denda ini ke Total Biaya.", 
                            "Peringatan Denda", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Barang dikembalikan tepat waktu. Tidak ada denda.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                break;
            }
        }
    }

    private void aksiReset() {
        txtNama.setText("");
        txtTelepon.setText("");
        cmbAlat.setSelectedIndex(0);
        txtKamera.setText("");
        txtTglMulai.setText(LocalDate.now().toString());
        txtTglKembali.setText(LocalDate.now().plusDays(3).toString());
        barisDipilih = -1;
        tabel.clearSelection();
        updateBiaya();
    }

    private void isiFormDariBaris() {
        barisDipilih = tabel.getSelectedRow();
        if (barisDipilih < 0) return;
        
        String idLayanan = model.getValueAt(barisDipilih, 0).toString();
        
        for (Transaksi t : DataStore.daftarTransaksi) {
            if (t.getLayanan().getIdLayanan().equals(idLayanan) && t.getLayanan() instanceof SewaAlat) {
                SewaAlat alat = (SewaAlat) t.getLayanan();
                
                txtNama.setText(t.getCustomer().getNamaLengkap());
                txtTelepon.setText(t.getCustomer().getNoTelepon());
                
                cmbAlat.setSelectedItem(alat.getJenisAlat());
                txtKamera.setText(alat.getNamaKamera());
                txtTglMulai.setText(alat.getTglMulai().toString());
                txtTglKembali.setText(alat.getTglKembali().toString());
                updateBiaya();
                break;
            }
        }
    }

    private double hitungBiayaSewa() {
        try {
            LocalDate mulai = LocalDate.parse(txtTglMulai.getText().trim());
            LocalDate balik = LocalDate.parse(txtTglKembali.getText().trim());
            long hari = Math.max(0, ChronoUnit.DAYS.between(mulai, balik));
            if (hari == 0) hari = 1;
            double tarif = DataStore.getTarifAlat((String) cmbAlat.getSelectedItem());
            return hari * tarif;
        } catch (Exception e) { return 0; }
    }

    private void updateBiaya() {
        double b = hitungBiayaSewa();
        lblBiaya.setText("Rp " + String.format("%,.0f", b));
    }

    private void muatTabel() {
        model.setRowCount(0);
        for (Transaksi r : DataStore.daftarTransaksi) {
            if (r.getLayanan() instanceof SewaAlat) {
                SewaAlat alat = (SewaAlat) r.getLayanan();
                
                // Tambahkan teks kalau sudah dikembalikan
                String statusKembali = alat.getTglKembali().toString();
                if (alat.getTglDikembalikan() != null) {
                    statusKembali += " (Dikembalikan: " + alat.getTglDikembalikan() + ")";
                }

                model.addRow(new Object[]{
                        alat.getIdLayanan(), 
                        r.getCustomer().getNamaLengkap(), 
                        r.getCustomer().getNoTelepon(),   
                        alat.getJenisAlat(),
                        alat.getNamaKamera(), 
                        alat.getTglMulai(), 
                        statusKembali, // <-- Menampilkan status di tabel
                        "Rp " + String.format("%,.0f", r.getTotalBiaya())
                });
            }
        }
    }

    public void refresh() { muatTabel(); }

    private javax.swing.event.DocumentListener docListener() {
        return new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { updateBiaya(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { updateBiaya(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateBiaya(); }
        };
    }
}