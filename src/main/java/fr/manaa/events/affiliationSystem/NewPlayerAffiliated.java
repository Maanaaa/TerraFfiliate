package fr.manaa.events.affiliationSystem;

import com.google.gson.*;
import fr.manaa.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.*;

public class NewPlayerAffiliated implements Listener{ private Main main;public NewPlayerAffiliated(Main main){this.main = main;}
    private static final String STATS_FILE_PATH = "stats.json";
    private Map<String, String[]> affiliationStats;
    private Gson gson;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String serverAdress = p.getAddress().getAddress().getHostAddress();



    }

}
