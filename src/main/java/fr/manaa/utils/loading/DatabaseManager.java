package fr.manaa.utils.loading;

import fr.manaa.*;
import org.bukkit.entity.*;

import java.sql.*;

public class DatabaseManager {

    private Main main;

    private Connection connection;

    public DatabaseManager(Main main) {
        this.main = main;
    }
    //database:
    // url: ""
    //  host: localhost
    //  port: 3306
    //  database: db
    //  user: username
    //  password: pass
    //
    //globalmessaBges:




    public void connect(){
        String url = main.getConfig().getString("database.url")
        String host = main.getConfig().getString("database.host");
        int port = main.getConfig().getInt("database.port");
        String database = main.getConfig().getString("database.database");
        String user = main.getConfig().getString("database.user");
        String password = main.getConfig().getString("database.pass");

        try {
            connection = DriverManager.getConnection(url,user,password);
            // Connection successfull
        } catch (SQLException e){
            e.printStackTrace();
            // Connecton error
        }

    }

    public void createAddress(Player p){

    }

}
