package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

// TODO [IMPORTANT]:
// CurrencyApiClient masih versi sync + manual load.
// Ke depannya:
// - Init kurs harus async saat startup
// - Tambahin retry + fallback (offline JSON)
// - Pastikan thread-safe sebelum dipakai multi-service
// Jangan refactor sekarang, lanjut fitur dulu!

public class CurrencyApiClient {
    private static final Logger log = LoggerFactory.getLogger(CurrencyApiClient.class);

    // value
    private BigDecimal usdToIdr;
    private BigDecimal eurToIdr;

    // constructror
    private CurrencyApiClient() {
        // load offline
        try {
            loadRatesFromJson();
        } catch (Exception e) {
            log.warn("Kurs offline tidak tersedia, akan fetch di background.", e);
        }

        // Fetch terbaru di background thread
        Thread t = new Thread(() -> {
            try {
                fetchAndSaveAllRates();
            } catch (Exception ex) {
                log.error("gagal fetch kurs terbaru", ex);
            }
        });
        t.setName("CurrencyAPI-Thread");
        t.start();

        log.info("CurrencyApiClient siap digunakan!");
    }

    private static class Holder {
        private static final CurrencyApiClient INSTANCE = new CurrencyApiClient();
    }

    public static CurrencyApiClient getInstance() {
        return Holder.INSTANCE;
    }


    public String getExchangeRate(String from, String to) throws Exception {
        String url = "https://api.frankfurter.app/latest?from=" + from + "&to=" + to;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                return response.body();
            } else {
                log.error("Gagal fetch kurs, HTTP code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            log.warn("User sedang offline! tidak bisa fetch kurs " + from + " -> " + to);
            return null;
        }
    }

    public double extractRate(String json, String currency) {
        Gson gson = new Gson();
        ExchangeResponse resp = gson.fromJson(json, ExchangeResponse.class);
        return resp.getRates().get(currency);
    }

    public void fetchAndSaveAllRates() {
        try {
            String usdJson = getExchangeRate("USD", "IDR");
            String eurJson = getExchangeRate("EUR", "IDR");

            if(usdJson == null || eurJson == null) {
                log.warn("Menggunakan data kurs lokal karena fetch gagal!");
                return;
            }

            usdToIdr = BigDecimal.valueOf(extractRate(usdJson, "IDR"));
            eurToIdr = BigDecimal.valueOf(extractRate(eurJson, "IDR"));

            // root object
            JsonObject root = new JsonObject();
            root.addProperty("fetchedAt", java.time.LocalDateTime.now().toString());

            // rates object
            JsonObject rates = new JsonObject();
            rates.addProperty("USD", usdToIdr.toPlainString());
            rates.addProperty("EUR", eurToIdr.toPlainString());

            root.add("rates", rates);

            Path path = Path.of("data/exchange_rates.json");
            Files.createDirectories(path.getParent());
            Files.writeString(path, new Gson().toJson(root));

            log.info("User sedang Online! Data dari CurrencyApiClient berhasil difetch");
            log.info("File JSON berhasil disimpan di: " + path.toAbsolutePath());

        } catch (Exception e) {
            log.error("Gagal fetch & save kurs", e);
        }
    }


    public void loadRatesFromJson() throws Exception {
        Path path = Path.of("data/exchange_rates.json");
        if (!Files.exists(path)) {
            log.warn("File JSON kurs tidak ditemukan, lewati load offline!");
            return;
        }
        String json = Files.readString(path);
        JsonObject root = new Gson().fromJson(json, JsonObject.class);

        JsonObject rates = root.getAsJsonObject("rates");

        usdToIdr = new BigDecimal(rates.get("USD").getAsString());
        eurToIdr = new BigDecimal(rates.get("EUR").getAsString());

        log.info("Kurs berhasil di-load dari JSON offline. fetchedAt={}",
                root.get("fetchedAt").getAsString());
    }

    public BigDecimal convert(BigDecimal amount, String from, String to) {

        if (usdToIdr == null || eurToIdr == null) {
            throw new IllegalStateException("Exchange rate belum di-load");
        }

        if (from.equals("USD") && to.equals("IDR")) {
            return amount.multiply(usdToIdr);
        }

        if (from.equals("EUR") && to.equals("IDR")) {
            return amount.multiply(eurToIdr);
        }

        if (from.equals("IDR") && to.equals("USD")) {
            return amount.divide(usdToIdr, 2, RoundingMode.HALF_UP);
        }

        if (from.equals("IDR") && to.equals("EUR")) {
            return amount.divide(eurToIdr, 2, RoundingMode.HALF_UP);
        }

        if(from.equals("IDR") && to.equals("IDR")) {
            return amount;
        }

        throw new IllegalArgumentException("Mata uang tidak didukung");
    }
}