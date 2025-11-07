import java.util.ArrayList;

public class Tabungan extends Transaksi {
    public String label;
    ArrayList<Transaksi> riwayat = new ArrayList<>();

    Tabungan(String label) {
        this.label = label;
    }

    @Override
    public int mainMenu(){
        System.out.println("== PROGRAM MANAGEMEN KEUANGAN PRIBADI ==");
        System.out.println("1. Pemasukan");
        System.out.println("2. Pengeluaran");
        System.out.println("3. Cek Saldo");
        System.out.println("4. Rencana Tabungan");
        System.out.println("5. Analisis");
        System.out.println("6. Riwayat");
        System.out.println("0. Keluar");
        System.out.print("Masukan pilihan anda: ");
        int pil = scan.nextInt();
        return pil;
    }
}
