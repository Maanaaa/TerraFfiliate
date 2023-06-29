package fr.manaa.utils.loading;

import com.google.gson.*;
import fr.manaa.*;
import fr.manaa.affiliate.events.*;
import fr.manaa.affiliator.cmds.administration.*;
import fr.manaa.affiliator.cmds.menus.*;
import fr.manaa.affiliator.events.menus.affiliation.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

import java.util.*;

public class PluginInitializer {

    private Main main;
    private File affilConfigFile;
    private FileConfiguration affilConfig;

    public PluginInitializer(Main main) {
        this.main = main;
    }

    private Gson gson;

    public void initialize() {
        // CREATION OF CONFIG.YML FILE
        this.main.saveDefaultConfig();

        Objects.requireNonNull(this.main.getCommand("affil")).setExecutor(new HomeMenu(main));
        Objects.requireNonNull(this.main.getCommand("affilreload")).setExecutor(new Reload(main));
        main.getServer().getPluginManager().registerEvents(new AffiliationMenuOpened(main), main);
        main.getServer().getPluginManager().registerEvents(new JoinWithAffiliateAdress(main), main);

        // DataBase Connection
        DatabaseManager databaseManager = new DatabaseManager((this.main));
        databaseManager.connect();
    }
}