import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    Transaksi trans = new Transaksi();
    int pil = 1;
    do {
        pil = trans.mainMenu();
        System.out.println("\n");
        if(pil == 1){
            trans.pemasukan();
        } else if(pil == 2){
          trans.pengeluaran();
        } else if(pil == 3){
            trans.cekSaldo();
        }  else if(pil == 4) {
            trans.rencanaTabungan();
        } else if(pil == 5){
            trans.analisis();
        } else if(pil == 6){
            trans.tampilRiwayat();
        }
    } while(pil != 0);
    }
}