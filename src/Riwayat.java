import java.time.LocalDateTime;

public class Riwayat {
    public int tipe;
    public long nominal;
    public String sumber;
    LocalDateTime date;

    Riwayat(long val, String sum, LocalDateTime date, int tipe){
        this.nominal = val;
        this.sumber = sum;
        this.date = date;
        this.tipe = tipe;
    }
}
