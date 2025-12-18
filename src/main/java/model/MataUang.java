package model;

public class MataUang {
    private int id;
    private String kode;        // ISO code: IDR, USD, EUR
    private String nama;        // Rupiah, US Dollar
    private String simbol;      // Rp, $, €
    private int desimal;        // 0 = IDR, 2 = USD

    public MataUang(int id, String kode, String nama, String simbol, int desimal) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.simbol = simbol;
        this.desimal = desimal;
    }

    public MataUang() {}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSimbol() {
        return simbol;
    }

    public void setSimbol(String simbol) {
        this.simbol = simbol;
    }

    public int getDesimal() {
        return desimal;
    }

    public void setDesimal(int desimal) {
        this.desimal = desimal;
    }
}
