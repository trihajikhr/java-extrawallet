package dataflow;

// import sqlite library
import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

// import logger
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import package
import model.TipeLabel;
import model.

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

    // constructor
    private Database () {
        try {
            File folder = new File(DATABASE_FOLDER);
            if(!folder.exists()) {
                folder.mkdir();
            }

            String querySql;
            this.koneksi = DriverManager.getConnection(JDBC_URL + DATABASE_FOLDER + File.separator + DATABASE_NAME);

            querySql =
            """
            CREATE TABLE IF NOT EXISTS "kategori" (
                "id"	INTEGER NOT NULL UNIQUE,
                "tipe"	TEXT NOT NULL,
                "label"	TEXT NOT NULL,
                PRIMARY KEY("id" AUTOINCREMENT)
            )
            """;

            try (Statement perintah = koneksi.createStatement()) {
                perintah.executeUpdate(querySql);
            }

            querySql =
            """
            CREATE TABLE IF NOT EXISTS "tipelabel" (
                "id"	INTEGER NOT NULL UNIQUE,
                "nama"	TEXT NOT NULL UNIQUE,
                "warna"	TEXT NOT NULL,
                PRIMARY KEY("id" AUTOINCREMENT)
            )
            """;

            try (Statement perintah = koneksi.createStatement()) {
                perintah.executeUpdate(querySql);
                log.info("table label terbuat!");
            }

            String[][] kategoriData = {
                // IN
                {"IN", "salary"},
                {"IN", "allowance"},
                {"IN", "bonus"},
                {"IN", "business"},
                {"IN", "freelance/project"},
                {"IN", "sales"},
                {"IN", "dividends"},
                {"IN", "investment gains"},
                {"IN", "incoming transfer"},
                {"IN", "gift"},
                {"IN", "cashback"},
                {"IN", "commission"},
                {"IN", "royalty"},
                {"IN", "app reward"},
                {"IN", "others"},

                // OUT
                {"OUT", "food & beverages"},
                {"OUT", "daily shopping"},
                {"OUT", "transportation"},
                {"OUT", "bills & utilities"},
                {"OUT", "personal shopping"},
                {"OUT", "gadgets & electronics"},
                {"OUT", "health"},
                {"OUT", "entertainment & lifestyle"},
                {"OUT", "education & courses"},
                {"OUT", "financial obligations"},
                {"OUT", "home & appliances"},
                {"OUT", "family"},
                {"OUT", "gift"},
                {"OUT", "donation"},
                {"OUT", "others"}
            };

            try (PreparedStatement ps = koneksi.prepareStatement(
                    "INSERT OR IGNORE INTO kategori (tipe, label) VALUES (?,?)")) {
                for (int i = 0; i < kategoriData.length; i++) {
                    ps.setString(1, kategoriData[i][0]);
                    ps.setString(2, kategoriData[i][1]);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            querySql =
            """
            CREATE TABLE IF NOT EXISTS "transaksi" (
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

            try (Statement perintah = koneksi.createStatement()) {
                perintah.executeUpdate(querySql);
            }

            log.info("Database finance berhasil dibuat!");
        } catch (SQLException e) {
            log.error("Database gagal!",  e);
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

    // =============== TIPELABEL FUNCTION GROUP =============== //
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

    // fetching table [tipelabel
    public ArrayList<TipeLabel> fetchTipeLabel() {
        try (Statement stat = koneksi.createStatement()){
            ResultSet rs = stat.executeQuery("SELECT * FROM tipelabel");
            ArrayList<TipeLabel> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String warnaTemp = rs.getString("warna");

                Color warna = Color.web("#" + warnaTemp);

                data.add(new TipeLabel(id, nama, warna));
            }

            return data;
        } catch (SQLException e) {
            log.error("Terjadi masalah: ", e);
            return null;
        }
    }

//     fetching kategori
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