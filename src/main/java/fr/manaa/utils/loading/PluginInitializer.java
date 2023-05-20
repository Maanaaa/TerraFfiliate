package fr.manaa.utils.loading;

import com.google.gson.*;
import fr.manaa.*;
import fr.manaa.cmds.menus.*;
import fr.manaa.events.affiliationSystem.*;
import fr.manaa.events.menus.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import java.io.IOException;
import java.util.*;

public class PluginInitializer {

    private Main main;
    private File affilConfigFile;
    private FileConfiguration affilConfig;

    public PluginInitializer(Main main){this.main = main;}
    private Gson gson;
    public void initialize(){
        // CREATION OF CONFIG.YML FILE
        this.main.saveDefaultConfig();
        createAffilConfig();
        // INITIALIZE THE /AFFIL COMMANDS
        Objects.requireNonNull(this.main.getCommand("affil")).setExecutor(new HomeMenu(main));
        //Objects.requireNonNull(this.main.getCommand("affilreload")).setExecutor(new Reload(main));
        main.getServer().getPluginManager().registerEvents(new RegisterPlayerInFile(main), main);
        main.getServer().getPluginManager().registerEvents(new Dispatch(main), main);
        main.getServer().getPluginManager().registerEvents(new NewPlayerAffiliated(main), main);

        // DataBase Connection
        DatabaseManager databaseManager = new DatabaseManager((this.main));

        databaseManager.connect();
    }

    public FileConfiguration getCustomConfig(){
        return this.affilConfig;
    }

    // CREATION OF AFFIL.YML FILE
    private void createAffilConfig() {
        affilConfigFile = new File(this.main.getDataFolder(), "players.yml");
        if(!(affilConfigFile.exists())){
            affilConfigFile.getParentFile().mkdirs();
            this.main.saveResource("players.yml", false);
        }

        affilConfig = new YamlConfiguration();

        try{
            affilConfig.load(affilConfigFile);
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }
}