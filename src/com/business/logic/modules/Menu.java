package com.business.logic.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Menu {
    private static final Map<String, String> CURRENCY_DESCRIPTIONS = new HashMap<>() {{
        put("USD", "Dólar estadounidense");
        put("COP", "Peso colombiano");
        put("BRL", "Real brasileño");
    }};

    private final HttpClient httpClient; // Cliente HTTP
    private final Gson gson; // Instancia de Gson
    private final HttpResponse<String> response; // Respuesta HTTP

    public Menu() throws IOException, InterruptedException {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";

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

    public void showCurrencyMenu() {
        Map<String, Double> conversionRates = getConversionRates();
        System.out.println("""
                               ¿Qué moneda necesitas convertir? 
                               (Escriba salir para detener el programa)
                               """);
        for (String key : CURRENCY_DESCRIPTIONS.keySet()) {
            if (conversionRates.containsKey(key)) {
                System.out.println(key + " - " + CURRENCY_DESCRIPTIONS.get(key));
            }
        }
        System.out.println("Escribe el código de moneda que deseas:");
    }

    public String askForAmount(String currencyCode) {
        return "¿Cuánta cantidad de " + currencyCode + " deseas convertir?";
    }

    public void showConversionOptions(String currency) {
        Map<String, Double> conversionRates = getConversionRates();
        System.out.println("¿A qué moneda deseas convertir?");
        for (String key : CURRENCY_DESCRIPTIONS.keySet()) {
            if (!key.equals(currency) && conversionRates.containsKey(key)) {
                System.out.println(key + " - " + CURRENCY_DESCRIPTIONS.get(key));
            }
        }
    }

    private Map<String, Double> getConversionRates() {
        String json = response.body();
        Map<String, Object> data = gson.fromJson(json, Map.class);
        return (Map<String, Double>) data.get("conversion_rates");
    }
}
