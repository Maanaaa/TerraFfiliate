package fr.manaa.events.affiliationSystem;

import com.google.gson.*;
import fr.manaa.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;


import java.io.*;
import java.util.*;

public class NewPlayerAffiliated implements Listener{ private Main main;public NewPlayerAffiliated(Main main){this.main = main;}
    private static final String STATS_FILE_PATH = "stats.yml";
    private Map<String, String[]> affiliationStats;
    private Gson gson;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String serverAdress = p.getAddress().getAddress().getHostAddress();

        File players = new File(main.getDataFolder(), "players.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(players);

        File statsFile = new File(main.getDataFolder(), "stats.yml");
        YamlConfiguration stats = YamlConfiguration.loadConfiguration(statsFile);

        String[] parts = serverAdress.split("\\.");  // Utilisation de "\\." pour échapper le caractère "."

        String affiliator = parts[0];  // Récupération de la première partie de l'adresse

        System.out.println("Nom de l'affiliateur' : " + affiliator);



    }

}
