package fr.manaa.cmds.menus;

import fr.manaa.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.io.*;
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

        if(sender instanceof Player){
            // Get the file players.yml
            //File configFile = new File(main.getDataFolder(), "players.yml");
            //YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            Player p = (Player) sender;
            String player = p.getDisplayName();
            boolean hasIp = true;//config.getBoolean("affiliation-adress-list." + player + ".enable");
            try (Connection connection = getConnection()) {
                String selectQuery = "SELECT is_created FROM Affiliation WHERE player = ?";
                try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                    selectStatement.setString(1, player);
                    try (ResultSet resultSet = selectStatement.executeQuery()) {
                        if (resultSet.next()) {
                            boolean isCreated = resultSet.getBoolean("is_created");
                            if (isCreated) {
                                // La variable is_created est déjà activée, effectue ton action ici
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
                                    Inventory enableAffilMenu = Bukkit.createInventory(null, size, title);
                                    registerItem(p, enableAffilMenu, 1, "enableAffil");
                                    registerItem(p, enableAffilMenu, 2, "enableAffil");
                                    registerItem(p, enableAffilMenu, 3, "enableAffil");
                                    p.openInventory(enableAffilMenu);
                                }
                            }
                        } else {
                            // Le joueur n'existe pas dans la base de données, l'ajouter avec is_created = true
                            String insertQuery = "INSERT INTO Affiliation (is_created, player) VALUES (true, ?)";
                            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                                insertStatement.setString(1, player);
                                insertStatement.executeUpdate();
                                // Action exécutée lorsque le joueur est ajouté avec succès
                                System.out.println("Le joueur a été ajouté avec succès avec is_created = true");
                                Inventory enableAffilMenu = Bukkit.createInventory(null, size, title);

                                registerItem(p, enableAffilMenu, 1, "enableAffil");
                                registerItem(p, enableAffilMenu, 2, "enableAffil");
                                registerItem(p, enableAffilMenu, 3, "enableAffil");

                                p.openInventory(enableAffilMenu);
                                // Autres actions à exécuter lorsque le joueur est ajouté avec succès
                            }
                        }
                    }
                }
            } catch (SQLException es) {
                es.printStackTrace();
                // Gérer l'exception selon ton besoin
            }

        }

        return false;
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
        String jdbcUrl = "jdbc:mysql://172.17.0.4:3306/affiliation";
        String username = "root";
        String password = "j57jQ22YsG#h";

        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}


