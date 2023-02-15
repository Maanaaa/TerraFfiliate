package fr.manaa.utils.loading;

import fr.manaa.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import java.io.IOException;

public class PluginInitializer {

    private Main main;
    private File affilConfigFile;
    private FileConfiguration affilConfig;

    public PluginInitializer(Main main){this.main = main;}

    public void initialize(){
        // CREATION OF CONFIG.YML FILE
        this.main.saveDefaultConfig();
        createAffilConfig();
    }

    public FileConfiguration getCustomConfig(){
        return this.affilConfig;
    }

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




    public void initializeDisable(){



    }

}
