package dataflow;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import helper.Converter;
import service.AppPaths;

// TODO [IMPORTANT]:
// Database Singleton masih basic.
// Perlu:
// - Thread-safe init (double-check / holder)
// - Connection lifecycle management
// - Reconnect handling kalau koneksi mati
// Jangan refactor sekarang, fokus fitur dulu!

public class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);
    private static Database instance;

    private final String JDBC_URL = "jdbc:sqlite:";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Connection koneksi;

    // [0] >=== instance
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

    // [1] >=== objek database singleton
    private Database () {
        try {
            Files.createDirectories(AppPaths.DATABASE_DIR);

            this.koneksi = DriverManager.getConnection(JDBC_URL + AppPaths.DB_FILE.toAbsolutePath());

            // aktifkan opsi foreign key (tidak aktif secara default!)
            try (Statement perintah = koneksi.createStatement()) {
                perintah.execute("PRAGMA foreign_keys = ON");
            }

            createTableKategori();
            createTableMataUang();
            createTableTipeLabel();
            createTableAkun();
            createTableTransaksi();
            createTableTemplate();

            log.info("Database siap digunakan di: {}", AppPaths.DB_FILE.toAbsolutePath());

        } catch (SQLException e) {
            log.error("Database gagal!",  e);
        } catch (IOException e) {
            log.error("pembuatan folder database gagal!", e);
        }
    }

    // [2] >=== modularisasi statement create table
    private void createTableKategori() {
        try (Statement perintah = koneksi.createStatement()){
            String querySql =
                """
                CREATE TABLE IF NOT EXISTS "category" (
                    "id"	INTEGER NOT NULL UNIQUE,
                    "tipe"	TEXT NOT NULL,
                    "nama"	TEXT NOT NULL,
                    "icon_path"	TEXT NOT NULL,
                    "warna"	TEXT NOT NULL,
                    PRIMARY KEY("id" AUTOINCREMENT)
                )
                """;
            perintah.executeUpdate(querySql);
            log.info("table category berhasil dibuat!");

        } catch (Exception e) {
            log.error("table category gagal dibuat: " , e);
        }
    }
    private void createTableMataUang() {
        try(Statement perintah = koneksi.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "mata_uang" (
                "id"	INTEGER NOT NULL UNIQUE,
                "kode"	TEXT NOT NULL,
                "nama"	TEXT NOT NULL,
                "simbol"	TEXT NOT NULL,
                "desimal"	INTEGER NOT NULL,
                PRIMARY KEY("id" AUTOINCREMENT)
            )
            """;

            perintah.executeUpdate(querySql);
            log.info("table mata_uang berhasil dibuat!");

        } catch (SQLException e){
            log.error("table mata_uang gagal dibuat: ", e);
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
            CREATE TABLE IF NOT EXISTS "account" (
                "id"	INTEGER NOT NULL UNIQUE,
                "nama"	TEXT NOT NULL,
                "warna"	TEXT NOT NULL,
                "icon_path"	TEXT NOT NULL,
                "jumlah"	INTEGER NOT NULL,
                "id_mata_uang"	INTEGER NOT NULL,
                PRIMARY KEY("id" AUTOINCREMENT),
                CONSTRAINT "akun_mata_uang" FOREIGN KEY("id_mata_uang") REFERENCES "mata_uang"("id")
            )
            """;
            perintah.executeUpdate(querySql);
            log.info("table account berhasil dibuat!");

        } catch (SQLException e) {
            log.error("table account gagal dibuat: ", e);
        }
    }
    private void createTableTransaksi() {
        try (Statement perintah = koneksi.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "transaction" (
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
                CONSTRAINT "transaksi_akun" FOREIGN KEY("id_akun") REFERENCES "account"("id") ON DELETE CASCADE,
                CONSTRAINT "transaksi_kategori" FOREIGN KEY("id_kategori") REFERENCES "category"("id") ON DELETE RESTRICT,
                CONSTRAINT "transaksi_label" FOREIGN KEY("id_tipelabel") REFERENCES "tipelabel"("id") ON DELETE RESTRICT
            )
            """;
            perintah.executeUpdate(querySql);
            log.info("table transaction berhasil dibuat!");

        } catch (SQLException e){
            log.error("table transaction gagal dibuat: ", e);
        }
    }
    private void createTableTemplate() {
        try (Statement perintah = koneksi.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "template" (
                "id"	INTEGER NOT NULL UNIQUE,
                "tipe"	TEXT NOT NULL,
                "nama"	TEXT NOT NULL,
                "jumlah"	INTEGER NOT NULL,
                "id_akun"	INTEGER NOT NULL,
                "id_kategori"	INTEGER NOT NULL,
                "id_tipelabel"	INTEGER,
                "keterangan"	TEXT,
                "metode_transaksi"	TEXT,
                "status"	TEXT,
                PRIMARY KEY("id" AUTOINCREMENT),
                CONSTRAINT "template_akun" FOREIGN KEY("id_akun") REFERENCES "account"("id") ON DELETE CASCADE,
                CONSTRAINT "template_kategori" FOREIGN KEY("id_kategori") REFERENCES "category"("id") ON DELETE CASCADE,
                CONSTRAINT "template_label" FOREIGN KEY("id_tipelabel") REFERENCES "tipelabel"("id") ON DELETE CASCADE
            )
            """;
            perintah.executeUpdate(querySql);
            log.info("table template berhasil dibuat!");

        } catch (SQLException e) {
            log.error("table template gagal dibuat!");
        }
    }

    // [3] >=== manipulasi data tipelabel
    public int insertTipeLabel(LabelType tipelabel) {
        String querySql = "INSERT INTO tipelabel (nama, warna) VALUES (?, ?)";

        try {
            koneksi.setAutoCommit(false);
            try (PreparedStatement ps = koneksi.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, tipelabel.getName());
                ps.setString(2, Converter.colorToHex(tipelabel.getColor()));

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Insert tidak mengubah data");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Generated key tidak ditemukan");
                    }

                    int newId = rs.getInt(1);
                    koneksi.commit();
                    return newId;
                }
            }
        } catch (SQLException e) {
            try {
                koneksi.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal!", ex);
            }
            log.error("insert label gagal!", e);
            return -1;

        } finally {
            try {
                koneksi.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("gagal reset autoCommit!", e);
            }
        }
    }
    public ArrayList<LabelType> fetchTipeLabel() {
        try (Statement stat = koneksi.createStatement()){
            ResultSet rs = stat.executeQuery("SELECT * FROM tipelabel");
            ArrayList<LabelType> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String hex = rs.getString("warna");

                Color warna = Converter.hexToColor(hex);
                data.add(new LabelType(id, nama, warna));
            }
            log.info("fetch data tipelabel berhasil!");
            return data;

        } catch (SQLException e) {

            log.error("fetch data tipelabel gagal: ", e);
            return null;
        }
    }

    // [4] >=== manipulasi data category
    public ArrayList<Category> fetchKategori() {
        try (Statement stat = koneksi.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM category");
            ArrayList<Category> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String tipe = rs.getString("tipe");
                String nama = rs.getString("nama");
                String iconPath = rs.getString("icon_path");
                Color warna = Converter.hexToColor(rs.getString("warna"));

                data.add(
                    new Category(
                        id,
                        tipe,
                        nama,
                        new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toString()),
                        iconPath,
                        warna
                    )
                );
            }

            log.info("Data category berhasil difetch!");
            return data;

        } catch (SQLException e) {
            log.error("Gagal fetch data 'category'!", e);
            return null;
        }
    }

    // [5] >=== manipulasi data transaction
    public ArrayList<Transaction> fetchTransaksi() {
        try (Statement stat = koneksi.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM transaction");

            ArrayList<Transaction> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                TransactionType tipe = TransactionType.valueOf(rs.getString("tipe"));
                int jumlah = rs.getInt("jumlah");
                int idAkun = rs.getInt("id_akun");
                int idKategori = rs.getInt("id_kategori");
                int idTipeLabel = rs.getInt("id_tipelabel");
                String tanggalSet = rs.getString("tanggal");
                String keterangan = rs.getString("keterangan");
                PaymentType paymentType = PaymentType.fromString(rs.getString("metode_transaksi"));
                PaymentStatus status = PaymentStatus.fromString(rs.getString("status"));

                LocalDate tanggal = LocalDate.parse(tanggalSet, formatter);

                Account account = null;
                for(Account item : DataManager.getInstance().getDataAkun()){
                    if(item.getId() == idAkun) {
                        account = item;
                        break;
                    }
                }

                if (account == null ) {
                    log.warn("fetch transaction:  idAkun={} tidak ditemukan!", idAkun);
                    continue; // skip
                }

                Category category = null;
                for(Category item : DataManager.getInstance().getDataKategori()){
                    if(item.getId() == idKategori) {
                        category = item;
                        break;
                    }
                }

                if (category == null ) {
                    log.warn("fetch transaction:  idKategori={} tidak ditemukan!", idKategori);
                    continue; // skip
                }

                LabelType tipelabel = null;
                for(LabelType item : DataManager.getInstance().getDataTipeLabel()) {
                    if(item.getId() == idTipeLabel) {
                        tipelabel = item;
                        break;
                    }
                }

                if(tipe == TransactionType.INCOME){
                    data.add(new Pemasukan(id, tipe, jumlah, account, category, tipelabel, tanggal, keterangan, paymentType, status));
                } else if(tipe == TransactionType.EXPANSE) {
                    data.add(new Pengeluaran(id, tipe, jumlah, account, category, tipelabel, tanggal, keterangan, paymentType, status));
                }
            }

            log.info("data transaction berhasil di fetch!");
            return data;

        } catch (SQLException e) {
            log.error("gagal fetch data 'transaction'!", e);
            return null;
        }
    }
    public int insertTransaksi(Transaction trans) {
        String querySql = "INSERT INTO transaction " +
                "(tipe, jumlah, id_akun, id_kategori, id_tipelabel, tanggal, keterangan, metode_transaksi, status) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            koneksi.setAutoCommit(false); // mulai
            try (PreparedStatement ps = koneksi.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, trans.getTipeTransaksi().name());
                ps.setInt(2, trans.getAmount());
                ps.setInt(3, trans.getAkun().getId());
                ps.setInt(4, trans.getKategori().getId());

                if (trans.getLabelType() != null) {
                    ps.setInt(5, trans.getLabelType().getId());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.setString(6, trans.getDate().format(formatter));

                if (trans.getDescription() != null) {
                    ps.setString(7, trans.getDescription());
                } else {
                    ps.setNull(7, Types.VARCHAR);
                }

                if(trans.getPaymentType() != null) {
                    ps.setString(8, trans.getPaymentType().name());
                } else {
                    ps.setNull(8, Types.VARCHAR);
                }

                if(trans.getPaymentStatus() != null) {
                    ps.setString(9, trans.getPaymentStatus().name());
                } else {
                    ps.setNull(9, Types.VARCHAR);
                }

                int affected = ps.executeUpdate();
                if (affected == 0) {
                    koneksi.rollback();
                    return -1;
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        koneksi.commit(); // aman
                        return newId;
                    }
                }

                // proteksi gagal
                koneksi.rollback(); // batal insert
                return -1;
            }

        } catch (SQLException e) {
            try {
                koneksi.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("insert transaction gagal", e);
            return -1;

        } finally {
            try {
                koneksi.setAutoCommit(true); // balikin normal
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }
    public void deleteTransaksi(int id) {
        String querySql = "DELETE FROM transaction WHERE id = ?";

        try (PreparedStatement perintah = Database.getInstance().koneksi.prepareStatement(querySql)){
            perintah.setInt(1, id);
            int affectedRows = perintah.executeUpdate();

            if (affectedRows > 0) {
                DataManager.getInstance().getDataTransaksi().removeIf(t -> t.getId() == id);
            }
            log.info("Data transaction berhasil dihapus!");
        } catch (Exception e) {
            log.error("Data transaction gagal dihapus!", e);
        }
    }
    public Boolean updateTransaksi(Transaction trans){
        String querySql = """
            UPDATE transaction SET
                jumlah = ?,
                id_akun = ?,
                id_kategori = ?,
                id_tipelabel = ?,
                tanggal = ?,
                keterangan = ?,
                metode_transaksi = ?,
                status = ?
            WHERE id = ?;
            """;

        try {
            koneksi.setAutoCommit(false);
            try (PreparedStatement ps = koneksi.prepareStatement(querySql)) {
                ps.setInt(1, trans.getAmount());
                ps.setInt(2, trans.getAkun().getId());
                ps.setInt(3, trans.getKategori().getId());

                if(trans.getLabelType() != null) {
                    ps.setInt(4, trans.getLabelType().getId());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }

                ps.setString(5, trans.getDate().format(formatter));

                if (trans.getDescription() != null) {
                    ps.setString(6, trans.getDescription());
                } else {
                    ps.setNull(6, Types.VARCHAR);
                }

                if(trans.getPaymentType() != null) {
                    ps.setString(7, trans.getPaymentType().name());
                } else {
                    ps.setNull(7, Types.VARCHAR);
                }

                if(trans.getPaymentStatus() != null) {
                    ps.setString(8, trans.getPaymentStatus().name());
                } else {
                    ps.setNull(8, Types.VARCHAR);
                }

                ps.setInt(9, trans.getId());


                int affected = ps.executeUpdate();
                if(affected == 0) {
                    koneksi.rollback();
                    return false;
                }

                koneksi.commit();
                return true;

            }
        } catch (SQLException e) {
            try {
                koneksi.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("update transaction gagal", e);
            return false;

        } finally {
            try {
                koneksi.setAutoCommit(true); // balikin normal
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }

    // [6] >=== manipulasi data account
    public int insertAkun(Account dataAccount) {
        String querySql = """
        INSERT INTO account (nama, warna, icon_path, jumlah, id_mata_uang)
        VALUES (?, ?, ?, ?, ?)
        """;

        try {
            koneksi.setAutoCommit(false); // mulai

            try (PreparedStatement ps = koneksi.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, dataAccount.getName());
                ps.setString(2, Converter.colorToHex(dataAccount.getColor()));
                ps.setString(3, dataAccount.getIconPath());
                ps.setInt(4, dataAccount.getBalance());
                ps.setInt(5, dataAccount.getCurrencyType().getId());

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Insert tidak mengubah data");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Generated key tidak ditemukan");
                    }

                    int newId = rs.getInt(1);
                    koneksi.commit();
                    return newId;
                }
            }
        } catch (SQLException e) {
            try {
                koneksi.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("insert account gagal", e);
            return -1;

        } finally {
            try {
                koneksi.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }
    public ArrayList<Account> fetchAkun() {
        try (Statement stat = koneksi.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM account");

            ArrayList<Account> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                Color warna = Converter.hexToColor(rs.getString("warna"));
                String iconPath = rs.getString("icon_path");
                int jumlah = rs.getInt("jumlah");
                int idMataUang = rs.getInt("id_mata_uang");

                MataUang mataUang = null;
                for(MataUang item : DataManager.getInstance().getDataMataUang()){
                    if(item.getId() == idMataUang) {
                        mataUang = item;
                        break;
                    }
                }
                if (mataUang == null ) {
                    log.warn("fetch account: id_mata_uang={} tidak ditemukan!", idMataUang);
                    continue; // skip
                }

                data.add(new Account(
                        id,
                        nama,
                        warna,
                        new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toString()),
                        iconPath,
                        jumlah,
                        mataUang)
                );
            }
            log.info("data account berhasil di fetch!");
            return data;

        } catch (SQLException e) {
            log.error("gagal fetch data account: ", e);
            return null;
        }
    }
    public Boolean updateSaldoAkun(Account account, int jumlah) {
        String querySql = """
            UPDATE account SET
            jumlah = ?
            WHERE id = ?;
            """;

        try {
            koneksi.setAutoCommit(false);
            try (PreparedStatement ps = koneksi.prepareStatement(querySql)) {
                ps.setInt(1, jumlah);
                ps.setInt(2, account.getId());

                int affected = ps.executeUpdate();
                if(affected == 0) {
                    koneksi.rollback();
                    return false;
                }

                koneksi.commit();
                return true;

            }
        } catch (SQLException e) {
            try {
                koneksi.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("update saldo account gagal", e);
            return false;

        } finally {
            try {
                koneksi.setAutoCommit(true); // balikin normal
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }

    // [7] >=== manipulasi data template
    public ArrayList<Template> fetchTemplate() {
        try (Statement stat = koneksi.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM template");

            ArrayList<Template> dataTemplate = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                TransactionType tipe = TransactionType.valueOf(rs.getString("tipe"));
                String nama = rs.getString("nama");
                int jumlah = rs.getInt("jumlah");
                int idAkun = rs.getInt("id_akun");
                int idKategori = rs.getInt("id_kategori");
                int idTipeLabel = rs.getInt("id_tipelabel");
                String keterangan = rs.getString("keterangan");
                PaymentType paymentType = PaymentType.fromString(rs.getString("metode_transaksi"));
                PaymentStatus paymentStatus = PaymentStatus.fromString(rs.getString("status"));

                Account account = null;
                for(Account item : DataManager.getInstance().getDataAkun()) {
                    if(item.getId() == idAkun) {
                        account = item;
                        break;
                    }
                }

                if(account == null) {
                    log.error("id_akun {} tidak ditemukan!", idAkun);
                    continue;
                }

                Category category = null;
                for(Category ktgr : DataManager.getInstance().getDataKategori()) {
                    if(ktgr.getId() == idKategori) {
                        category = ktgr;
                        break;
                    }
                }

                if(category == null) {
                    log.error("id_kategori {} tidak ditemukan!", idKategori);
                    continue;
                }

                LabelType labelType = null;
                for(LabelType item : DataManager.getInstance().getDataTipeLabel()){
                    if(item.getId() == idTipeLabel) {
                        labelType = item;
                        break;
                    }
                }

                dataTemplate.add(new Template(
                        id,
                        tipe,
                        nama,
                        jumlah,
                        account,
                        category,
                        labelType,
                        keterangan,
                        paymentType,
                        paymentStatus
                ));
            }

            log.info("data template berhasil di fetch!");
            return dataTemplate;

        } catch (SQLException e) {
            log.error("gagal fetch data template: ", e);
            return null;
        }
    }
    public int insertTemplate(Template temp) {
        String quertSql = "INSERT INTO template (tipe, nama, jumlah, id_akun, id_kategori, id_tipelabel, keterangan, metode_transaksi, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            koneksi.setAutoCommit(false);

            try (PreparedStatement ps = koneksi.prepareStatement(quertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, temp.getTipeTransaksi().name());
                ps.setString(2, temp.getNama());
                ps.setInt(3, temp.getJumlah());
                ps.setInt(4, temp.getAkun().getId());
                ps.setInt(5, temp.getKategori().getId());

                if (temp.getTipeLabel() != null) {
                    ps.setInt(6, temp.getTipeLabel().getId());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                if (temp.getKeterangan() != null) {
                    ps.setString(7, temp.getKeterangan());
                } else {
                    ps.setNull(7, Types.VARCHAR);
                }

                if (temp.getPaymentType() != null) {
                    ps.setString(8, temp.getPaymentType().name());
                } else {
                    ps.setNull(8, Types.VARCHAR);
                }

                if (temp.getPaymentStatus() != null) {
                    ps.setString(9, temp.getPaymentStatus().name());
                } else {
                    ps.setNull(9, Types.VARCHAR);
                }

                if(ps.executeUpdate() == 0) {
                    throw new SQLException("insert tidak mengubah data");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if(!rs.next()) {
                        throw new SQLException("generated key tidak ditemukan");
                    }

                    int newId = rs.getInt(1);
                    koneksi.commit();
                    return newId;
                }
            }
        } catch (SQLException e) {
            try {
                koneksi.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("insert template gagal", e);
            return -1;
        } finally {
            try {
                koneksi.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }
}