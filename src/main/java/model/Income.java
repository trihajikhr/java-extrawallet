import java.time.LocalDateTime;

public class Income {
    protected int idPemasukan;
    protected LocalDateTime tanggal;
    protected int idKategori;
    protected int jumlah;

    public Transaksi(int idPemasukan, LocalDateTime tanggal, int idKategori, int jumlah) {
        this.idPemasukan = idPemasukan;
        this.tanggal = tanggal;
        this.idKategori = idKategori;
        this.jumlah = jumlah;
    }