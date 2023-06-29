package fr.manaa.affiliate.events;

import fr.manaa.*;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.net.*;
import java.util.*;

public class JoinWithAffiliateAdress implements Listener {
    private final Main main;
    public JoinWithAffiliateAdress(Main main) {
        this.main = main;
    }

    String serverDomain = "dev.terracraft.fr";
        //for(String messages : main.getConfig().getStringList("onJoinWithAffiliateAdress.messages.connectedSuccessfullyWith")) {
        //assert serverHostname != null;
        //event.getPlayer().sendMessage(messages.replace("&","§").replace("%affiliateAdress%", serverHostname).replace("&","§"));//replace("%affiliator%",affiliatorName)
    //}
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();

        String serverAddress = Objects.requireNonNull(event.getPlayer().getAddress()).getHostName();
        System.out.println("Server Address: " + serverAddress);

        p.sendMessage("§6Vous êtes connectés avec l'addresse §7"+serverAddress);

    }
}