package fr.manaa;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CloudflareAPI {

    private static Main main;
    private static String API_EMAIL;
    private static String API_KEY;
    private static String ZONE_ID;

    public CloudflareAPI(Main main, String API_EMAIL, String API_KEY, String ZONE_ID) {
        CloudflareAPI.main = main;
        CloudflareAPI.API_EMAIL = API_EMAIL;
        CloudflareAPI.API_KEY = API_KEY;
        CloudflareAPI.ZONE_ID = ZONE_ID;
    }

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

            String domain = main.getConfig().getString("cloudflare.domain");

            String requestBody = "{\"type\":\"A\",\"name\":\"" + subdomain + "."+domain+"\",\"content\":\"" + ipAddress + "\",\"proxied\":false}";
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

            String domain = main.getConfig().getString("cloudflare.domain");
            int port = main.getConfig().getInt("cloudflare.port");

            String srvRequestBody = "{\"type\": \"SRV\", \"data\": {\"service\": \"_minecraft\", \"proto\": \"_tcp\", \"name\": \""+subdomain+"."+domain+"\", \"priority\": 1, \"weight\": 5, \"port\": "+port+", \"target\": \""+subdomain+"."+domain+"\"}}";
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