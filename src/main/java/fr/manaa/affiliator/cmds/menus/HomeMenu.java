package fr.manaa.affiliator.cmds.menus;

import fr.manaa.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.sql.*;
import java.util.*;

public class HomeMenu implements CommandExecutor {
    private Main main;

    public HomeMenu(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        int size = main.getConfig().getInt("menus.enableAffil.size");
        String title = Objects.requireNonNull(main.getConfig().getString("menus.enabledAffil.title")).replace("&", "§");

        int sizeEnabled = main.getConfig().getInt("menus.enableAffil.size");
        String titleEnabled = Objects.requireNonNull(main.getConfig().getString("menus.enabledAffil.title")).replace("&", "§");

        if (sender instanceof Player) {

            Player p = (Player) sender;
            String player = p.getDisplayName();

            try (Connection connection = getConnection()) {
                String selectQuery = "SELECT is_created FROM affiliation WHERE player = ?";
                try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                    selectStatement.setString(1, player);
                    try (ResultSet resultSet = selectStatement.executeQuery()) {
                        if (resultSet.next()) {
                            boolean isCreated = resultSet.getBoolean("is_created");
                            if (isCreated) {
                                // La variable is_created est déjà activée
                                // Autres actions à exécuter lorsque is_created est déjà sur true
                                Inventory enabledAffilMenu = Bukkit.createInventory(null, sizeEnabled, titleEnabled);
                                registerItem(p, enabledAffilMenu, 1, "enabledAffil");
                                registerItem(p, enabledAffilMenu, 2, "enabledAffil");
                                registerItem(p, enabledAffilMenu, 3, "enabledAffil");
                                registerItem(p, enabledAffilMenu, 4, "enabledAffil");
                                registerItem(p, enabledAffilMenu, 5, "enabledAffil");

                                p.openInventory(enabledAffilMenu);
                            } else {
                                // La variable is_created n'est pas activée, l'ajouter à la base de données
                                String updateQuery = "UPDATE Affiliation SET is_created = true WHERE player = ?";
                                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                    updateStatement.setString(1, player);
                                    updateStatement.executeUpdate();
                                    System.out.println("La variable is_created a été activée avec succès");

                                    // Vérifier et générer un sous-domaine unique
                                    String affiliationAddress = generateUniqueAffiliationAddress(player, connection);
                                    System.out.println("Adresse d'affiliation créée : " + affiliationAddress);

                                    Inventory enableAffilMenu = Bukkit.createInventory(null, size, title);
                                    registerItem(p, enableAffilMenu, 1, "enableAffil");
                                    registerItem(p, enableAffilMenu, 2, "enableAffil");
                                    registerItem(p, enableAffilMenu, 3, "enableAffil");
                                    p.openInventory(enableAffilMenu);
                                }
                            }
                        } else {
                            // Le joueur n'existe pas dans la base de données, l'ajouter avec is_created = true
                            String insertQuery = "INSERT INTO affiliation (is_created, player, affiliation_address) VALUES (true, ?, ?)";

                            String playerEdited = player.replace("_", "").toLowerCase();

                            // Vérifier et générer un sous-domaine unique
                            String affiliationAddress = generateUniqueAffiliationAddress(player, connection);
                            System.out.println("Adresse d'affiliation créée : " + affiliationAddress);

                            String adress = "163.5.143.62";

                            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                                insertStatement.setString(1, player);
                                insertStatement.setString(2, affiliationAddress);
                                insertStatement.executeUpdate();

                                // Action exécutée lorsque le joueur est ajouté avec succès
                                System.out.println("Le joueur a été ajouté avec succès avec is_created = true");

                                Inventory enableAffilMenu = Bukkit.createInventory(null, size, title);
                                registerItem(p, enableAffilMenu, 1, "enableAffil");
                                registerItem(p, enableAffilMenu, 2, "enableAffil");
                                registerItem(p, enableAffilMenu, 3, "enableAffil");
                                p.openInventory(enableAffilMenu);

                                CloudflareAPI.createSubdomain(playerEdited, adress);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    // Vérifie et génère un sous-domaine unique
    private String generateUniqueAffiliationAddress(String player, Connection connection) throws SQLException {
        String playerEdited = player.replace("_", "").toLowerCase();
        String affiliationAddress = playerEdited + ".terracraft.fr";
        String originalAffiliationAddress = affiliationAddress;

        // Vérifier si le sous-domaine existe déjà
        boolean domainExists = true;
        int suffix = 1;
        while (domainExists) {
            String selectDomainQuery = "SELECT affiliation_address FROM affiliation WHERE affiliation_address = ?";
            try (PreparedStatement selectDomainStatement = connection.prepareStatement(selectDomainQuery)) {
                selectDomainStatement.setString(1, affiliationAddress);
                try (ResultSet domainResultSet = selectDomainStatement.executeQuery()) {
                    if (domainResultSet.next()) {
                        // Le sous-domaine existe déjà, ajouter un chiffre supplémentaire
                        suffix++;
                        affiliationAddress = originalAffiliationAddress + suffix;
                    } else {
                        // Le sous-domaine est unique, sortir de la boucle
                        domainExists = false;
                    }
                }
            }
        }

        return affiliationAddress;
    }



    // ITEMS

    public void registerItem(Player player, Inventory menu, int itemNumber, String config){
        newItem(Objects.requireNonNull(player.getPlayer()),
                menu,
                main.getConfig().getString("menus."+config+".items."+itemNumber+".material"),
                Objects.requireNonNull(main.getConfig().getString("menus."+config+".items."+itemNumber+".display-name")).replace("&", "§"),
                main.getConfig().getStringList("menus."+config+".items."+itemNumber+".lore"),
                main.getConfig().getIntegerList("menus."+config+".items."+itemNumber+".slots"));
    }

    public void newItem(Player player, Inventory inventory, String material, String name, List<String> description, List<Integer> slots) {
        // SI ON VEUT DONNER LA TËTE DU JOUEUR
        if(material.equals("PLAYER_HEAD:%player%")){
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
            assert itemMeta != null;
            itemMeta.setOwningPlayer(player);
            itemMeta.setDisplayName(name);

            String playerip = player.getDisplayName().replace("_","").toLowerCase();
            for(int i = 0;i < description.size(); i++){
                description.set(i, description.get(i).replace("&","§").replace("%playerip%", playerip).replace("%player%",player.getDisplayName()));
            }

            itemMeta.setLore(description);
            item.setItemMeta(itemMeta);

            for(int slot : slots){
                inventory.setItem(slot, item);
            }
        }
        // SI ON NE VEUT PAS
        else{
            ItemStack item = new ItemStack(Material.valueOf(material));
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(name);

            String playerip = player.getDisplayName().replace("_","").toLowerCase();
            for(int i = 0;i < description.size(); i++){
                description.set(i, description.get(i).replace("&","§").replace("%playerip%", playerip).replace("%player%",player.getDisplayName()));
            }

            itemMeta.setLore(description);
            item.setItemMeta(itemMeta);

            for(int slot : slots){
                inventory.setItem(slot, item);
            }
        }


    }

    public Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://172.17.0.2:3306/affiliation";
        String username = "root";
        String password = "j57jQ22YsG#h";

        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}


