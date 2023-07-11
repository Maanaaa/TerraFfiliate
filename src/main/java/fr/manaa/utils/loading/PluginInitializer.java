package fr.manaa.utils.loading;

import com.google.gson.*;
import fr.manaa.*;
import fr.manaa.affiliator.cmds.all.*;
import fr.manaa.affiliator.events.menus.affiliation.*;
import fr.manaa.utils.loading.placeholderapi.*;
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

        // DataBase Connection
        DatabaseManager databaseManager = new DatabaseManager(main);
        databaseManager.createTables();
        databaseManager.createIndexes();
        databaseManager.connect();

        // CloudFlare API
        String API_EMAIL = main.getConfig().getString("cloudflare.API_EMAIL");
        String API_KEY = main.getConfig().getString("cloudflare.API_KEY");
        String ZONE_ID = main.getConfig().getString("cloudflare.ZONE_ID");
        CloudflareAPI cloudflareApi = new CloudflareAPI(this.main, API_EMAIL, API_KEY, ZONE_ID);

        // PlaceHolderAPI
        new AffiliationPlaceholder(main).register();

    }


}