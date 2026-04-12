package studiokita.gui;

import studiokita.DataStore;
import studiokita.Navigator;
import studiokita.model.*; 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class JasaPanel extends JPanel {

    private final Navigator nav;

    private JTable            tabel;
    private DefaultTableModel model;

    private JTextField        txtNama;
    private JTextField        txtTelepon;
    private JComboBox<String> cmbPaket;
    private JTextField        txtFotografer;
    private JTextField        txtTglSesi;
    private JSpinner          spnDurasi;
    private JSpinner          spnFotoEdit;
    private JLabel            lblBiaya;

    private int barisDipilih = -1;

    public JasaPanel(Navigator nav) {
        this.nav = nav;
        setBackground(UIKit.BG);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIKit.SURFACE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIKit.BORDER),
                new EmptyBorder(16, 28, 16, 28)));

        JPanel titleArea = new JPanel();
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));
        titleArea.setOpaque(false);

        JLabel lblTitle = new JLabel("📸 JASA FOTO");
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        lblTitle.setForeground(UIKit.BLUE);

        JLabel lblDesc = new JLabel("Input & kelola booking sesi foto pelanggan");
        lblDesc.setFont(UIKit.FONT_NORMAL);
        lblDesc.setForeground(UIKit.MUTED);

        titleArea.add(lblTitle);
        titleArea.add(UIKit.gap(4));
        titleArea.add(lblDesc);
        
        headerPanel.add(titleArea, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
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
        } else if (c instanceof JSpinner) {
            // ── TAMBAHAN KHUSUS UNTUK JSPINNER (Angka) ──
            // Mengambil text field di dalam JSpinner untuk diubah warnanya
            JSpinner spinner = (JSpinner) c;
            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
                textField.setForeground(Color.BLACK);
                textField.setBackground(UIKit.FIELD);
                textField.setCaretColor(Color.BLACK);
            }
        }
    }

    private JPanel buildTengah() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(UIKit.BG);
        p.setBorder(new EmptyBorder(16, 20, 10, 10));

        String[] cols = {"ID", "Nama Customer", "Telepon", "Paket", "Fotografer", "Tgl Sesi", "Durasi", "Foto Edit", "Total Biaya"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(model);
        UIKit.styleTable(tabel, Color.BLACK);
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiFormDariBaris();
        });

        JPanel tombolRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tombolRow.setBackground(UIKit.BG);

        JButton btnSimpan = UIKit.btn("Simpan", UIKit.GREEN);
        JButton btnUpdate = UIKit.btn("Update",  UIKit.BLUE);
        JButton btnRemove = UIKit.btn("Hapus",   UIKit.RED);
        JButton btnReset  = UIKit.btn("Reset",   new Color(65, 65, 95));

        btnSimpan.addActionListener(e -> aksiSimpan());
        btnUpdate.addActionListener(e -> aksiUpdate());
        btnRemove.addActionListener(e -> aksiHapus());
        btnReset .addActionListener(e -> aksiReset());

        tombolRow.add(btnSimpan);
        tombolRow.add(btnUpdate);
        tombolRow.add(btnRemove);
        tombolRow.add(btnReset);

        p.add(UIKit.scroll(tabel), BorderLayout.CENTER);
        p.add(tombolRow,           BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildForm() {
        JPanel p = UIKit.formPanel(270);
        JLabel formTitle = UIKit.heading("Form Jasa Foto", UIKit.BLUE, UIKit.FONT_H3);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(formTitle);
        p.add(UIKit.gap(16));

        p.add(UIKit.label("Nama Customer *"));
        p.add(UIKit.gap(4));
        txtNama = new JTextField();
        styleInput(txtNama);
        p.add(txtNama);
        p.add(UIKit.gap(10));

        p.add(UIKit.label("No. Telepon"));
        p.add(UIKit.gap(4));
        txtTelepon = new JTextField();
        styleInput(txtTelepon);
        p.add(txtTelepon);
        p.add(UIKit.gap(14));

        UIKit.addSep(p);
        p.add(UIKit.gap(14));

        p.add(UIKit.label("Paket Foto *"));
        p.add(UIKit.gap(4));
        cmbPaket = UIKit.combo(DataStore.PAKET_FOTO);
        cmbPaket.addActionListener(e -> updateBiaya());
        p.add(cmbPaket);
        p.add(UIKit.gap(10));

        p.add(UIKit.label("Nama Fotografer"));
        p.add(UIKit.gap(4));
        txtFotografer = new JTextField();
        txtFotografer.setText("Fotografer Studio");
        styleInput(txtFotografer);
        p.add(txtFotografer);
        p.add(UIKit.gap(10));

        p.add(UIKit.label("Tanggal Sesi (yyyy-MM-dd) *"));
        p.add(UIKit.gap(4));
        txtTglSesi = new JTextField();
        txtTglSesi.setText(LocalDate.now().toString());
        styleInput(txtTglSesi);
        p.add(txtTglSesi);
        p.add(UIKit.gap(10));

        p.add(UIKit.label("Durasi (jam)"));
        p.add(UIKit.gap(4));
        spnDurasi = UIKit.spinner(2, 1, 12, 1);
        styleInput(spnDurasi);
        spnDurasi.addChangeListener(e -> updateBiaya());
        p.add(spnDurasi);
        p.add(UIKit.gap(10));

        p.add(UIKit.label("Jumlah Foto Diedit"));
        p.add(UIKit.gap(4));
        spnFotoEdit = UIKit.spinner(20, 0, 500, 5);
        styleInput(spnFotoEdit);
        spnFotoEdit.addChangeListener(e -> updateBiaya());
        p.add(spnFotoEdit);
        p.add(UIKit.gap(16));

        UIKit.addSep(p);
        p.add(UIKit.gap(10));
        p.add(UIKit.label("Estimasi Total:"));
        p.add(UIKit.gap(4));
        lblBiaya = UIKit.previewBiaya("Rp 0");
        p.add(lblBiaya);
        p.add(Box.createVerticalGlue());

        updateBiaya();
        return p;
    }

    private JPanel buildBottom() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(UIKit.SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIKit.BORDER), new EmptyBorder(10, 20, 10, 20)));
        JButton btnSelesai = UIKit.btn("✔ Selesai → Dashboard", UIKit.GREEN);
        btnSelesai.addActionListener(e -> nav.goTo("dashboard"));
        JButton btnBack = UIKit.btn("← Kembali ke Input Menu", new Color(65, 65, 95));
        btnBack.addActionListener(e -> nav.goTo("inputmenu"));
        p.add(btnSelesai);
        p.add(btnBack);
        return p;
    }

    private void aksiSimpan() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) { JOptionPane.showMessageDialog(this, "Nama customer wajib diisi.", "Validasi", JOptionPane.WARNING_MESSAGE); return; }
        
        // --- PERBAIKAN 1: Parameter Customer disesuaikan ---
        String usernameAuto = nama.replaceAll("\\s+","").toLowerCase();
        Customer cust = new Customer(usernameAuto, "12345", nama, "-", txtTelepon.getText().trim(), "-");
        
        String paket = (String) cmbPaket.getSelectedItem();
        double hargaDasar = DataStore.getHargaDasarPaket(paket);
        
        JasaFoto jasa = new JasaFoto(DataStore.nextJasaId(), paket, (int) spnDurasi.getValue(), (int) spnFotoEdit.getValue(), txtFotografer.getText().trim(), txtTglSesi.getText().trim(), hargaDasar);
        Transaksi trx = new Transaksi(cust, jasa);
        DataStore.daftarTransaksi.add(trx);
        
        DataStore.simpanData(); // <-- PERBAIKAN 3: Simpan Permanen
        
        muatTabel();
        aksiReset();
    }

    private void aksiUpdate() {
        if (barisDipilih < 0) return;
        String idLayanan = model.getValueAt(barisDipilih, 0).toString();
        
        for (int i = 0; i < DataStore.daftarTransaksi.size(); i++) {
            Transaksi t = DataStore.daftarTransaksi.get(i);
            if (t.getLayanan().getIdLayanan().equals(idLayanan)) {
                
                String nama = txtNama.getText().trim();
                String usernameAuto = nama.replaceAll("\\s+","").toLowerCase();
                // --- PERBAIKAN 1: Parameter Customer disesuaikan ---
                Customer cust = new Customer(usernameAuto, "12345", nama, "-", txtTelepon.getText().trim(), "-");
                
                String paket = (String) cmbPaket.getSelectedItem();
                JasaFoto jasa = new JasaFoto(idLayanan, paket, (int) spnDurasi.getValue(), (int) spnFotoEdit.getValue(), txtFotografer.getText().trim(), txtTglSesi.getText().trim(), DataStore.getHargaDasarPaket(paket));
                DataStore.daftarTransaksi.set(i, new Transaksi(cust, jasa));
                break;
            }
        }
        
        DataStore.simpanData(); // <-- PERBAIKAN 3: Simpan Permanen
        muatTabel();
        aksiReset();
    }

    private void aksiHapus() {
        if (barisDipilih < 0) return;
        if (JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String id = model.getValueAt(barisDipilih, 0).toString();
            DataStore.daftarTransaksi.removeIf(t -> t.getLayanan().getIdLayanan().equals(id));
            
            DataStore.simpanData(); // <-- PERBAIKAN 3: Simpan Permanen
            muatTabel();
            aksiReset();
        }
    }

    private void aksiReset() {
        txtNama.setText(""); txtTelepon.setText("");
        cmbPaket.setSelectedIndex(0); txtFotografer.setText("Fotografer Studio");
        txtTglSesi.setText(LocalDate.now().toString());
        spnDurasi.setValue(2); spnFotoEdit.setValue(20);
        barisDipilih = -1; tabel.clearSelection(); updateBiaya();
    }

    private void isiFormDariBaris() {
        barisDipilih = tabel.getSelectedRow();
        if (barisDipilih < 0) return;
        String idLayanan = model.getValueAt(barisDipilih, 0).toString();
        
        for (Transaksi t : DataStore.daftarTransaksi) {
            if (t.getLayanan().getIdLayanan().equals(idLayanan) && t.getLayanan() instanceof JasaFoto) {
                JasaFoto jasa = (JasaFoto) t.getLayanan();
                
                // --- PERBAIKAN 2: Pemanggilan Getter ---
                txtNama.setText(t.getCustomer().getNamaLengkap());
                txtTelepon.setText(t.getCustomer().getNoTelepon());
                
                cmbPaket.setSelectedItem(jasa.getPaket());
                txtFotografer.setText(jasa.getFotografer());
                txtTglSesi.setText(jasa.getTglSesi());
                spnDurasi.setValue(jasa.getDurasiJam());
                spnFotoEdit.setValue(jasa.getJumlahFotoEdit());
                updateBiaya();
                break;
            }
        }
    }

    private double hitungBiayaJasa() {
        return DataStore.getHargaDasarPaket((String) cmbPaket.getSelectedItem()) + ((int) spnDurasi.getValue() * 100_000) + ((int) spnFotoEdit.getValue() * 15_000);
    }

    private void updateBiaya() { lblBiaya.setText("Rp " + String.format("%,.0f", hitungBiayaJasa())); }

    private void muatTabel() {
        model.setRowCount(0);
        for (Transaksi r : DataStore.daftarTransaksi) {
            if (r.getLayanan() instanceof JasaFoto) {
                JasaFoto jasa = (JasaFoto) r.getLayanan();
                model.addRow(new Object[]{
                        jasa.getIdLayanan(), 
                        r.getCustomer().getNamaLengkap(), // <-- PERBAIKAN 2
                        r.getCustomer().getNoTelepon(),   // <-- PERBAIKAN 2
                        jasa.getPaket(), jasa.getFotografer(), jasa.getTglSesi(), 
                        jasa.getDurasiJam() + " Jam", jasa.getJumlahFotoEdit() + " Foto", "Rp " + String.format("%,.0f", r.getTotalBiaya())
                });
            }
        }
    }

    public void refresh() { muatTabel(); }
}