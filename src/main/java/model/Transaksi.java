package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaksi {
    private static int jumlah = 0;

    private int nomor;
    private String jenis;       // "PINJAM" atau "KEMBALI"
    private Buku buku;
    private String nama;
    private String nim;
    private int hari;           // index hari (0 = Senin), -1 kalau pengembalian
    private LocalDateTime waktu;

    public Transaksi(String jenis, Buku buku, String nama, String nim, int hari) {
        jumlah++;
        this.nomor = jumlah;
        this.jenis = jenis;
        this.buku = buku;
        this.nama = nama;
        this.nim = nim;
        this.hari = hari;
        this.waktu = LocalDateTime.now();
    }

    public int getNomor() { return nomor; }
    public String getJenis() { return jenis; }
    public Buku getBuku() { return buku; }
    public String getNama() { return nama; }
    public String getNim() { return nim; }
    public int getHari() { return hari; }

    public String toString() {
        String w = waktu.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return String.format("#%03d | %-7s | %s - %s | %s (%s) | %s",
                nomor, jenis, buku.getId(), buku.getJudul(), nama, nim, w);
    }
}