package controller;

import dataflow.DataManager;
import helper.MyPopup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Transaksi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.File;
import java.util.ResourceBundle;

public class ReportControl implements Initializable {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Logger log = LoggerFactory.getLogger(ReportControl.class);

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    private void handleExportCSV(ActionEvent event) {
        List<Transaksi> transaksiList = DataManager.getInstance().getDataTransaksi();
        exportToCSV(transaksiList);
    }

    private void exportToCSV(List<Transaksi> list) {
        File folder = new File("export-data");
        if (!folder.exists() && !folder.mkdirs()) {
            log.error("Gagal membuat folder export-data");
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
            return;
        }

        File file = new File(folder, "akun-report.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
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

            log.info("CSV berhasil dibuat: " + file.getAbsolutePath());
            MyPopup.showSucces("Export berhasil!", "Export data berhasil!");

        } catch (IOException e) {
            MyPopup.showDanger("Gagal!", "Terjadi kesalahan!");
            log.error("CSV gagal diexport!", e);
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

}
