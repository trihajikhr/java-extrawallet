package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class CurrencyApiClient {
    private static final Logger log = LoggerFactory.getLogger(CurrencyApiClient.class);
    private static CurrencyApiClient instance;

    // value
    private double usdToIdr;
    private double eurToIdr;

    // constructro
    private CurrencyApiClient() {}

    private static class Holder {
        private static final CurrencyApiClient INSTANCE = new CurrencyApiClient();
    }

    public static CurrencyApiClient getInstance() {
        log.info("currencyClient API berhasil dibuat!");
        return Holder.INSTANCE;
    }


    public String getExchangeRate(String from, String to) throws Exception {
        String url = "https://api.frankfurter.app/latest?from=" + from + "&to=" + to;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body(); // JSON mentah
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

            usdToIdr = extractRate(usdJson, "IDR");
            eurToIdr = extractRate(eurJson, "IDR");

            // Buat objek JSON untuk disimpan
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("USD", usdToIdr);
            jsonObj.addProperty("EUR", eurToIdr);

            Path path = Path.of("data/exchange_rates.json");
            Files.createDirectories(path.getParent());
            Files.writeString(path, new Gson().toJson(jsonObj));

            log.info("File JSON berhasil disimpan di: " + path.toAbsolutePath());

        } catch (Exception e) {
            log.info("Gagal fetch & save kurs.");
            e.printStackTrace();
        }
    }

    public void loadRatesFromJson() throws Exception {
        Path path = Path.of("data/exchange_rates.json");
        String json = Files.readString(path);
        JsonObject obj = new Gson().fromJson(json, JsonObject.class);

        usdToIdr = obj.get("USD").getAsDouble();
        eurToIdr = obj.get("EUR").getAsDouble();

        log.info("Kurs berhasil di-load dari JSON offline.");
    }

    public double convert(double amount, String from, String to) {
        if(from.equals("USD") && to.equals("IDR")) return amount * usdToIdr;
        if(from.equals("EUR") && to.equals("IDR")) return amount * eurToIdr;
        if(from.equals("IDR") && to.equals("USD")) return amount / usdToIdr;
        if(from.equals("IDR") && to.equals("EUR")) return amount / eurToIdr;

        throw new IllegalArgumentException("Mata uang tidak didukung");
    }
}