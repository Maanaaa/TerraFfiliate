package fr.manaa.utils.loading;

import fr.manaa.*;
import fr.manaa.cmds.administration.*;
import fr.manaa.cmds.menus.*;
import fr.manaa.events.*;
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

    public void initialize(){
        // CREATION OF CONFIG.YML FILE
        this.main.saveDefaultConfig();
        createAffilConfig();
        // INITIALIZE THE /AFFIL COMMAND
        Objects.requireNonNull(this.main.getCommand("affil")).setExecutor(new HomeMenu(main));
        Objects.requireNonNull(this.main.getCommand("affilreload")).setExecutor(new Reload(main));
        main.getServer().getPluginManager().registerEvents(new RegisterPlayerInFile(main), main);
        main.getServer().getPluginManager().registerEvents(new Dispatch(main), main);
    }

    public FileConfiguration getCustomConfig(){
        return this.affilConfig;
    }

    // CREATION OF AFFIL.YML FILE
    private void createAffilConfig() {
        affilConfigFile = new File(this.main.getDataFolder(), "affil.yml");
        if(!(affilConfigFile.exists())){
            affilConfigFile.getParentFile().mkdirs();
            this.main.saveResource("affil.yml", false);
        }

        affilConfig = new YamlConfiguration();

        try{
            affilConfig.load(affilConfigFile);
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }


}
