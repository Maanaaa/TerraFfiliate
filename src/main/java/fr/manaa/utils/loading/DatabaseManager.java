package fr.manaa.utils.loading;

import fr.manaa.*;

import java.sql.*;

public class DatabaseConnect {

    private Main main;

    private Connection connection;

    public DatabaseConnect(Main main) {
        this.main = main;
    }

    public void connect(){
        String url = "";
        String user = "";
        String password = "";

        try {
            connection = DriverManager.getConnection(url,user,password);
            // Connection successfull
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

}
