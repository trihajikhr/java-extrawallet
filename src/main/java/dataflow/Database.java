package dataflow;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.*;
import model.enums.PaymentStatus;
import model.enums.PaymentType;
import model.enums.TransactionType;
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

    private Connection myConnection;

    // [0] >=== instance
    public static Database getInstance() {
        if(instance == null) {
            instance = new Database();
            log.info("objek database dibuat!");
        }
        return instance;
    }
    public Connection getConnection() {
        return myConnection;
    }

    // [1] >=== objek database singleton
    private Database () {
        try {
            Files.createDirectories(AppPaths.DATABASE_DIR);

            this.myConnection = DriverManager.getConnection(JDBC_URL + AppPaths.DB_FILE.toAbsolutePath());

            // aktifkan opsi foreign key (tidak aktif secara default!)
            try (Statement perintah = myConnection.createStatement()) {
                perintah.execute("PRAGMA foreign_keys = ON");
            }

            createCategoryTable();
            createCurrencyTable();
            createLabelTypeTable();
            createAccountTable();
            createTransactionTable();
            createTemplateTable();

            log.info("Database siap digunakan di: {}", AppPaths.DB_FILE.toAbsolutePath());

        } catch (SQLException e) {
            log.error("Database gagal!",  e);
        } catch (IOException e) {
            log.error("pembuatan folder database gagal!", e);
        }
    }

    // [2] >=== modularisasi statement create table
    private void createCategoryTable() {
        try (Statement perintah = myConnection.createStatement()){
            String querySql =
                """
                CREATE TABLE IF NOT EXISTS "category" (
                    "id"	INTEGER NOT NULL UNIQUE,
                    "type"	TEXT NOT NULL,
                    "name"	TEXT NOT NULL,
                    "icon_path"	TEXT NOT NULL,
                    "color"	TEXT NOT NULL,
                    PRIMARY KEY("id" AUTOINCREMENT)
                )
                """;
            perintah.executeUpdate(querySql);
            log.info("table category berhasil dibuat!");

        } catch (Exception e) {
            log.error("table category gagal dibuat: " , e);
        }
    }
    private void createCurrencyTable() {
        try(Statement perintah = myConnection.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "currency" (
                "id"	INTEGER NOT NULL UNIQUE,
                "code"	TEXT NOT NULL,
                "name"	TEXT NOT NULL,
                "symbol"	TEXT NOT NULL,
                "fraction_digits"	INTEGER NOT NULL,
                PRIMARY KEY("id" AUTOINCREMENT)
            )
            """;

            perintah.executeUpdate(querySql);
            log.info("table mata_uang berhasil dibuat!");

        } catch (SQLException e){
            log.error("table mata_uang gagal dibuat: ", e);
        }
    }
    private void createLabelTypeTable() {
        try (Statement perintah = myConnection.createStatement()){
            String querySql =
                """
                CREATE TABLE IF NOT EXISTS "labeltype" (
                    "id"	INTEGER NOT NULL UNIQUE,
                    "name"	TEXT NOT NULL UNIQUE,
                    "color"	TEXT NOT NULL,
                    PRIMARY KEY("id" AUTOINCREMENT)
                )
                """;
            perintah.executeUpdate(querySql);
            log.info("table tipelabel berhasil dibuat!");

        } catch (SQLException e) {
            log.error("table tipelabel gagal dibuat: ", e);
        }
    }
    private void createAccountTable() {
        try (Statement perintah = myConnection.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "account" (
                "id"	INTEGER NOT NULL UNIQUE,
                "name"	TEXT NOT NULL,
                "color"	TEXT NOT NULL,
                "icon_path"	TEXT NOT NULL,
                "balance"	INTEGER NOT NULL,
                "id_currency"	INTEGER NOT NULL,
                PRIMARY KEY("id" AUTOINCREMENT),
                CONSTRAINT "account_to_currency" FOREIGN KEY("id_currency") REFERENCES "currency"("id")
            )
            """;
            perintah.executeUpdate(querySql);
            log.info("table account berhasil dibuat!");

        } catch (SQLException e) {
            log.error("table account gagal dibuat: ", e);
        }
    }
    private void createTransactionTable() {
        try (Statement perintah = myConnection.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "transaction_record" (
                "id"	INTEGER NOT NULL UNIQUE,
                "type"	TEXT NOT NULL,
                "amount"	INTEGER NOT NULL,
                "id_account"	INTEGER NOT NULL,
                "id_category"	INTEGER NOT NULL,
                "id_labeltype"	INTEGER,
                "date"	TEXT NOT NULL,
                "description"	TEXT,
                "payment_type"	TEXT,
                "payment_status"	TEXT,
                PRIMARY KEY("id" AUTOINCREMENT),
                CONSTRAINT "transaction_to_account" FOREIGN KEY("id_account") REFERENCES "account"("id") ON DELETE CASCADE,
                CONSTRAINT "transaction_to_category" FOREIGN KEY("id_category") REFERENCES "category"("id") ON DELETE RESTRICT,
                CONSTRAINT "transaction_to_labeltype" FOREIGN KEY("id_labeltype") REFERENCES "labeltype"("id") ON DELETE RESTRICT
            )
            """;
            perintah.executeUpdate(querySql);
            log.info("table transaction berhasil dibuat!");

        } catch (SQLException e){
            log.error("table transaction gagal dibuat: ", e);
        }
    }
    private void createTemplateTable() {
        try (Statement perintah = myConnection.createStatement()) {
            String querySql =
            """
            CREATE TABLE IF NOT EXISTS "template" (
                "id"	INTEGER NOT NULL UNIQUE,
                "type"	TEXT NOT NULL,
                "name"	TEXT NOT NULL,
                "amount"	INTEGER NOT NULL,
                "id_account"	INTEGER NOT NULL,
                "id_category"	INTEGER NOT NULL,
                "id_labeltype"	INTEGER,
                "description"	TEXT,
                "payment_type"	TEXT,
                "payment_status"	TEXT,
                PRIMARY KEY("id" AUTOINCREMENT),
                CONSTRAINT "template_to_account" FOREIGN KEY("id_account") REFERENCES "account"("id") ON DELETE CASCADE,
                CONSTRAINT "template_to_category" FOREIGN KEY("id_category") REFERENCES "category"("id") ON DELETE CASCADE,
                CONSTRAINT "template_to_labeltype" FOREIGN KEY("id_labeltype") REFERENCES "labeltype"("id") ON DELETE CASCADE
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
        String querySql = "INSERT INTO labeltype (name, color) VALUES (?, ?)";

        try {
            myConnection.setAutoCommit(false);
            try (PreparedStatement ps = myConnection.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {

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
                    myConnection.commit();
                    return newId;
                }
            }
        } catch (SQLException e) {
            try {
                myConnection.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal!", ex);
            }
            log.error("insert label gagal!", e);
            return -1;

        } finally {
            try {
                myConnection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("gagal reset autoCommit!", e);
            }
        }
    }
    public ArrayList<LabelType> fetchTipeLabel() {
        try (Statement stat = myConnection.createStatement()){
            ResultSet rs = stat.executeQuery("SELECT * FROM labeltype");
            ArrayList<LabelType> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("name");
                String hex = rs.getString("color");

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
        try (Statement stat = myConnection.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM category");
            ArrayList<Category> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String tipe = rs.getString("type");
                String nama = rs.getString("name");
                String iconPath = rs.getString("icon_path");
                Color warna = Converter.hexToColor(rs.getString("color"));

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
    public ArrayList<Transaction> fetchTransaction() {
        try (Statement stat = myConnection.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM transaction_record");

            ArrayList<Transaction> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                TransactionType tipe = TransactionType.valueOf(rs.getString("type"));
                BigDecimal jumlah = rs.getBigDecimal("amount");
                int idAkun = rs.getInt("id_account");
                int idKategori = rs.getInt("id_category");
                int idTipeLabel = rs.getInt("id_labeltype");
                String tanggalSet = rs.getString("date");
                String keterangan = rs.getString("description");
                PaymentType paymentType = PaymentType.fromString(rs.getString("payment_type"));
                PaymentStatus status = PaymentStatus.fromString(rs.getString("payment_status"));

                LocalDate tanggal = LocalDate.parse(tanggalSet, Converter.formatter);

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

                data.add(new Transaction(id, tipe, jumlah, account, category, tipelabel, tanggal, keterangan, paymentType, status));
            }

            log.info("data transaction berhasil di fetch!");
            return data;

        } catch (SQLException e) {
            log.error("gagal fetch data 'transaction'!", e);
            return null;
        }
    }
    public int insertTransaksi(Transaction trans) {
        String querySql = "INSERT INTO transaction_record " +
                "(type, amount, id_account, id_category, id_labeltype, date, description, payment_type, payment_status) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            myConnection.setAutoCommit(false); // mulai
            try (PreparedStatement ps = myConnection.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, trans.getTransactionType().name());
                ps.setBigDecimal(2, trans.getAmount());
                ps.setInt(3, trans.getAccount().getId());
                ps.setInt(4, trans.getCategory().getId());

                if (trans.getLabelType() != null) {
                    ps.setInt(5, trans.getLabelType().getId());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.setString(6, trans.getDate().format(Converter.formatter));

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
                    myConnection.rollback();
                    return -1;
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        myConnection.commit(); // aman
                        return newId;
                    }
                }

                // proteksi gagal
                myConnection.rollback(); // batal insert
                return -1;
            }

        } catch (SQLException e) {
            try {
                myConnection.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("insert transaction gagal", e);
            return -1;

        } finally {
            try {
                myConnection.setAutoCommit(true); // balikin normal
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }
    public void deleteTransaksi(int id) {
        String querySql = "DELETE FROM transaction_record WHERE id = ?";

        try (PreparedStatement perintah = Database.getInstance().myConnection.prepareStatement(querySql)){
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
            UPDATE transaction_record SET
                amount = ?,
                id_account = ?,
                id_category = ?,
                id_labeltype = ?,
                date = ?,
                description = ?,
                payment_type = ?,
                payment_status = ?
            WHERE id = ?;
            """;

        try {
            myConnection.setAutoCommit(false);
            try (PreparedStatement ps = myConnection.prepareStatement(querySql)) {
                ps.setBigDecimal(1, trans.getAmount());
                ps.setInt(2, trans.getAccount().getId());
                ps.setInt(3, trans.getCategory().getId());

                if(trans.getLabelType() != null) {
                    ps.setInt(4, trans.getLabelType().getId());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }

                ps.setString(5, trans.getDate().format(Converter.formatter));

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
                    myConnection.rollback();
                    return false;
                }

                myConnection.commit();
                return true;

            }
        } catch (SQLException e) {
            try {
                myConnection.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("update transaction gagal", e);
            return false;

        } finally {
            try {
                myConnection.setAutoCommit(true); // balikin normal
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }

    // [6] >=== manipulasi data account
    public int insertAkun(Account dataAccount) {
        String querySql = """
        INSERT INTO account (name, color, icon_path, balance, id_currency)
        VALUES (?, ?, ?, ?, ?)
        """;

        try {
            myConnection.setAutoCommit(false); // mulai

            try (PreparedStatement ps = myConnection.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, dataAccount.getName());
                ps.setString(2, Converter.colorToHex(dataAccount.getColor()));
                ps.setString(3, dataAccount.getIconPath());
                ps.setBigDecimal(4, dataAccount.getBalance());
                ps.setInt(5, dataAccount.getCurrencyType().getId());

                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Insert tidak mengubah data");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Generated key tidak ditemukan");
                    }

                    int newId = rs.getInt(1);
                    myConnection.commit();
                    return newId;
                }
            }
        } catch (SQLException e) {
            try {
                myConnection.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("insert account gagal", e);
            return -1;

        } finally {
            try {
                myConnection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }
    public ArrayList<Account> fetchAkun() {
        try (Statement stat = myConnection.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM account");

            ArrayList<Account> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("name");
                Color warna = Converter.hexToColor(rs.getString("color"));
                String iconPath = rs.getString("icon_path");
                BigDecimal jumlah = rs.getBigDecimal("balance");
                int idMataUang = rs.getInt("id_currency");

                Currency currency = null;
                for(Currency item : DataManager.getInstance().getDataMataUang()){
                    if(item.getId() == idMataUang) {
                        currency = item;
                        break;
                    }
                }
                if (currency == null ) {
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
                        currency)
                );
            }
            log.info("data account berhasil di fetch!");
            return data;

        } catch (SQLException e) {
            log.error("gagal fetch data account: ", e);
            return null;
        }
    }
    public Boolean updateSaldoAkun(Account account, BigDecimal jumlah) {
        String querySql = """
            UPDATE account SET
            balance = ?
            WHERE id = ?;
            """;

        try {
            myConnection.setAutoCommit(false);
            try (PreparedStatement ps = myConnection.prepareStatement(querySql)) {
                ps.setBigDecimal(1, jumlah);
                ps.setInt(2, account.getId());

                int affected = ps.executeUpdate();
                if(affected == 0) {
                    myConnection.rollback();
                    return false;
                }

                myConnection.commit();
                return true;

            }
        } catch (SQLException e) {
            try {
                myConnection.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("update saldo account gagal", e);
            return false;

        } finally {
            try {
                myConnection.setAutoCommit(true); // balikin normal
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }

    // [7] >=== manipulasi data template
    public ArrayList<Template> fetchTemplate() {
        try (Statement stat = myConnection.createStatement()) {
            ResultSet rs = stat.executeQuery("SELECT * FROM template");

            ArrayList<Template> dataTemplate = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                TransactionType tipe = TransactionType.valueOf(rs.getString("type"));
                String nama = rs.getString("name");
                BigDecimal jumlah = rs.getBigDecimal("amount");
                int idAkun = rs.getInt("id_account");
                int idKategori = rs.getInt("id_category");
                int idTipeLabel = rs.getInt("id_labeltype");
                String keterangan = rs.getString("description");
                PaymentType paymentType = PaymentType.fromString(rs.getString("payment_type"));
                PaymentStatus paymentStatus = PaymentStatus.fromString(rs.getString("payment_status"));

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
        String quertSql = "INSERT INTO template (type, name, amount, id_account, id_category, id_labeltype, description, payment_type, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            myConnection.setAutoCommit(false);

            try (PreparedStatement ps = myConnection.prepareStatement(quertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, temp.getTransactionType().name());
                ps.setString(2, temp.getName());
                ps.setBigDecimal(3, temp.getAmount());
                ps.setInt(4, temp.getAccount().getId());
                ps.setInt(5, temp.getCategory().getId());

                if (temp.getLabelType() != null) {
                    ps.setInt(6, temp.getLabelType().getId());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                if (temp.getDescription() != null) {
                    ps.setString(7, temp.getDescription());
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
                    myConnection.commit();
                    return newId;
                }
            }
        } catch (SQLException e) {
            try {
                myConnection.rollback();
            } catch (SQLException ex) {
                log.error("rollback database gagal", ex);
            }
            log.error("insert template gagal", e);
            return -1;
        } finally {
            try {
                myConnection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("gagal reset autoCommit", e);
            }
        }
    }
}