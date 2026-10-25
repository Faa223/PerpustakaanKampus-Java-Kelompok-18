package model;

public class Buku {
    private static int jumlah = 0;

    private String id;
    private String judul;
    private String penulis;
    private int tahun;
    private int kategori;   // index di array kategori
    private int lantai, rak, posisi;
    private boolean tersedia = true;
    private String namaPeminjam;
    private String nimPeminjam;

    public Buku(String judul, String penulis, int tahun, int kategori) {
        jumlah++;
        this.id = String.format("B%03d", jumlah);
        this.judul = judul;
        this.penulis = penulis;
        this.tahun = tahun;
        this.kategori = kategori;
    }

    public String getId() { return id; }
    public String getJudul() { return judul; }
    public String getPenulis() { return penulis; }
    public int getTahun() { return tahun; }
    public int getKategori() { return kategori; }
    public int getLantai() { return lantai; }
    public int getRak() { return rak; }
    public int getPosisi() { return posisi; }
    public boolean isTersedia() { return tersedia; }
    public String getNamaPeminjam() { return namaPeminjam; }
    public String getNimPeminjam() { return nimPeminjam; }

    public void setLokasi(int lantai, int rak, int posisi) {
        this.lantai = lantai;
        this.rak = rak;
        this.posisi = posisi;
    }

    public void pinjam(String nama, String nim) {
        tersedia = false;
        namaPeminjam = nama;
        nimPeminjam = nim;
    }

    public void kembalikan() {
        tersedia = true;
        namaPeminjam = null;
        nimPeminjam = null;
    }

    public String getLokasiTeks() {
        return "Lantai " + (lantai + 1) + ", Rak " + (rak + 1) + ", Posisi " + (posisi + 1);
    }
}