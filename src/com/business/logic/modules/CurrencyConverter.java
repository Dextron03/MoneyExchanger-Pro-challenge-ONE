package com.business.logic.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class CurrencyConverter {
    private static final int SCALE = 2; // Escala para redondear
    private static final String SUPPORTED_CURRENCIES = "USD, BRL, COP";

    private final String mainCurrency; // Moneda principal
    private final String currencyToConvert; // Moneda a convertir
    private final double amountToConvert; // Cantidad a convertir
    private final HttpClient httpClient; // Cliente HTTP
    private final Gson gson; // Instancia de Gson
    private HttpResponse<String> response; // Respuesta HTTP

    public CurrencyConverter(String mainCurrency, String currencyToConvert, double amountToConvert) throws IOException, InterruptedException {
        validateCurrency(mainCurrency);
        validateCurrency(currencyToConvert);
        validateAmount(amountToConvert);

        this.mainCurrency = mainCurrency;
        this.currencyToConvert = currencyToConvert;
        this.amountToConvert = amountToConvert;

        // Carga la clave de la API desde el archivo .env
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + mainCurrency;

        httpClient = HttpClient.newHttpClient();
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void validateCurrency(String currency) {
        if (!SUPPORTED_CURRENCIES.contains(currency)) {
            throw new IllegalArgumentException("Unsupported currency code: " + currency);
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    public double getConversionRate() {
        String json = response.body();
        Map<String, Object> data = gson.fromJson(json, Map.class);
        Map<String, Double> conversionRates = (Map<String, Double>) data.get("conversion_rates");

        if (!conversionRates.containsKey(currencyToConvert)) {
            throw new IllegalArgumentException("Conversion rate not found for: " + currencyToConvert);
        }

        return conversionRates.get(currencyToConvert);
    }

    public double convertCurrency() {
        double rate = getConversionRate();
        BigDecimal amountBigDecimal = BigDecimal.valueOf(amountToConvert);
        BigDecimal rateBigDecimal = BigDecimal.valueOf(rate);

        BigDecimal resultBigDecimal = amountBigDecimal.multiply(rateBigDecimal);
        resultBigDecimal = resultBigDecimal.setScale(SCALE, RoundingMode.HALF_UP);

        return resultBigDecimal.doubleValue();
    }
}
