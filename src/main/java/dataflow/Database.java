package dataflow;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import helper.Converter;

public class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);
    private static Database instance;

    private final String JDBC_URL = "jdbc:sqlite:";
    private final String DATABASE_FOLDER = "database";
    private final String DATABASE_NAME = "finance.db";

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private Connection koneksi;

    // [1] >=== instance
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


    // [2] >=== objek database singleton
    private Database () {
        try {
            File folder = new File(DATABASE_FOLDER);
            if(!folder.exists()) {
                folder.mkdir();
            }

            this.koneksi = DriverManager.getConnection(JDBC_URL + DATABASE_FOLDER + File.separator + DATABASE_NAME);

            // aktifkan opsi foreign key (tidak aktif secara default!)
            try (Statement perintah = koneksi.createStatement()) {
                perintah.execute("PRAGMA foreign_keys = ON");
            }

            createTableKategori();
            createTableTipeLabel();
            createTableAkun();
            createTableTransaksi();
            createTableTemplate();

        } catch (SQLException e) {
            log.error("Database gagal!",  e);
        }
    }

    // [3] >=== modularisasi statement create table
    private void createTableKategori() {
        try (Statement perintah = koneksi.createStatement()){
            String querySql =
                """
                CREATE TABLE IF NOT EXISTS "kategori" (
                    "id"	INTEGER NOT NULL UNIQUE,
                    "tipe"	TEXT NOT NULL,
                    "nama"	TEXT NOT NULL,
                    "icon_path"	TEXT NOT NULL,
                    "warna"	TEXT NOT NULL,
                    PRIMARY KEY("id" AUTOINCREMENT)
                )
                """;
            perintah.executeUpdate(querySql);
            log.info("table kategori berhasil dibuat!");

        } catch (Exception e) {
            log.error("table kategori gagal dibuat: " , e);
        }
    }

    private void createTableTipeLabel() {
        try (Statement perintah = koneksi.createStatement()){
            String querySql =
                """
                CREATE TABLE IF NOT EXISTS "tipelabel" (
                    "id"	INTEGER NOT NULL UNIQUE,
                    "nama"	TEXT NOT NULL UNIQUE,
                    "warna"	TEXT NOT NULL,
                    PRIMARY KEY("id" AUTOINCREMENT)
                )
                """;
            perintah.executeUpdate(querySql);
            log.info("table tipelabel berhasil dibuat!");

        } catch (SQLException e) {
            log.error("table tipelabel gagal dibuat: ", e);
        }
    }

    private void createTableAkun() {
        try (Statement perintah = koneksi.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "akun" (
                "id"	INTEGER NOT NULL UNIQUE,
                "nama"	TEXT NOT NULL,
                "warna"	TEXT NOT NULL,
                "icon_path"	TEXT NOT NULL,
                "jumlah"	INTEGER NOT NULL,
                PRIMARY KEY("id" AUTOINCREMENT)
            )
            """;
            perintah.executeUpdate(querySql);
            log.info("table akun berhasil dibuat!");

        } catch (SQLException e) {
            log.error("table akun gagal dibuat: ", e);
        }
    }

    private void createTableTransaksi() {
        try (Statement perintah = koneksi.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "transaksi" (
                "id"	INTEGER NOT NULL UNIQUE,
                "tipe"	TEXT NOT NULL,
                "jumlah"	INTEGER NOT NULL,
                "id_akun"	INTEGER NOT NULL,
                "id_kategori"	INTEGER NOT NULL,
                "id_tipelabel"	INTEGER,
                "tanggal"	TEXT NOT NULL,
                "keterangan"	TEXT,
                "metode_transaksi"	TEXT,
                "status"	TEXT,
                PRIMARY KEY("id" AUTOINCREMENT),
                CONSTRAINT "transaksi_akun" FOREIGN KEY("id_akun") REFERENCES "akun"("id") ON DELETE CASCADE,
                CONSTRAINT "transaksi_kategori" FOREIGN KEY("id_kategori") REFERENCES "kategori"("id") ON DELETE RESTRICT,
                CONSTRAINT "transaksi_label" FOREIGN KEY("id_tipelabel") REFERENCES "tipelabel"("id") ON DELETE RESTRICT
            )
            """;
            perintah.executeUpdate(querySql);
            log.info("table transaksi berhasil dibuat!");

        } catch (SQLException e){
            log.error("table transaksi gagal dibuat: ", e);
        }
    }

    private void createTableTemplate() {
        try (Statement perintah = koneksi.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "template" (
                "id"	INTEGER NOT NULL UNIQUE,
                "tipe"	TEXT NOT NULL,
                "jumlah_satu"	INTEGER NOT NULL,
                "jumlah_dua"	INTEGER,
                "id_akun_satu"	INTEGER NOT NULL,
                "id_akun_dua"	INTEGER,
                "id_kategori"	INTEGER,
                "id_tipelabel"	INTEGER,
                "keterangan"	TEXT,
                "metode_transaksi"	TEXT,
                "status"	TEXT,
                PRIMARY KEY("id" AUTOINCREMENT),
                CONSTRAINT "template_akun_dua" FOREIGN KEY("id_akun_dua") REFERENCES "akun"("id") ON DELETE CASCADE,
                CONSTRAINT "template_akun_satu" FOREIGN KEY("id_akun_satu") REFERENCES "akun"("id") ON DELETE CASCADE,
                CONSTRAINT "template_kategori" FOREIGN KEY("id_kategori") REFERENCES "kategori"("id") ON DELETE CASCADE,
                CONSTRAINT "template_label" FOREIGN KEY("id_tipelabel") REFERENCES "tipelabel"("id") ON DELETE CASCADE
            )
            """;
            perintah.executeUpdate(querySql);
            log.info("table template berhasil dibuat!");

        } catch (SQLException e) {
            log.error("table template gagal dibuat!");
        }
    }


    // [4] >=== akses dan modifikasi data tipelabel
    public int insertTipeLabel(TipeLabel tipelabel) {
        String querySql = "INSERT INTO tipelabel (nama, warna) VALUES (?, ?)";
        try (PreparedStatement perintah = koneksi.prepareStatement(querySql)){

            PreparedStatement ps = koneksi.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int newId = rs.getInt(1);

            perintah.setString(1, tipelabel.getNama());
            perintah.setString(2, Converter.getInstance().colorToHex(tipelabel.getWarna()));

            perintah.executeUpdate();
            log.info("tipe label berhasil ditambahkan!");
            return newId;

        } catch (SQLException e) {
            log.error("tipe label gagal dibuat!");
            return -1;
        }
    }

    public ArrayList<TipeLabel> fetchTipeLabel() {
        try (Statement stat = koneksi.createStatement()){
            ResultSet rs = stat.executeQuery("SELECT * FROM tipelabel");
            ArrayList<TipeLabel> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String hex = rs.getString("warna");

                Color warna = Converter.getInstance().hexToColor(hex);
                data.add(new TipeLabel(id, nama, warna));
            }
            log.info("fetch data tipelabel berhasil!");
            return data;

        } catch (SQLException e) {

            log.error("fetch data tipelabel gagal: ", e);
            return null;
        }
    }


    // [5] >=== akses data kategori
    public ArrayList<Kategori> fetchKategori() {
        try (Statement stat = koneksi.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM kategori");
            ArrayList<Kategori> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String tipe = rs.getString("tipe");
                String nama = rs.getString("nama");
                String iconPath = rs.getString("icon_path");
                Color warna = Converter.getInstance().hexToColor(rs.getString("warna"));

                data.add(
                    new Kategori (
                        id,
                        tipe,
                        nama,
                        new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toString()),
                        iconPath,
                        warna
                    )
                );
            }

            log.info("Data kategori berhasil difetch!");
            return data;

        } catch (SQLException e) {
            log.error("Gagal fetch data 'kategori'!", e);
            return null;
        }
    }


    // [6] >=== fetching, insert, dan hapus data transaksi
    public ArrayList<Transaksi> fetchTransaksi() {
        try (Statement stat = koneksi.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM transaksi");

            ArrayList<Transaksi> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String tipe = rs.getString("tipe");
                int jumlah = rs.getInt("jumlah");
                int idAkun = rs.getInt("id_akun");
                int idKategori = rs.getInt("id_kategori");
                int idTipeLabel = rs.getInt("id_tipelabel");
                String tanggalSet = rs.getString("tanggal");
                String keterangan = rs.getString("keterangan");
                String metodeTransaksi = rs.getString("metode_transaksi");
                String status = rs.getString("status");

                LocalDateTime tanggal = LocalDateTime.parse(tanggalSet, formatter);

                Akun akun = null;
                for(Akun item : DataManager.getInstance().coreDataAkun()){
                    if(item.getId() == idAkun) {
                        akun = item;
                        break;
                    }
                }

                if (akun == null ) {
                    log.warn("fetch transaksi:  idAkun={} tidak ditemukan!", idAkun);
                    continue; // skip
                }

                Kategori kategori = null;
                for(Kategori item : DataManager.getInstance().coreDataKategori()){
                    if(item.getId() == idKategori) {
                        kategori = item;
                        break;
                    }
                }

                if (kategori == null ) {
                    log.warn("fetch transaksi:  idKategori={} tidak ditemukan!", idKategori);
                    continue; // skip
                }

                TipeLabel tipelabel = null;
                for(TipeLabel item : DataManager.getInstance().coreDataTipeLabel()) {
                    if(item.getId() == idTipeLabel) {
                        tipelabel = item;
                        break;
                    }
                }

                if (tipelabel == null ) {
                    log.warn("fetch transaksi:  idTipeLabel={} tidak ditemukan!", idTipeLabel);
                    continue; // skip
                }

                if(tipe.equals("IN")){
                    data.add(new Pemasukan(id, tipe, jumlah, akun, kategori, tipelabel, tanggal, keterangan, metodeTransaksi, status));
                } else if(tipe.equals("OUT")) {
                    data.add(new Pengeluaran(id, tipe, jumlah, akun, kategori, tipelabel, tanggal, keterangan, metodeTransaksi, status));
                }
            }

            log.info("data transaksi berhasil di fetch!");
            return data;

        } catch (SQLException e) {
            log.error("gagal fetch data 'transaksi'!", e);
            return null;
        }
    }

    public int insertTransaksi(Transaksi trans) {
        String querySql = "INSERT INTO transaksi " +
                "(tipe, jumlah, id_akun, id_kategori, id_tipelabel, tanggal, keterangan, metode_transaksi, status) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement perintah = koneksi.prepareStatement(querySql)){

            PreparedStatement ps = koneksi.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int newId = rs.getInt(1);

            perintah.setString(1, trans.getTipe());
            perintah.setInt(2, trans.getJumlah());
            perintah.setInt(3, trans.getAkun().getId());
            perintah.setInt(4, trans.getKategori().getId());
            perintah.setInt(5, trans.getTipelabel().getId());
            perintah.setString(6, trans.getTanggal().format(formatter));
            perintah.setString(7, trans.getKeterangan());
            perintah.setString(8, trans.getMetodeTransaksi());
            perintah.setString(9, trans.getStatus());
            perintah.executeUpdate();
            log.info("data transaksi pertanggal: {} berhasil ditambahkan!", trans.getTanggal());
            return newId;

        } catch (SQLException e) {
            log.error("Data transaksi gagal ditambahkan! ", e);
            return -1;
        }
    }

    public void deleteTransaksi(int id) {
        String querySql = "DELETE FROM transaksi WHERE id = ?";

        try (PreparedStatement perintah = Database.getInstance().koneksi.prepareStatement(querySql)){
            perintah.setInt(1, id);
            int affectedRows = perintah.executeUpdate();

            if (affectedRows > 0) {
                DataManager.getInstance().coreDataTransaksi().removeIf(t -> t.getId() == id);
            }
            log.info("Data transaksi berhasil dihapus!");
        } catch (Exception e) {
            log.error("Data transaksi gagal dihapus!", e);
        }
    }
}