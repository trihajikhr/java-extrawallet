package dataflow;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.TipeLabel;
import model.Kategori;
import helper.Converter;

public class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);
    private static Database instance;

    private final String JDBC_URL = "jdbc:sqlite:";
    private final String DATABASE_FOLDER = "database";
    private final String DATABASE_NAME = "finance.db";

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private Connection koneksi;

    private Database () {
        try {
            File folder = new File(DATABASE_FOLDER);
            if(!folder.exists()) {
                folder.mkdir();
            }

            this.koneksi = DriverManager.getConnection(JDBC_URL + DATABASE_FOLDER + File.separator + DATABASE_NAME);

            createTableKategori();
            DataManager.getInstance().setDataKategori(DataSeeder.getInstance().seedArrayKategori());
            DataSeeder.getInstance().seedDatabaseKategori();

            createTableTipeLabel();


        } catch (SQLException e) {
            log.error("Database gagal!",  e);
        }
    }

    // =============== CREATE TABLE =============== //
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

    // [3] >> =============== TIPELABEL FUNCTION GROUP =============== //
    public int insertTipeLabel(String nama, String warna) {
        String querySql = "INSERT INTO tipelabel (nama, warna) VALUES (?, ?)";
        try (PreparedStatement perintah = koneksi.prepareStatement(querySql)){

            PreparedStatement ps = koneksi.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int newId = rs.getInt(1);

            perintah.setString(1, nama);
            perintah.setString(2, warna);

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
            return data;

        } catch (SQLException e) {

            log.error("Terjadi masalah: ", e);
            return null;
        }
    }

    // [4] >> =============== KTEGORI FUNCTION GROUP =============== //
    public ArrayList<Kategori> fetchKategori() {
        try (Statement stat = koneksi.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM kategori");
            ArrayList<Kategori> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String tipe = rs.getString("tipe");
                String label = rs.getString("label");

                data.add(new Kategori(id, tipe, label));
            }

            log.info("Data kategori berhasil difetch!");
            return data;

        } catch (SQLException e) {
            log.error("Gagal fetch data 'kategori'!", e);
            return null;
        }
    }

//     fetching transaksi
    public ArrayList<Transaksi> fetchTransaksi() {
        try (Statement stat = koneksi.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM transaksi");

            ArrayList<Transaksi> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String tipe = rs.getString("tipe");
                int jumlah = rs.getInt("jumlah");
                int idKategori = rs.getInt("id_kategori");
                String tanggalSetTemp = rs.getString("tanggal_set");
                String tanggalBuatTemp = rs.getString("tanggal_buat");

                LocalDateTime tanggalSet = LocalDateTime.parse(tanggalSetTemp, formatter);
                LocalDateTime tanggalBuat = LocalDateTime.parse(tanggalBuatTemp, formatter);

                Kategori kategori = null;
                for(Kategori ktgr : DataManager.getInstance().copyDataKategori()){
                    if(ktgr.getId() == idKategori) {
                        kategori = ktgr;
                        break;
                    }
                }

                if (kategori == null) {
                    log.warn("Kategori id={} tidak ditemukan!", idKategori);
                    continue; // skip
                }

                if(tipe.equals("IN")){
                    data.add(new Pemasukan(id,tipe,jumlah, kategori, tanggalSet, tanggalBuat));
                } else if(tipe.equals("OUT")) {
                    data.add(new Pengeluaran(id, tipe, jumlah, kategori, tanggalSet, tanggalBuat));
                }
            }

            log.info("Data transaksi berhasil di fetch!");
            return data;

        } catch (SQLException e) {
            log.error("Gagal fetch data 'transaksi'!", e);
            return null;
        }
    }

    // insert transaksi baru
    public void insertTransaksi(Transaksi trans) {
        String querySql = "INSERT INTO transaksi (tipe, jumlah, id_kategori, tanggal_set, tanggal_buat, keterangan) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement perintah = koneksi.prepareStatement(querySql)){
            perintah.setString(1, trans.getTipe());
            perintah.setInt(2, trans.getJumlah());
            perintah.setInt(3, trans.getIdKategori());

            String tanggalSet = trans.getTanggal().format(formatter);
            String tanggalBuat = trans.getTanggalBuat().format(formatter);

            perintah.setString(4, tanggalSet);
            perintah.setString(5, tanggalBuat);
            perintah.setString(6, trans.getKeterangan());

            perintah.executeUpdate();
            DataManager.getInstance().coreDataTransaksi().add(trans);
            log.info("Data pertanggal: {} berhasil ditambahkan!", trans.getTanggal());

        } catch (SQLException e) {
            log.error("Data transaksi gagal ditambahkan! ", e);
        }
    }

    // delete transaksi [JIKA PERLU ya..]
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