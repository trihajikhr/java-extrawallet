package dataflow;

// import sqlite library
import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

// import logger
import model.Pengeluaran;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import package
import model.Pemasukan;
import model.Transaksi;

// SINGLETON CLASS!
public class Database {
    // logger
    private static final Logger log = LoggerFactory.getLogger(Database.class);

    // instance
    private static Database instance;
    private Connection koneksi;
    private Statement stat;
    private final String JDBC_URL = "jdbc:sqlite:";
    private final String DATABASE_FOLDER = "database";
    private final String DATABASE_NAME = "finance.db";

    // global variabel
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // constructor
    private Database () throws SQLException {
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
                    CREATE TABLE IF NOT EXISTS "kategori" (
                        "id"	INTEGER NOT NULL UNIQUE,
                        "jenis"	TEXT,
                        "nama"	TEXT UNIQUE,
                        PRIMARY KEY("id" AUTOINCREMENT)
                    )
                    """;
            perintah.executeUpdate(querySql);

            // table kategori langsung diisi beberapa data:
            querySql =
                    """
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (1, 'IN', 'gaji');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (2, 'IN', 'tunjangan');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (3, 'IN', 'bonus');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (4, 'IN', 'usaha');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (5, 'IN', 'freelance/proyek');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (6, 'IN', 'penjualan');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (7, 'IN', 'dividen');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (8, 'IN', 'keuntungan investasi');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (9, 'IN', 'transfer masuk');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (10, 'IN', 'hadiah');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (11, 'IN', 'cashback');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (12, 'IN', 'komisi');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (13, 'IN', 'royalti');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (14, 'IN', 'reward aplikasi');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (15, 'IN', 'lain-lain');
                    """;
            perintah.executeUpdate(querySql);

            querySql =
                    """
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (16, 'OUT', 'makanan & minuman');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (17, 'OUT', 'belanja harian');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (18, 'OUT', 'transportasi');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (19, 'OUT', 'tagihan & utilitas');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (20, 'OUT', 'belanja pribadi');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (21, 'OUT', 'gadget & elektronik');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (22, 'OUT', 'kesehatan');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (23, 'OUT', 'hiburan & lifestyle');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (24, 'OUT', 'pendidikan & kursus');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (25, 'OUT', 'kewajiban keuangan');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (26, 'OUT', 'rumah & peralatan');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (27, 'OUT', 'keluarga');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (28, 'OUT', 'hadiah');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (29, 'OUT', 'donasi');
                    INSERT INTO "main"."kategori" ("id", "jenis", "nama") VALUES (30, 'OUT', 'lain-lain');
                    """;
            perintah.executeUpdate(querySql);

            querySql =
                    """
                    CREATE TABLE IF NOT EXISTS "transaksi" (
                        "id"	INTEGER NOT NULL UNIQUE,
                        "tipe"	TEXT NOT NULL,
                        "jumlah"	INTEGER NOT NULL,
                        "id_kategori"	INTEGER NOT NULL,
                        "tanggal_set"	TEXT NOT NULL,
                        "tanggal_buat"	TEXT NOT NULL,
                        "note"	TEXT,
                        PRIMARY KEY("id" AUTOINCREMENT),
                        CONSTRAINT "fk_kategori" FOREIGN KEY("id_kategori") REFERENCES "kategori"("id")
                    )
                    """;
            perintah.executeUpdate(querySql);
            log.info("Database finance berhasil dibuat!");

        } catch (SQLException e) {
            log.error("Database gagal!");
            throw e;
        }
    }

    public static Database getInstance() throws SQLException {
        if(instance == null) {
            instance = new Database();
            log.info("objek database dibuat!");
        }
        return instance;
    }

    public Connection getConnection() {
        return koneksi;
    }

    // fetching transaksi
    ArrayList<Transaksi> fetchDatabaseData() {
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

//                String strSekarang = sekarang.format(formatter);
//                System.out.println(strSekarang); // contoh: 2025-12-10T23:45:12

                if(tipe.equals("IN")){
                    data.add(new Pemasukan(id,tipe,jumlah, idKategori, tanggalSet, tanggalBuat));
                } else if(tipe.equals("OUT")) {
                    data.add(new Pengeluaran(id, tipe, jumlah, idKategori, tanggalSet, tanggalBuat));
                }
            }

            rs.close();
            stat.close();
            return data;

        } catch (SQLException e) {
            log.error("Gagal fetch data database");
            return null;
        }
    }

    // insert transaksi baru
    public boolean insertTransaksi(Transaksi trans) {
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
            return true;

        } catch (SQLException e) {
            log.error("Kesalahan SQL: {}", e.getMessage());
            return false;
        }
    }
}