package fr.manaa.events;

import fr.manaa.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.io.*;
import java.util.*;

public class RegisterPlayerInFile implements Listener {

    private Main main;

    public RegisterPlayerInFile(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = (Player) e.getPlayer();

        // Get the configuration file
        File configFile = new File(main.getDataFolder(), "affil.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        String player = p.getName();

        // Add player to file if player isn't in
        if(!(config.contains("affiliation-adress-list." + player))){
            config.set("affiliation-adress-list." + player + ".enable", false);
        }
        else{
            return;
        }

        // Save modifications
        try {
            config.save(configFile);
        } catch (IOException et) {
            et.printStackTrace();
        }
    }

}
