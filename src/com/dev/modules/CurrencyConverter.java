package com.dev.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class CurrencyConverter {
    private String currencyToConvert;
    private float amountToConvert;
    private HttpClient httpClient; // Cliente HTTP
    private Gson gson; // Instancia de Gson
    private HttpResponse<String> response; // Respuesta HTTP

    public CurrencyConverter(String currencyToConvert, float amountToConvert) throws IOException, InterruptedException {
        this.currencyToConvert = currencyToConvert; // Moneda a convertir
        this.amountToConvert = amountToConvert; // cantidad a Convertir

        // Carga la clave de la API desde el archivo .env
        Dotenv dotenv = Dotenv.load();
        String myAPIKey = dotenv.get("API_KEY");
        String url = "https://v6.exchangerate-api.com/v6/" + myAPIKey + "/latest/"+currencyToConvert;

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

    public void getCurrencyDescription() {
        // Puedes usar el mismo conjunto de datos para diferentes operaciones
        String json = response.body();
        Map<String, Object> data = gson.fromJson(json, Map.class);

        // Extrae "conversion_rates" para obtener descripciones de monedas
        Map<String, Double> conversionRates = (Map<String, Double>) data.get("conversion_rates");

        System.out.println("¿Qué moneda quieres convertir?");
        for (Map.Entry<String, Double> entry : conversionRates.entrySet()) {
            switch (entry.getKey()) {
                case "USD":
                    System.out.println(entry.getKey() + " - Dólar Estadounidense");
                    break;

                case "EUR":
                    System.out.println(entry.getKey() + " - Euro");
                    break;

                case "DOP":
                    System.out.println(entry.getKey() + " - Peso Dominicano");
                    break;
            }
        }



    }
}
