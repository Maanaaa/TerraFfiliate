package fr.manaa.affiliator.cmds.all;

import fr.manaa.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jdbi.v3.core.*;

import java.sql.*;
import java.util.*;

public class Affiliation implements CommandExecutor {
    private Main main;

    public Affiliation(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            int size = main.getConfig().getInt("menu.enableAffil.size");
            String title = Objects.requireNonNull(main.getConfig().getString("menu.enabledAffil.title")).replace("&", "§");

            int sizeEnabled = main.getConfig().getInt("menu.enableAffil.size");
            String titleEnabled = Objects.requireNonNull(main.getConfig().getString("menu.enabledAffil.title")).replace("&", "§");

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
                                String insertQuery = "INSERT INTO affiliation (is_created, player, affiliation_address, affiliated) VALUES (true, ?, ?, 0)";
                                String insertQuery2 = "INSERT INTO winners (player, winning_number) VALUES (?, ?)";
                                String playerEdited = player.replace("_", "").toLowerCase();

                                // Vérifier et générer un sous-domaine unique
                                String affiliationAddress = generateUniqueAffiliationAddress(player, connection);
                                System.out.println("Adresse d'affiliation créée : " + affiliationAddress);

                                String adress = "154.51.39.202";

                                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery))  {
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
                                // ADD PLAYER TO WINNERS TABLE
                                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery2))  {
                                    insertStatement.setString(1, player);
                                    insertStatement.setInt(2, 0);
                                    insertStatement.executeUpdate();

                                    // Action exécutée lorsque le joueur est ajouté avec succès
                                    System.out.println("Le joueur a été ajouté avec succès à la table 'winners'");
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
            // ----------
            // RELOAD COMMAND
        } else if(args[0].equalsIgnoreCase("reload")){
            if(sender.hasPermission("affiliation.reload")){
                main.reloadConfig();
                sender.sendMessage("§6Configuration de TerraFfiliate rechargée !");
            }
            // ----------
            // STATS VIEW COMMAND
        }  else if (args[0].equalsIgnoreCase("view")) {
            if (args.length == 2) {
                if (sender.hasPermission("affiliation.view")) {
                    String targetName = args[1];
                    // Vérifier si le joueur cible est connecté
                    Player targetPlayer = Bukkit.getPlayer(targetName);
                        try (Connection connection = getConnection()) {
                            String query = "SELECT affiliated FROM affiliation WHERE player = ?";
                            try (PreparedStatement statement = connection.prepareStatement(query)) {
                                statement.setString(1, targetPlayer.getDisplayName());
                                try (ResultSet resultSet = statement.executeQuery()) {
                                    if (resultSet.next()) {
                                        int affiliatedPlayerCount = resultSet.getInt("affiliated");
                                        // Afficher les statistiques
                                        List<String> description = main.getConfig().getStringList("competition.view.message");
                                        String playerip = targetPlayer.getDisplayName().replace("_", "").toLowerCase();
                                        int playerRank = getAffiliationRank(targetPlayer.getDisplayName());
                                        sender.sendMessage("\"§7§n                             \"\n" +
                                                "§6Statistiques §7"+ targetPlayer.getName()+"\n" +
                                                "§7➤ §bAdresse §f: §7"+playerip+".terracraft.fr\n" +
                                                "§7➤ §bNombre d'affiliés §f: §e"+String.valueOf(getAffiliatedPlayerCount(targetPlayer.getName()))+"\n" +
                                                "§7➤ §bParcours remportés §f: §e"+String.valueOf(getWinnerCount(targetPlayer.getName()))+"\n"+
                                                "§7➤ §bClassement §f: §7§e"+playerRank+"\n" +
                                                "§7§n                             \"");
                                    }
                                }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                }
            }
            // ----------
            // SET WINNER COMMAND
        } else if(args[0].equalsIgnoreCase("winner")){
            if(args.length == 2){
                if(sender.hasPermission("affiliation.winner")){
                    String targetName = args[1];
                    Player targetPlayer = Bukkit.getPlayer(targetName);
                    try {
                        Connection connection = getConnection();
                        String sql = "UPDATE `winners` SET `winning_number` = `winning_number` + 1 WHERE `player` = ?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setString(1, targetName);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();
                        Bukkit.getServer().broadcastMessage("\n \n    §e§l⚡ §b§lPLAY.TERRACRAFT.FR §e§l⚡ \n§7➤ §bLe joueur §7"+targetName+" §ba remporté le concours d'affiliation mensuel ! Félicitations !\n \n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            // ---------
            // RESET DATABASE COMMAND
        }else if(args[0].equalsIgnoreCase("reset")){
            List<String> operators = main.getConfig().getStringList("operator");
            if((!(sender instanceof Player)) || operators.contains(((Player) sender).getDisplayName())) {
                resetDataBase();
                sender.sendMessage("§cVous avez réinitialisé la base de donnée !");
                Bukkit.getServer().broadcastMessage("§7➤ §cLe système d'affiliation a été réinitialisé");
            }else{sender.sendMessage("§cErreur, vous ne faites pas partie de la liste des opérateurs.");}
        }else if(args[0].equalsIgnoreCase("rules")){
            List<String> messages = main.getConfig().getStringList("competition.rules.message");
            for (String message : messages) {
                sender.sendMessage(message.replace("&","§"));
            }
        }
        return false;
    }

    public void resetDataBase() {
        try {
            Connection connection = getConnection();
            String sql = "DELETE FROM `affiliation`";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
            System.out.println("Base de donnée réinitialisée");
            System.out.println("Tous les joueurs ont été supprimés de la table `affiliation`");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                Objects.requireNonNull(main.getConfig().getString("menu." + config + ".items." + itemNumber + ".material")),
                Objects.requireNonNull(Objects.requireNonNull(main.getConfig().getString("menu." + config + ".items." + itemNumber + ".display-name")).replace("&", "§").replace("%afNB%", "10")),
                main.getConfig().getStringList("menu."+config+".items."+itemNumber+".lore"),
                main.getConfig().getIntegerList("menu."+config+".items."+itemNumber+".slots"));
    }

    public void newItem(Player player, Inventory inventory, String material, String name, List<String> description, List<Integer> slots) {
        ItemStack item;
        ItemMeta itemMeta;

        if (material.equals("PLAYER_HEAD:%player%")) {
            item = new ItemStack(Material.PLAYER_HEAD);
            itemMeta = item.getItemMeta();
            assert itemMeta instanceof SkullMeta;
            SkullMeta skullMeta = (SkullMeta) itemMeta;

            skullMeta.setOwningPlayer(player);
        } else {
            item = new ItemStack(Material.valueOf(material));
            itemMeta = item.getItemMeta();
        }

        assert itemMeta != null;
        itemMeta.setDisplayName(name);
        String playerip = player.getDisplayName().replace("_", "").toLowerCase();
        int playerRank = getAffiliationRank(player.getDisplayName());

        List<String> replacedDescription = new ArrayList<>();
        for (String line : description) {
            line = line.replace("&", "§");
            line = line.replace("%playerip%", playerip);
            line = line.replace("%player%", player.getName());
            line = line.replace("%top%", String.valueOf(playerRank));
            line = line.replace("%cashprize%", String.valueOf(main.getConfig().getString("competition.cashPrize")));
            line = line.replace("%winner%", String.valueOf(getWinnerCount(player.getName())));
            line = line.replace("%affiliateNumber%", String.valueOf(getAffiliatedPlayerCount(player.getName())));
            replacedDescription.add(line);
        }

        itemMeta.setLore(replacedDescription);
        item.setItemMeta(itemMeta);

        for (int slot : slots) {
            inventory.setItem(slot, item);
        }
    }


    public int getWinnerCount(String playerName) {
        try (Connection connection = getConnection()) {
            String query = "SELECT winning_number FROM winners WHERE player = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("winning_number");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0; // Retourne 0 si le joueur n'est pas trouvé ou s'il y a une erreur
    }


    public int getAffiliatedPlayerCount(String playerName) {
        try (Connection connection = getConnection()) {
            String query = "SELECT affiliated FROM affiliation WHERE player = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("affiliated");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0; // Retourne 0 si le joueur n'est pas trouvé ou s'il y a une erreur
    }

    public int getAffiliationRank(String playerName) {
        try (Connection connection = getConnection()) {
            String query = "SELECT player, affiliated FROM affiliation ORDER BY affiliated DESC";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    int playerRank = 1;
                    while (resultSet.next()) {
                        String currentPlayerName = resultSet.getString("player");
                        if (currentPlayerName.equalsIgnoreCase(playerName)) {
                            return playerRank;
                        }
                        playerRank++;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // Retourne -1 si le joueur n'est pas trouvé ou s'il y a une erreur
    }


    public Connection getConnection() throws SQLException {
        String host = main.getConfig().getString("database.host");
        int port = main.getConfig().getInt("database.port");
        String database = main.getConfig().getString("database.database");

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;

        String username = main.getConfig().getString("database.user");
        String password = main.getConfig().getString("database.password");

        assert username != null;
        assert password != null;
        Jdbi jdbi = Jdbi.create(jdbcUrl, username, password);

        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}


