package fr.manaa.utils.loading;

import fr.manaa.*;
import org.jdbi.v3.core.*;

import java.io.*;
import java.net.*;
import java.sql.*;

public class DatabaseManager {

    private Main main;

    private Connection connection;

    public DatabaseManager(Main main) {
        this.main = main;
    }

    public void createTables() {
        try {
            Connection connection = getConnection();
            DatabaseMetaData metaData = connection.getMetaData();

            // Vérifier si la table `affiliation` existe
            ResultSet tableResultSet = metaData.getTables(null, null, "affiliation", null);
            if (!tableResultSet.next()) {
                // La table `affiliation` n'existe pas, donc nous la créons
                Statement statement = connection.createStatement();
                String sql = "CREATE TABLE `affiliation` (" +
                        "`id` int(11) NOT NULL AUTO_INCREMENT," +
                        "`is_created` tinyint(1) DEFAULT NULL COMMENT 'true/false si l''addresse est crée ou non'," +
                        "`player` text NOT NULL COMMENT 'propriétaire de l''addresse'," +
                        "`affiliation_address` text DEFAULT NULL COMMENT 'adresse d''affiliation'," +
                        "`affiliated` int(11) DEFAULT NULL COMMENT 'nombre de joueurs affiliés'," +
                        "`winner` int(11) DEFAULT NULL COMMENT 'nombre de fois où le joueur a gagné le concours'," +
                        "PRIMARY KEY (`id`)" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
                statement.executeUpdate(sql);
                statement.close();
                System.out.println("Table `affiliation` créée avec succès !");
            } else {
                System.out.println("La table `affiliation` existe déjà !");
            }

            tableResultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    public void createIndexes() {
        try {
            Connection connection = getConnection();
            DatabaseMetaData metaData = connection.getMetaData();

            // Vérifier si l'index pour la table `affiliation` existe
            ResultSet indexResultSet = metaData.getIndexInfo(null, null, "affiliation", true, false);
            boolean indexExists = false;
            while (indexResultSet.next()) {
                if (indexResultSet.getString("INDEX_NAME").equalsIgnoreCase("PRIMARY")) {
                    indexExists = true;
                    break;
                }
            }
            indexResultSet.close();

            if (!indexExists) {
                // L'index pour la table `affiliation` n'existe pas, donc nous le créons
                Statement statement = connection.createStatement();
                String sql = "ALTER TABLE `affiliation` ADD PRIMARY KEY (`id`);";
                statement.executeUpdate(sql);
                statement.close();
                System.out.println("Index pour la table `affiliation` créé avec succès !");
            } else {
                System.out.println("L'index pour la table `affiliation` existe déjà !");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void connect(){
        String host = main.getConfig().getString("database.host");
        int port = main.getConfig().getInt("database.port");
        String database = main.getConfig().getString("database.database");

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;

        String username = main.getConfig().getString("database.user");
        String password = main.getConfig().getString("database.password");

        assert username != null;
        assert password != null;
        Jdbi jdbi = Jdbi.create(jdbcUrl, username, password);

        jdbi.useHandle(handle -> {
            // Tester la connexion en exécutant une requête simple
            handle.execute("SELECT 1");
            System.out.println(" ");
            System.out.println(" ");
            System.out.println(" ");
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("   TERRAFFILIATE");
            System.out.println("Connexion réussie !");
            System.out.println("   TERRAFFILIATE");
            System.out.println(" ");
            System.out.println(" ");
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("");
        });
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