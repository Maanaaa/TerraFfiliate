package fr.manaa.affiliate.events;

import fr.manaa.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.net.http.*;
import java.util.*;

public class JoinWithAffiliateAdress implements Listener {
    private final Main main;
    public JoinWithAffiliateAdress(Main main) {
        this.main = main;
    }

// onJoinWithAffiliateAdress.messages.connectedSuccessfullyWith
    @EventHandler
    public void onJoin(PlayerLoginEvent e){
        Player p = e.getPlayer();
        String connectionAdress = Objects.requireNonNull(p.getAddress()).getAddress().getHostAddress();
        //String affiliatorName = null;

        if(!(Objects.equals(connectionAdress, main.getConfig().getString("server.serverAdress")))){ // Si l'adresse n'est pas l'adresse du serveur
            for(String messages : main.getConfig().getStringList("onJoinWithAffiliateAdress.messages.connectedSuccessfullyWith")) {
                p.sendMessage(messages.replace("&","§").replace("%affiliateAdress%", connectionAdress).replace("&","§"));//replace("%affiliator%",affiliatorName)
            }
        }

    }

}
