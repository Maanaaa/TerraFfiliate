package fr.manaa.events.affiliationSystem;

import fr.manaa.*;
import fr.manaa.utils.loading.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.io.*;

public class RegisterPlayerInFile implements Listener {

    private Main main;

    public RegisterPlayerInFile(Main main) {
        this.main = main;
    }
    DatabaseManager databaseManager = new DatabaseManager(main);

    @EventHandler
    public void onJoin(PlayerJoinEvent e){


        
        Player p = (Player) e.getPlayer();

        databaseManager.createAddress(p);

        // Get the configuration file
        File configFile = new File(main.getDataFolder(), "players.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        String player = p.getDisplayName();

        // Add player to file if player isn't in
        if(!(config.contains("affiliation-adress-list." + player))){
            config.set("affiliation-adress-list." + player + ".enable", false);
            // définir l'adresse dans la liste
            config.set("affiliation-adress-list."+p+".adress.",""+".terracraft.fr");
            transformAdress(p);
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

    public void transformAdress(Player p){

    }

}