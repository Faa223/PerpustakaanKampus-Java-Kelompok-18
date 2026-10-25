package model;

public class Mahasiswa {
    private String nama;
    private String nim;
    private String idBuku;   // buku yang mau dipinjam

    public Mahasiswa(String nama, String nim, String idBuku) {
        this.nama = nama;
        this.nim = nim;
        this.idBuku = idBuku;
    }

    public String getNama() { return nama; }
    public String getNim() { return nim; }
    public String getIdBuku() { return idBuku; }
}