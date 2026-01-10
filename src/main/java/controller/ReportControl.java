package controller;

import dataflow.DataManager;
import helper.MyPopup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import service.AppPaths;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ReportControl implements Initializable {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Logger log = LoggerFactory.getLogger(ReportControl.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    String timestamp = LocalDateTime.now().format(formatter);

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    // [0] >=== EXPORT CSV
    @FXML
    private void handleExportCSV(ActionEvent event) {
        List<Transaksi> transaksiList = DataManager.getInstance().getDataTransaksi();
        exportToCSV(transaksiList);
    }

    private void exportToCSV(List<Transaksi> list) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan File CSV");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv")
        );

        fileChooser.setInitialFileName(
                "akun-report_" + timestamp + ".csv"
        );

        fileChooser.setInitialDirectory(
                AppPaths.ROOT.toFile()
        );

        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            log.info("User membatalkan export CSV");
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {

            writer.write("ID,TipeTransaksi,Jumlah,Akun,Kategori,TipeLabel,Tanggal,Keterangan,PaymentType,PaymentStatus");
            writer.newLine();

            for (Transaksi t : list) {
                writer.write(String.join(",",
                        String.valueOf(t.getId()),
                        t.getTipeTransaksi() != null ? t.getTipeTransaksi().name() : "",
                        String.valueOf(t.getJumlah()),
                        t.getAkun() != null ? t.getAkun().getNama() : "",
                        t.getKategori() != null ? t.getKategori().getNama() : "",
                        t.getTipelabel() != null ? t.getTipelabel().getNama() : "",
                        t.getTanggal() != null ? t.getTanggal().format(DATE_FORMAT) : "",
                        escapeCSV(t.getKeterangan()),
                        t.getPaymentType() != null ? t.getPaymentType().name() : "",
                        t.getPaymentStatus() != null ? t.getPaymentStatus().name() : ""
                ));
                writer.newLine();
            }

            log.info("CSV berhasil dibuat: {}", file.getAbsolutePath());
            MyPopup.showSucces("Export berhasil!", "Export data berhasil!");

        } catch (IOException e) {
            log.error("CSV gagal diexport!", e);
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
        }
    }

    private static String escapeCSV(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            escaped = "\"" + escaped + "\"";
        }
        return escaped;
    }

    // [1] >=== IMPORT CSV
    @FXML
    private void handleImportCSV(ActionEvent evt) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import CSV");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        // ambil stage dari button
        Stage stage = (Stage) ((Node) evt.getSource())
                .getScene()
                .getWindow();

        File selectedFile = fileChooser.showOpenDialog(stage);

        // user pencet cancel
        if (selectedFile == null) {
            return;
        }

        importFromCSV(selectedFile);
    }

    public void importFromCSV(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            Boolean allSucces = true;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {

                // skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = parseCSVLine(line);
                Transaksi transaksi = mapToTransaksi(data);
                transaksi.showData();
                allSucces = allSucces && DataManager.getInstance().importTransaksiFromCSV(transaksi);
            }

            if(allSucces) {
                log.info("data berhasil diimport");
                MyPopup.showSucces("Import berhasil", "Semua data berhasil diimport");
            } else {
                log.info("data terimport sebagian");
                MyPopup.showDanger("Terjadi kesalahan!" , "Ada data gagal diimport!");
            }

        } catch (Exception e) {
            log.error("data gagal diimport!", e);
            MyPopup.showDanger("Import gagal", "Terjadi kesalahan saat import");
            e.printStackTrace();
        }
    }

    private String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private Transaksi mapToTransaksi(String[] d) {
        Transaksi t = new Transaksi();

        // Kalau ID auto increment → jangan set
        // t.setId(Integer.parseInt(d[0]));

        t.setTipeTransaksi(TipeTransaksi.valueOf(d[1].trim().toUpperCase()));
        t.setJumlah(Integer.parseInt(d[2]));

        t.setAkun(findAkun(d[3]));
        t.setKategori(findKategori(d[4]));
        t.setTipelabel(findLabel(d[5]));

        t.setTanggal(LocalDate.parse(d[6], DATE_FORMAT));
        t.setKeterangan(unescapeCSV(d[7]));

        t.setPaymentType(parsePaymentType(d[8].trim().toUpperCase()));
        t.setPaymentStatus(parsePaymentStatus(d[9].trim().toUpperCase()));

        return t;
    }

    private Akun findAkun(String target) {
        for(Akun akun : DataManager.getInstance().getDataAkun()) {
            if(akun.getNama().equalsIgnoreCase(target)) {
                return akun;
            }
        }
        return null;
    }

    private Kategori findKategori(String target){
        for(Kategori ktgr : DataManager.getInstance().getDataKategori()) {
            if(ktgr.getNama().equalsIgnoreCase(target)) {
                return ktgr;
            }
        }
        return null;
    }

    private TipeLabel findLabel(String target) {
        for(TipeLabel label : DataManager.getInstance().getDataTipeLabel()) {
            if(label.getNama().equalsIgnoreCase(target)) {
                return label;
            }
        }
        return null;
    }

    private PaymentType parsePaymentType(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return PaymentType.valueOf(value.trim().toUpperCase());
    }

    private PaymentStatus parsePaymentStatus(String value) {
        if(value == null || value.trim().isEmpty()) {
            return null;
        }
        return PaymentStatus.valueOf((value.trim().toUpperCase()));
    }


    private static String unescapeCSV(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        value = value.trim();

        // Kalau dibungkus tanda kutip
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        // Kembalikan double-quote jadi single-quote
        value = value.replace("\"\"", "\"");

        return value;
    }


}
