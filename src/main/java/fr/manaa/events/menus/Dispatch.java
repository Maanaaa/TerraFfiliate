package fr.manaa.events.menus;

import fr.manaa.*;
import fr.manaa.utils.loading.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class Dispatch implements Listener {
    private Main main;

    public Dispatch(Main main) {
        this.main = main;
    }

    public static void createSubdomain(String subdomain, String ipAddress) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Auth-Email", EMAIL);
            connection.setRequestProperty("X-Auth-Key", API_KEY);
            connection.setDoOutput(true);

            String requestBody = "{\"type\":\"A\",\"name\":\"" + subdomain + ".terracraft.fr\",\"content\":\"" + ipAddress + "\",\"ttl\":1,\"proxied\":false}";

            connection.getOutputStream().write(requestBody.getBytes());

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                System.out.println("Sous domaine créé avec succès: " + response.toString());
            } else {
                System.out.println("Problème lors de la création du sous domaine " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'exception selon tes besoins
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) {
            return;
        }
        String title = Objects.requireNonNull(main.getConfig().getString("menus.enableAffil.title")).replace("&", "§");
        Inventory inventory = e.getInventory();
        if(e.getView().getTitle().equals(main.getConfig().getString("menus.enableAffil.title").replace("&","§"))){
            e.setCancelled(true);
            if(clickedItem.getItemMeta().getDisplayName().equals(main.getConfig().getString("menus.enableAffil.items.3.display-name").replace("&","§"))) {
                p.updateInventory();
                p.closeInventory();

                String player = p.getDisplayName();
                // CLOUDFLARE API

                // SET TO TRUE PLAYER AFFILIATE
                try (Connection connection = getConnection()) {
                    String selectQuery = "SELECT is_created FROM Affiliation WHERE player = ?";
                    try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                        selectStatement.setString(1, player);
                        try (ResultSet resultSet = selectStatement.executeQuery()) {
                            if (resultSet.next()) {
                                boolean isCreated = resultSet.getBoolean("is_created");
                                if (!isCreated) {
                                    String updateQuery = "UPDATE Affiliation SET is_created = true WHERE player = ?";
                                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                        updateStatement.setString(1, player);
                                        updateStatement.executeUpdate();

                                        String playerSubdomain = p.getDisplayName().replace("_","-").toLowerCase();
                                        String subdomain = playerSubdomain;

                                        // Associer sous-domaine au joueur dans la bdd

                                        // Création du sous-domaine sur Cloudflare

                                    }
                                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                        updateStatement.setString(1, player);
                                        updateStatement.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                } catch (SQLException es) {
                    es.printStackTrace();
                }
                p.updateInventory();

                for(String messages : main.getConfig().getStringList("affiliateEnabled.message")) {
                    p.sendMessage(messages.replace("&","§").replace("%playerip%", player.replace("_","").toLowerCase()));
                }
                if (main.getConfig().getBoolean("affiliateEnabled.playSound.enable")) {
                    Sound joinSound = Sound.valueOf(main.getConfig().getString("affiliateEnabled.playSound.sound"));
                    p.playSound(p.getLocation(), joinSound, 1.0f, 1.0f);
                }
                if (main.getConfig().getBoolean("affiliateEnabled.fireworks.enable")){
                    Location location = p.getLocation();
                    Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
                    FireworkMeta meta = firework.getFireworkMeta();
                    String color1 = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.color-1");
                    String color2 = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.color-2");
                    String color3 = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.color-3");

                    String type = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.type");
                    FireworkEffect effect = FireworkEffect.builder().withColor(Color.AQUA, Color.WHITE, Color.ORANGE).with(Type.valueOf(type)).build();
                    meta.addEffect(effect);
                    meta.setPower(1);
                    firework.setFireworkMeta(meta);
                }

            }
        }


    }

    public Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://172.17.0.4:3306/affiliation";
        String username = "root";
        String password = "j57jQ22YsG#h";

        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    private static final String API_KEY = "f3165c7389aa9e88621d3507fe5b213dedd59";
    private static final String EMAIL = "manya.th@icloud.com";
    private static final String ZONE_ID = "17447cd96f6d79f72ab54258d8b35921";
    private static final String API_URL = "https://api.cloudflare.com/client/v4/zones/" + ZONE_ID + "/dns_records";


}
