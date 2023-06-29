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