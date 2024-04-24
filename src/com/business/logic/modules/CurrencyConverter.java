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
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    private String mainCurrency; // Moneda principal
    private String currencyToConvert; // Moneda a convertir
    private double amountToConvert; // Cantidad a convertir
    private HttpClient httpClient; // Cliente HTTP
    private Gson gson; // Instancia de Gson
    private HttpResponse<String> response; // Respuesta HTTP

    public CurrencyConverter(String mainCurrency, String currencyToConvert, double amountToConvert) throws IOException, InterruptedException {
        this.mainCurrency = mainCurrency; // Moneda principal
        this.currencyToConvert = currencyToConvert; // Moneda a convertir
        this.amountToConvert = amountToConvert; // Cantidad a convertir

        // Carga la clave de la API desde el archivo .env
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + mainCurrency;

        // Inicializa el cliente HTTP y Gson como variables de instancia
        httpClient = HttpClient.newHttpClient();
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        // Envía la solicitud HTTP y almacena la respuesta como variable de instancia
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public Map<String, Double> getConversionRate() {
        String json = response.body();
        Map<String, Object> data = gson.fromJson(json, Map.class);
        Map<String, Double> conversionRates = (Map<String, Double>) data.get("conversion_rates");

        Map<String, Double> rateForCurrency = new HashMap<>();

        if (mainCurrency.equals("USD") || mainCurrency.equals("BRL") || mainCurrency.equals("COP")) {
            for (Map.Entry<String, Double> entry : conversionRates.entrySet()) {
                if (currencyToConvert.equals(entry.getKey())) {
                    rateForCurrency.put(entry.getKey(), entry.getValue());
                    break;
                }
            }
        } else {
            System.out.println("This program doesn't yet support that currency code.");
        }

        return rateForCurrency;
    }

    public double convertCurrency() {
        Map<String, Double> exchangeRate = getConversionRate();
        double result = 0;

        for (Map.Entry<String, Double> entry : exchangeRate.entrySet()) {
            // Convertir el resultado a BigDecimal para mayor precisión
            BigDecimal amountBigDecimal = BigDecimal.valueOf(amountToConvert);
            BigDecimal rateBigDecimal = BigDecimal.valueOf(entry.getValue());

            // Calcular el resultado y redondear a 2 decimales
            BigDecimal resultBigDecimal = amountBigDecimal.multiply(rateBigDecimal);
            resultBigDecimal = resultBigDecimal.setScale(2, RoundingMode.HALF_UP);

            // Convertir de nuevo a double
            result = resultBigDecimal.doubleValue();
        }

        return result;
    }
}
