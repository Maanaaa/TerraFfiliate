package fr.manaa.events;

import fr.manaa.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.io.*;

public class RegisterInFile implements Listener {

    private Main main;

    public RegisterInFile(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = (Player) e.getPlayer();

        // Récupère le fichier de configuration
        File configFile = new File(main.getDataFolder(), "affil.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Ajoute un joueur avec un booléen "enable" correspondant à son pseudo
        String player = p.getName();
        config.set("affiliation-adress-list." + player + ".enable", false);

        // Sauvegarde les modifications du fichier de configuration
        try {
            config.save(configFile);
        } catch (IOException et) {
            et.printStackTrace();
        }
    }

}
