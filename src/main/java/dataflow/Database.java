package dataflow;

// import sqlite library
import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

// import logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import package
import model.Kategori;
import model.Pengeluaran;
import model.Pemasukan;
import model.Transaksi;

// SINGLETON CLASS!
public class Database {
    // logger
    private static final Logger log = LoggerFactory.getLogger(Database.class);

    // instance
    private static Database instance;

    private final String JDBC_URL = "jdbc:sqlite:";
    private final String DATABASE_FOLDER = "database";
    private final String DATABASE_NAME = "finance.db";

    // global variabel
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private Connection koneksi;
    private Statement stat;

    // constructor
    private Database () {
        try {
            File folder = new File(DATABASE_FOLDER);
            if(!folder.exists()) {
                folder.mkdir();
            }

            String querySql;
            this.koneksi = DriverManager.getConnection(JDBC_URL + DATABASE_FOLDER + File.separator + DATABASE_NAME);
            this.stat = koneksi.createStatement();

            Statement perintah = koneksi.createStatement();

            querySql =
                    """
                    CREATE TABLE "kategori" (
                        "id"	INTEGER NOT NULL UNIQUE,
                        "tipe"	TEXT NOT NULL,
                        "label"	TEXT NOT NULL UNIQUE,
                        PRIMARY KEY("id" AUTOINCREMENT)
                    )
                    """;
            perintah.executeUpdate(querySql);

            // table kategori langsung diisi beberapa data:
            querySql =
                    """
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (1, 'IN', 'gaji');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (2, 'IN', 'tunjangan');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (3, 'IN', 'bonus');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (4, 'IN', 'usaha');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (5, 'IN', 'freelance/proyek');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (6, 'IN', 'penjualan');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (7, 'IN', 'dividen');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (8, 'IN', 'keuntungan investasi');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (9, 'IN', 'transfer masuk');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (10, 'IN', 'hadiah');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (11, 'IN', 'cashback');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (12, 'IN', 'komisi');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (13, 'IN', 'royalti');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (14, 'IN', 'reward aplikasi');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (15, 'IN', 'lain-lain');
                    """;
            perintah.executeUpdate(querySql);

            querySql =
                    """
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (16, 'OUT', 'makanan & minuman');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (17, 'OUT', 'belanja harian');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (18, 'OUT', 'transportasi');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (19, 'OUT', 'tagihan & utilitas');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (20, 'OUT', 'belanja pribadi');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (21, 'OUT', 'gadget & elektronik');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (22, 'OUT', 'kesehatan');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (23, 'OUT', 'hiburan & lifestyle');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (24, 'OUT', 'pendidikan & kursus');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (25, 'OUT', 'kewajiban keuangan');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (26, 'OUT', 'rumah & peralatan');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (27, 'OUT', 'keluarga');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (28, 'OUT', 'hadiah');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (29, 'OUT', 'donasi');
                    INSERT OR IGNORE INTO "main"."kategori" ("id", "jenis", "nama") VALUES (30, 'OUT', 'lain-lain');
                    """;
            perintah.executeUpdate(querySql);

            querySql =
                    """
                    CREATE TABLE "transaksi" (
                        "id"	INTEGER NOT NULL UNIQUE,
                        "tipe"	TEXT NOT NULL,
                        "jumlah"	INTEGER NOT NULL,
                        "id_kategori"	INTEGER NOT NULL,
                        "tanggal_set"	TEXT NOT NULL,
                        "tanggal_buat"	TEXT NOT NULL,
                        "keterangan"	TEXT,
                        PRIMARY KEY("id" AUTOINCREMENT),
                        CONSTRAINT "fk_kategori" FOREIGN KEY("id_kategori") REFERENCES "kategori"("id")
                    )
                    """;
            perintah.executeUpdate(querySql);
            log.info("Database finance berhasil dibuat!");

        } catch (SQLException e) {
            log.error("Database gagal!");
        }
    }

    public static Database getInstance() {
        if(instance == null) {
            instance = new Database();
            log.info("objek database dibuat!");
        }
        return instance;
    }

    public Connection getConnection() {
        return koneksi;
    }

    // fetching kategori
    public ArrayList<Kategori> fetchKategori() {
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM kategori");
            ArrayList<Kategori> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String tipe = rs.getString("tipe");
                String label = rs.getString("label");

                data.add(new Kategori(id, tipe, label));
            }

            rs.close();
            stat.close();
            log.info("Data kategori berhasil difetch!");
            return data;

        } catch (SQLException e) {
            log.error("Gagal fetch data 'kategori'!");
            return null;
        }
    }

    // fetching transaksi
    public ArrayList<Transaksi> fetchTransaksi() {
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM transaksi");

            ArrayList<Transaksi> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String tipe = rs.getString("tipe");
                int jumlah = rs.getInt("jumlah");
                int idKategori = rs.getInt("id_kategori");
                String tanggalSetTemp = rs.getString("tanggal_set");
                String tanggalBuatTemp = rs.getString("Tanggal_buat");

                LocalDateTime tanggalSet = LocalDateTime.parse(tanggalSetTemp, formatter);
                LocalDateTime tanggalBuat = LocalDateTime.parse(tanggalBuatTemp, formatter);

                Kategori kategori = null;
                for(Kategori ktgr : DataManager.getInstance().getDataKategori()){
                    if(ktgr.getId() == idKategori) {
                        kategori = ktgr;
                        break;
                    }
                }

                if(tipe.equals("IN")){
                    data.add(new Pemasukan(id,tipe,jumlah, kategori, tanggalSet, tanggalBuat));
                } else if(tipe.equals("OUT")) {
                    data.add(new Pengeluaran(id, tipe, jumlah, kategori, tanggalSet, tanggalBuat));
                }
            }

            rs.close();
            stat.close();
            log.info("Data transaksi berhasil di fetch!");
            return data;

        } catch (SQLException e) {
            log.error("Gagal fetch data 'transaksi'!");
            return null;
        }
    }

    // insert transaksi baru
    public void insertTransaksi(Transaksi trans) {
        String querySql = "INSERT INTO transaksi (tipe, jumlah, id_kategori, tanggal_set, tanggal_buat, note) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement perintah = koneksi.prepareStatement(querySql);
            perintah.setString(1, trans.getTipe());
            perintah.setInt(2, trans.getJumlah());
            perintah.setInt(3, trans.getIdKategori());

            String tanggalSet = trans.getTanggalSet().format(formatter);
            String tanggalBuat = trans.getTanggalBuat().format(formatter);

            perintah.setString(4, tanggalSet);
            perintah.setString(5, tanggalBuat);
            perintah.setString(6, trans.getKeterangan());

            perintah.executeUpdate();
            log.info("Data pertanggal: {} berhasil ditambahkan!", trans.getTanggalSet());

        } catch (SQLException e) {
            log.error("Kesalahan SQL: {}", e.getMessage());
        }
    }

    // delete transaksi [JIKA PERLU ya..]
    public boolean deleteTransaksi(int id) {
        String querySql = "DELETE FROM transaksi WHERE id = ?";

        try {
            PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(querySql);
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            if (affectedRows > 0) {
                DataManager.getInstance().getDataKategori().removeIf(t -> t.getId() == id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        boolean dbDeleted = ... // kode SQL di atas

        return dbDeleted;
    }

}