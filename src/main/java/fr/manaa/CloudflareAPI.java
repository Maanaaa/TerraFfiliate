package fr.manaa;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CloudflareAPI {
    private static final String API_EMAIL = "manya.th@icloud.com";
    private static final String API_KEY = "f3165c7389aa9e88621d3507fe5b213dedd59";
    private static final String ZONE_ID = "17447cd96f6d79f72ab54258d8b35921";

    public static void createSubdomain(String subdomain, String ipAddress) {
        try {
            URL url = new URL("https://api.cloudflare.com/client/v4/zones/" + ZONE_ID + "/dns_records");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("X-Auth-Email", API_EMAIL);
            connection.setRequestProperty("X-Auth-Key", API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            String requestBody = "{\"type\":\"A\",\"name\":\"" + subdomain + ".terracraft.fr\",\"content\":\"" + ipAddress + "\",\"proxied\":false}";
            byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(requestBodyBytes);
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Enregistrement A créé avec succès pour le sous-domaine " + subdomain);

                createSRVRecord(subdomain);
            } else {
                System.out.println("Erreur lors de la création de l'enregistrement A : " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du sous-domaine : " + e.getMessage());
        }
    }

    private static void createSRVRecord(String subdomain) {
        try {
            URL url = new URL("https://api.cloudflare.com/client/v4/zones/" + ZONE_ID + "/dns_records");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("X-Auth-Email", API_EMAIL);
            connection.setRequestProperty("X-Auth-Key", API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            String srvRequestBody = "{\"type\": \"SRV\", \"data\": {\"service\": \"_minecraft\", \"proto\": \"_tcp\", \"name\": \""+subdomain+".terracraft.fr\", \"priority\": 1, \"weight\": 5, \"port\": 2021, \"target\": \""+subdomain+".terracraft.fr\"}}";
            byte[] srvRequestBodyBytes = srvRequestBody.getBytes(StandardCharsets.UTF_8);

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(srvRequestBodyBytes);
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Enregistrement SRV créé avec succès pour le sous-domaine " + subdomain);
            } else {
                System.out.println("Erreur lors de la création de l'enregistrement SRV : " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            System.out.println("Erreur lors de la création de l'enregistrement SRV : " + e.getMessage());
        }
    }

}