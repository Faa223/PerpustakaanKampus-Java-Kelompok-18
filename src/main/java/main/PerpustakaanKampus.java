package main;

import model.Buku;
import model.Mahasiswa;
import model.Transaksi;

import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class PerpustakaanKampus {

    // 1. array 1D: kategori buku
    static String[] kategori = {"Pemrograman", "Matematika", "Jaringan Komputer", "Basis Data"};

    static String[] hari = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};

    static final int LANTAI = 3;
    static final int RAK = 4;
    static final int POSISI = 5;

    // 2. array 2D: jumlah peminjaman [kategori][hari]
    static int[][] peminjaman = new int[kategori.length][hari.length];

    // 3. array 3D: lokasi buku [lantai][rak][posisi], isinya id buku (null = kosong)
    static String[][][] lokasi = new String[LANTAI][RAK][POSISI];

    // 4. arraylist: daftar buku
    static ArrayList<Buku> daftarBuku = new ArrayList<>();

    // 5. stack: riwayat transaksi
    static Stack<Transaksi> riwayat = new Stack<>();

    // 6. queue: antrean mahasiswa
    static Queue<Mahasiswa> antrean = new LinkedList<>();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        dataAwal();

        int pilih;
        do {
            System.out.println("\n==============================================");
            System.out.println("   SISTEM MANAJEMEN PERPUSTAKAAN KAMPUS");
            System.out.println("==============================================");
            System.out.println(" 1. Kelola Buku");
            System.out.println(" 2. Kategori Buku");
            System.out.println(" 3. Statistik Peminjaman");
            System.out.println(" 4. Lokasi Penyimpanan");
            System.out.println(" 5. Transaksi & Riwayat");
            System.out.println(" 6. Antrean Peminjaman");
            System.out.println(" 0. Keluar");
            System.out.println("----------------------------------------------");
            pilih = inputAngka("Pilih menu", 0, 6);

            if (pilih == 1) menuBuku();
            else if (pilih == 2) tampilKategori();
            else if (pilih == 3) tampilStatistik();
            else if (pilih == 4) menuLokasi();
            else if (pilih == 5) menuTransaksi();
            else if (pilih == 6) menuAntrean();
        } while (pilih != 0);

        System.out.println("\nTerima kasih, program selesai.");
    }

    // data awal biar langsung bisa dicoba
    static void dataAwal() {
        String[][] data = {
                {"Java: Dasar Pemrograman", "Budi Raharjo", "2020", "0"},
                {"Algoritma dan Struktur Data", "Rinaldi Munir", "2019", "0"},
                {"Kalkulus Jilid 1", "Purcell", "2018", "1"},
                {"Aljabar Linear Elementer", "Howard Anton", "2017", "1"},
                {"Jaringan Komputer", "Andrew Tanenbaum", "2021", "2"},
                {"TCP/IP Illustrated", "Richard Stevens", "2016", "2"},
                {"Sistem Basis Data", "Abraham Silberschatz", "2020", "3"},
                {"Dasar-dasar SQL", "Joe Celko", "2018", "3"}
        };
        for (int i = 0; i < data.length; i++) {
            Buku b = new Buku(data[i][0], data[i][1],
                    Integer.parseInt(data[i][2]), Integer.parseInt(data[i][3]));
            if (taruhDiRak(b)) {
                daftarBuku.add(b);
            }
        }

        int[][] contoh = {
                {5, 7, 6, 8, 4, 2, 1},
                {3, 2, 4, 3, 5, 1, 0},
                {2, 4, 3, 5, 6, 2, 1},
                {4, 3, 5, 4, 3, 1, 0}
        };
        for (int i = 0; i < contoh.length; i++) {
            for (int j = 0; j < contoh[i].length; j++) {
                peminjaman[i][j] = contoh[i][j];
            }
        }
    }

    // ================= KELOLA BUKU (ArrayList) =================

    static void menuBuku() {
        int pilih;
        do {
            System.out.println("\n--- KELOLA BUKU ---");
            System.out.println("1. Tambah buku");
            System.out.println("2. Tampilkan semua buku");
            System.out.println("3. Cari buku");
            System.out.println("4. Hapus buku");
            System.out.println("0. Kembali");
            pilih = inputAngka("Pilih", 0, 4);

            if (pilih == 1) tambahBuku();
            else if (pilih == 2) tampilBuku();
            else if (pilih == 3) cariBuku();
            else if (pilih == 4) hapusBuku();
        } while (pilih != 0);
    }

    static void tambahBuku() {
        boolean lagi = true;
        while (lagi) {
            System.out.println("\n[Tambah Buku]");
            String judul = inputJudul();
            String penulis = inputNama("Penulis", 3, 60);

            if (sudahAda(judul, penulis)) {
                System.out.println("Buku itu sudah ada di koleksi.");
            } else {
                int tahun = inputAngka("Tahun terbit", 1900, Year.now().getValue());

                System.out.println("Pilih kategori:");
                for (int i = 0; i < kategori.length; i++) {
                    System.out.println("  " + (i + 1) + ". " + kategori[i]);
                }
                int kat = inputAngka("Nomor kategori", 1, kategori.length) - 1;

                Buku baru = new Buku(judul, penulis, tahun, kat);
                if (taruhDiRak(baru)) {
                    daftarBuku.add(baru);
                    System.out.println("Buku ditambahkan dengan ID " + baru.getId()
                            + ", lokasi: " + baru.getLokasiTeks());
                } else {
                    System.out.println("Rak sudah penuh, buku tidak bisa ditambahkan.");
                    return;
                }
            }
            lagi = inputYaTidak("Tambah buku lagi?");
        }
    }

    static boolean sudahAda(String judul, String penulis) {
        for (Buku b : daftarBuku) {
            if (b.getJudul().equalsIgnoreCase(judul) && b.getPenulis().equalsIgnoreCase(penulis)) {
                return true;
            }
        }
        return false;
    }

    static void tampilBuku() {
        System.out.println("\n[Daftar Buku] total " + daftarBuku.size() + " buku");
        if (daftarBuku.isEmpty()) {
            System.out.println("Belum ada buku.");
            return;
        }
        String garis = "+------+------------------------------+----------------------+-------+-------------------+-----------+";
        System.out.println(garis);
        System.out.printf("| %-4s | %-28s | %-20s | %-5s | %-17s | %-9s |%n",
                "ID", "Judul", "Penulis", "Tahun", "Kategori", "Status");
        System.out.println(garis);
        for (Buku b : daftarBuku) {
            System.out.printf("| %-4s | %-28s | %-20s | %-5d | %-17s | %-9s |%n",
                    b.getId(), potong(b.getJudul(), 28), potong(b.getPenulis(), 20),
                    b.getTahun(), kategori[b.getKategori()],
                    b.isTersedia() ? "Tersedia" : "Dipinjam");
        }
        System.out.println(garis);
    }

    static void cariBuku() {
        System.out.print("Kata kunci (judul/penulis/ID): ");
        String kata = sc.nextLine().trim().toLowerCase();
        int ketemu = 0;
        for (Buku b : daftarBuku) {
            if (kata.length() > 0 && (b.getJudul().toLowerCase().contains(kata)
                    || b.getPenulis().toLowerCase().contains(kata)
                    || b.getId().toLowerCase().equals(kata))) {
                System.out.println(b.getId() + " | " + b.getJudul() + " | " + b.getPenulis()
                        + " | " + kategori[b.getKategori()]
                        + " | " + (b.isTersedia() ? "Tersedia" : "Dipinjam"));
                ketemu++;
            }
        }
        if (ketemu == 0) System.out.println("Buku tidak ditemukan.");
    }

    static void hapusBuku() {
        tampilBuku();
        if (daftarBuku.isEmpty()) return;

        String id = inputId("ID buku yang mau dihapus");
        Buku b = cariById(id);
        if (b == null) {
            System.out.println("Buku dengan ID " + id + " tidak ada.");
        } else if (!b.isTersedia()) {
            System.out.println("Buku sedang dipinjam " + b.getNamaPeminjam() + ", tidak bisa dihapus.");
        } else {
            lokasi[b.getLantai()][b.getRak()][b.getPosisi()] = null;   // kosongkan rak
            daftarBuku.remove(b);
            System.out.println("Buku \"" + b.getJudul() + "\" berhasil dihapus.");
        }
    }

    static Buku cariById(String id) {
        for (Buku b : daftarBuku) {
            if (b.getId().equalsIgnoreCase(id)) return b;
        }
        return null;
    }

    // ================= KATEGORI (Array 1D) =================

    static void tampilKategori() {
        System.out.println("\n--- KATEGORI BUKU ---");
        for (int i = 0; i < kategori.length; i++) {
            int jumlah = 0;
            for (Buku b : daftarBuku) {
                if (b.getKategori() == i) jumlah++;
            }
            System.out.printf("%d. %-20s : %d buku%n", i + 1, kategori[i], jumlah);
        }
    }

    // ================= STATISTIK (Array 2D) =================

    static void tampilStatistik() {
        System.out.println("\n--- STATISTIK PEMINJAMAN MINGGUAN ---");
        String garis = "+-------------------+-------+-------+-------+-------+-------+-------+-------+-------+";
        System.out.println(garis);
        System.out.printf("| %-17s |", "Kategori \\ Hari");
        for (int j = 0; j < hari.length; j++) {
            System.out.printf(" %-5s |", hari[j].substring(0, 3));
        }
        System.out.printf(" %-5s |%n", "Total");
        System.out.println(garis);

        int[] totalHari = new int[hari.length];
        int totalSemua = 0;
        int terpopuler = 0;
        int maxKategori = -1;

        for (int i = 0; i < kategori.length; i++) {
            int totalKategori = 0;
            System.out.printf("| %-17s |", kategori[i]);
            for (int j = 0; j < hari.length; j++) {
                System.out.printf(" %5d |", peminjaman[i][j]);
                totalKategori += peminjaman[i][j];
                totalHari[j] += peminjaman[i][j];
            }
            System.out.printf(" %5d |%n", totalKategori);
            totalSemua += totalKategori;
            if (totalKategori > maxKategori) {
                maxKategori = totalKategori;
                terpopuler = i;
            }
        }
        System.out.println(garis);

        System.out.printf("| %-17s |", "Total per hari");
        int tersibuk = 0;
        for (int j = 0; j < hari.length; j++) {
            System.out.printf(" %5d |", totalHari[j]);
            if (totalHari[j] > totalHari[tersibuk]) tersibuk = j;
        }
        System.out.printf(" %5d |%n", totalSemua);
        System.out.println(garis);

        System.out.println("Kategori terpopuler : " + kategori[terpopuler] + " (" + maxKategori + " peminjaman)");
        System.out.println("Hari tersibuk       : " + hari[tersibuk] + " (" + totalHari[tersibuk] + " peminjaman)");
    }

    // ================= LOKASI (Array 3D) =================

    static void menuLokasi() {
        int pilih;
        do {
            System.out.println("\n--- LOKASI PENYIMPANAN ---");
            System.out.println("1. Tampilkan peta rak");
            System.out.println("2. Cari lokasi buku");
            System.out.println("3. Lihat isi satu posisi");
            System.out.println("0. Kembali");
            pilih = inputAngka("Pilih", 0, 3);

            if (pilih == 1) tampilRak();
            else if (pilih == 2) cariLokasi();
            else if (pilih == 3) lihatPosisi();
        } while (pilih != 0);
    }

    // taruh buku di slot kosong pertama, mulai dari lantai sesuai kategorinya
    static boolean taruhDiRak(Buku b) {
        int mulai = b.getKategori() % LANTAI;
        for (int k = 0; k < LANTAI; k++) {
            int l = (mulai + k) % LANTAI;
            for (int r = 0; r < RAK; r++) {
                for (int p = 0; p < POSISI; p++) {
                    if (lokasi[l][r][p] == null) {
                        lokasi[l][r][p] = b.getId();
                        b.setLokasi(l, r, p);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static void tampilRak() {
        int terisi = 0;
        for (int l = 0; l < LANTAI; l++) {
            System.out.println("\n=== LANTAI " + (l + 1) + " ===");
            System.out.print("        ");
            for (int p = 0; p < POSISI; p++) {
                System.out.printf("Pos %-4d", p + 1);
            }
            System.out.println();
            for (int r = 0; r < RAK; r++) {
                System.out.printf("Rak %-3d ", r + 1);
                for (int p = 0; p < POSISI; p++) {
                    if (lokasi[l][r][p] == null) {
                        System.out.print("[-    ] ");
                    } else {
                        System.out.printf("[%-5s] ", lokasi[l][r][p]);
                        terisi++;
                    }
                }
                System.out.println();
            }
        }
        System.out.println("\n'-' artinya posisi masih kosong");
        System.out.println("Terisi: " + terisi + " dari " + (LANTAI * RAK * POSISI) + " posisi");
    }

    static void cariLokasi() {
        String id = inputId("ID buku");
        for (int l = 0; l < LANTAI; l++) {
            for (int r = 0; r < RAK; r++) {
                for (int p = 0; p < POSISI; p++) {
                    if (id.equals(lokasi[l][r][p])) {
                        Buku b = cariById(id);
                        System.out.println("Buku " + id + " (" + b.getJudul() + ")");
                        System.out.println("Lokasi: Lantai " + (l + 1) + ", Rak " + (r + 1) + ", Posisi " + (p + 1));
                        return;
                    }
                }
            }
        }
        System.out.println("Buku " + id + " tidak ditemukan di rak.");
    }

    static void lihatPosisi() {
        int l = inputAngka("Lantai (1-" + LANTAI + ")", 1, LANTAI) - 1;
        int r = inputAngka("Rak (1-" + RAK + ")", 1, RAK) - 1;
        int p = inputAngka("Posisi (1-" + POSISI + ")", 1, POSISI) - 1;
        String id = lokasi[l][r][p];
        if (id == null) {
            System.out.println("Posisi itu kosong.");
        } else {
            System.out.println("Isi posisi: " + id + " - " + cariById(id).getJudul());
        }
    }

    // ================= TRANSAKSI (Stack) =================

    static void menuTransaksi() {
        int pilih;
        do {
            System.out.println("\n--- TRANSAKSI & RIWAYAT ---");
            System.out.println("1. Pinjam buku");
            System.out.println("2. Kembalikan buku");
            System.out.println("3. Lihat transaksi terakhir");
            System.out.println("4. Batalkan transaksi terakhir");
            System.out.println("5. Tampilkan semua riwayat");
            System.out.println("0. Kembali");
            pilih = inputAngka("Pilih", 0, 5);

            if (pilih == 1) pinjamManual();
            else if (pilih == 2) kembalikanBuku();
            else if (pilih == 3) lihatTerakhir();
            else if (pilih == 4) batalkanTerakhir();
            else if (pilih == 5) tampilRiwayat();
        } while (pilih != 0);
    }

    static void pinjamManual() {
        System.out.println("\n[Peminjaman Buku]");
        String nama = inputNama("Nama mahasiswa", 2, 50);
        String nim = inputNim();
        String id = inputId("ID buku yang dipinjam");

        Buku b = cariById(id);
        if (b == null) {
            System.out.println("Buku tidak ditemukan.");
            return;
        }
        int h = pilihHari();
        if (!pinjam(b, nama, nim, h)) {
            System.out.println("Buku \"" + b.getJudul() + "\" sedang dipinjam " + b.getNamaPeminjam() + ".");
            System.out.println("Silakan daftar antrean lewat menu Antrean Peminjaman.");
        }
    }

    // dipakai juga waktu layani antrean
    static boolean pinjam(Buku b, String nama, String nim, int h) {
        if (!b.isTersedia()) return false;

        b.pinjam(nama, nim);
        peminjaman[b.getKategori()][h]++;
        riwayat.push(new Transaksi("PINJAM", b, nama, nim, h));

        System.out.println("Berhasil: \"" + b.getJudul() + "\" dipinjam " + nama + " hari " + hari[h] + ".");
        System.out.println("Ambil bukunya di " + b.getLokasiTeks());
        return true;
    }

    static void kembalikanBuku() {
        System.out.println("\n[Pengembalian Buku]");
        String id = inputId("ID buku yang dikembalikan");
        Buku b = cariById(id);
        if (b == null) {
            System.out.println("Buku tidak ditemukan.");
        } else if (b.isTersedia()) {
            System.out.println("Buku ini tidak sedang dipinjam.");
        } else {
            String nama = b.getNamaPeminjam();
            String nim = b.getNimPeminjam();
            b.kembalikan();
            riwayat.push(new Transaksi("KEMBALI", b, nama, nim, -1));
            System.out.println("Berhasil: \"" + b.getJudul() + "\" dikembalikan oleh " + nama + ".");
            System.out.println("Taruh di " + b.getLokasiTeks());
        }
    }

    static void lihatTerakhir() {
        if (riwayat.isEmpty()) {
            System.out.println("Belum ada transaksi.");
        } else {
            System.out.println("Transaksi terakhir:");
            System.out.println(riwayat.peek());
        }
    }

    static void batalkanTerakhir() {
        if (riwayat.isEmpty()) {
            System.out.println("Tidak ada transaksi yang bisa dibatalkan.");
            return;
        }
        Transaksi t = riwayat.pop();
        Buku b = t.getBuku();

        if (!daftarBuku.contains(b)) {
            System.out.println("Transaksi #" + t.getNomor() + " dibuang, tapi tidak bisa dibatalkan karena bukunya sudah dihapus.");
        } else if (t.getJenis().equals("PINJAM")) {
            b.kembalikan();
            if (peminjaman[b.getKategori()][t.getHari()] > 0) {
                peminjaman[b.getKategori()][t.getHari()]--;
            }
            System.out.println("Peminjaman \"" + b.getJudul() + "\" oleh " + t.getNama() + " dibatalkan.");
        } else {
            b.pinjam(t.getNama(), t.getNim());
            System.out.println("Pengembalian \"" + b.getJudul() + "\" dibatalkan, buku kembali dipinjam " + t.getNama() + ".");
        }
    }

    static void tampilRiwayat() {
        System.out.println("\n[Riwayat Transaksi] total " + riwayat.size());
        if (riwayat.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }
        // dibaca dari index terakhir supaya yang terbaru di atas
        for (int i = riwayat.size() - 1; i >= 0; i--) {
            String tanda = (i == riwayat.size() - 1) ? "  <-- TOP" : "";
            System.out.println(riwayat.get(i) + tanda);
        }
    }

    // ================= ANTREAN (Queue) =================

    static void menuAntrean() {
        int pilih;
        do {
            System.out.println("\n--- ANTREAN PEMINJAMAN ---");
            System.out.println("1. Tambah mahasiswa ke antrean");
            System.out.println("2. Lihat antrean terdepan");
            System.out.println("3. Layani antrean terdepan");
            System.out.println("4. Tampilkan semua antrean");
            System.out.println("0. Kembali");
            pilih = inputAngka("Pilih", 0, 4);

            if (pilih == 1) tambahAntrean();
            else if (pilih == 2) lihatDepan();
            else if (pilih == 3) layaniAntrean();
            else if (pilih == 4) tampilAntrean();
        } while (pilih != 0);
    }

    static void tambahAntrean() {
        String nama = inputNama("Nama mahasiswa", 2, 50);
        String nim = inputNim();
        String id = inputId("ID buku yang diinginkan");

        Buku b = cariById(id);
        if (b == null) {
            System.out.println("Buku tidak ditemukan, tidak jadi masuk antrean.");
        } else {
            antrean.offer(new Mahasiswa(nama, nim, id));
            System.out.println(nama + " masuk antrean nomor " + antrean.size()
                    + " untuk buku \"" + b.getJudul() + "\".");
        }
    }

    static void lihatDepan() {
        Mahasiswa m = antrean.peek();
        if (m == null) {
            System.out.println("Antrean kosong.");
        } else {
            System.out.println("Paling depan: " + m.getNama() + " (" + m.getNim() + ") -> "
                    + cariById(m.getIdBuku()).getJudul());
        }
    }

    static void layaniAntrean() {
        Mahasiswa m = antrean.poll();
        if (m == null) {
            System.out.println("Antrean kosong.");
            return;
        }
        System.out.println("Melayani: " + m.getNama() + " (" + m.getNim() + ")");

        Buku b = cariById(m.getIdBuku());
        if (b == null) {
            System.out.println("Bukunya sudah tidak ada di koleksi, mahasiswa keluar dari antrean.");
            return;
        }
        int h = pilihHari();
        if (!pinjam(b, m.getNama(), m.getNim(), h)) {
            // buku masih dipinjam, mahasiswa pindah ke belakang antrean
            antrean.offer(m);
            System.out.println("Buku \"" + b.getJudul() + "\" masih dipinjam, "
                    + m.getNama() + " dipindah ke belakang antrean.");
        }
    }

    static void tampilAntrean() {
        System.out.println("\n[Daftar Antrean] total " + antrean.size());
        if (antrean.isEmpty()) {
            System.out.println("Antrean kosong.");
            return;
        }
        int no = 1;
        for (Mahasiswa m : antrean) {
            System.out.printf("%d. %-15s %-12s -> %s%s%n", no, m.getNama(), m.getNim(),
                    cariById(m.getIdBuku()).getJudul(), no == 1 ? "   <-- FRONT" : "");
            no++;
        }
    }

    // ================= INPUT & VALIDASI =================

    static int pilihHari() {
        System.out.println("Pilih hari:");
        for (int i = 0; i < hari.length; i++) {
            System.out.println("  " + (i + 1) + ". " + hari[i]);
        }
        return inputAngka("Nomor hari", 1, hari.length) - 1;
    }

    // ulangi terus sampai user isi angka yang benar
    static int inputAngka(String pesan, int min, int max) {
        while (true) {
            System.out.print(pesan + ": ");
            String s = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(s);
                if (n >= min && n <= max) return n;
                System.out.println("Harus antara " + min + " sampai " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Harus berupa angka.");
            }
        }
    }

    // judul boleh ada angka, tapi harus ada hurufnya (jadi "1111" ditolak)
    static String inputJudul() {
        while (true) {
            System.out.print("Judul buku: ");
            String s = sc.nextLine().trim().replaceAll("\\s+", " ");
            if (s.length() < 3 || s.length() > 100) {
                System.out.println("Judul harus 3-100 karakter.");
            } else if (hitungHuruf(s) < 2) {
                System.out.println("Judul harus mengandung huruf, tidak boleh angka saja.");
            } else if (!karakterBoleh(s, true, ".,:;-'()/&!?+#")) {
                System.out.println("Judul mengandung karakter yang tidak diizinkan.");
            } else if (berulang(s)) {
                System.out.println("Judul tidak wajar, ada karakter yang berulang terlalu banyak.");
            } else {
                return s;
            }
        }
    }

    // buat nama penulis dan nama mahasiswa: hanya huruf, tidak boleh angka
    static String inputNama(String label, int min, int max) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim().replaceAll("\\s+", " ");
            if (s.length() < min || s.length() > max) {
                System.out.println(label + " harus " + min + "-" + max + " karakter.");
            } else if (!karakterBoleh(s, false, ".,'-")) {
                System.out.println(label + " hanya boleh huruf, tidak boleh ada angka.");
            } else if (hitungHuruf(s) < 2 || berulang(s)) {
                System.out.println(label + " tidak valid.");
            } else {
                return s;
            }
        }
    }

    static String inputNim() {
        while (true) {
            System.out.print("NIM (8-12 digit): ");
            String s = sc.nextLine().trim();
            boolean angkaSemua = s.length() > 0;
            for (int i = 0; i < s.length(); i++) {
                if (!Character.isDigit(s.charAt(i))) angkaSemua = false;
            }
            if (!angkaSemua) {
                System.out.println("NIM hanya boleh angka.");
            } else if (s.length() < 8 || s.length() > 12) {
                System.out.println("NIM harus 8-12 digit.");
            } else {
                return s;
            }
        }
    }

    // format id: B diikuti 3 angka, contoh B001
    static String inputId(String label) {
        while (true) {
            System.out.print(label + " (contoh B001): ");
            String s = sc.nextLine().trim().toUpperCase();
            if (s.matches("B[0-9]{3}")) return s;
            System.out.println("Format ID salah, contoh yang benar: B001");
        }
    }

    static boolean inputYaTidak(String pesan) {
        while (true) {
            System.out.print(pesan + " (y/n): ");
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y")) return true;
            if (s.equals("n")) return false;
            System.out.println("Ketik y atau n saja.");
        }
    }

    static int hitungHuruf(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i))) n++;
        }
        return n;
    }

    // cek semua karakter: huruf, spasi, angka (kalau boleh), atau simbol yang diizinkan
    static boolean karakterBoleh(String s, boolean bolehAngka, String simbol) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean ok = Character.isLetter(c) || c == ' '
                    || (bolehAngka && Character.isDigit(c))
                    || simbol.indexOf(c) >= 0;
            if (!ok) return false;
        }
        return true;
    }

    // true kalau ada karakter sama berurutan 4x atau lebih, contoh "aaaa"
    static boolean berulang(String s) {
        int hitung = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) hitung++;
            else hitung = 1;
            if (hitung >= 4) return true;
        }
        return false;
    }

    static String potong(String s, int panjang) {
        if (s.length() <= panjang) return s;
        return s.substring(0, panjang - 2) + "..";
    }
}