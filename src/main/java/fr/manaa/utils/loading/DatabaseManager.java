package fr.manaa.utils.loading;

import fr.manaa.*;
import fr.manaa.events.menus.*;
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
        String jdbcUrl = "jdbc:mysql://172.17.0.4:3306/affiliation";
        String username = "root";
        String password = "j57jQ22YsG#h";

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
        String jdbcUrl = "jdbc:mysql://172.17.0.4:3306/affiliation";
        String username = "root";
        String password = "j57jQ22YsG#h";

        return DriverManager.getConnection(jdbcUrl, username, password);
    }






    }

