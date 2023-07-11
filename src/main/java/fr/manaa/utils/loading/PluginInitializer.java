package fr.manaa.utils.loading;

import com.google.gson.*;
import fr.manaa.*;
import fr.manaa.affiliate.events.*;
import fr.manaa.affiliator.cmds.menus.*;
import fr.manaa.affiliator.events.menus.affiliation.*;
import org.bukkit.configuration.file.FileConfiguration;


import java.io.File;

import java.util.*;

public class PluginInitializer {

    private Main main;
    private File affilConfigFile;
    private FileConfiguration affilCo1nfig;

    public PluginInitializer(Main main) {
        this.main = main;
    }

    private Gson gson;

    public void initialize() {
        // CREATION OF CONFIG.YML FILE
        this.main.saveDefaultConfig();

        Objects.requireNonNull(this.main.getCommand("affiliation")).setExecutor(new Global(main));
        //Objects.requireNonNull(this.main.getCommand("affiliation")).setExecutor(new Reload(main));
        main.getServer().getPluginManager().registerEvents(new AffiliationMenuOpened(main), main);
        main.getServer().getPluginManager().registerEvents(new JoinWithAffiliateAdress(main), main);

        // DataBase Connection
        DatabaseManager databaseManager = new DatabaseManager(main);
        databaseManager.createTables();
        databaseManager.createIndexes();
        databaseManager.connect();
    }
}