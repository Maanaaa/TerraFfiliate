package fr.manaa.affiliator.events.menus.affiliation;

import fr.manaa.*;
import fr.manaa.utils.loading.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.sql.*;
import java.util.*;

public class AffiliationMenuOpened implements Listener {
    private Main main;

    public AffiliationMenuOpened(Main main) {
        this.main = main;
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) {
            return;
        }
        String title = Objects.requireNonNull(main.getConfig().getString("menu.enableAffil.title")).replace("&", "§");
        Inventory inventory = e.getInventory();
        if(e.getView().getTitle().equals(Objects.requireNonNull(main.getConfig().getString("menu.enableAffil.title")).replace("&","§"))){
            e.setCancelled(true);
            if(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().equals(main.getConfig().getString("menu.enableAffil.items.3.display-name").replace("&","§"))) {
                p.updateInventory();
                p.closeInventory();

                String player = p.getDisplayName();

                for(String messages : main.getConfig().getStringList("affiliateEnabled.message")) {
                    p.sendMessage(messages.replace("&","§").replace("%playerip%", player.replace("_","").toLowerCase()));
                }
                if (main.getConfig().getBoolean("affiliateEnabled.playSound.enable")) {
                    Sound joinSound = Sound.valueOf(main.getConfig().getString("affiliateEnabled.playSound.sound"));
                    p.playSound(p.getLocation(), joinSound, 1.0f, 1.0f);
                }
                if (main.getConfig().getBoolean("affiliateEnabled.fireworks.enable")){
                    Location location = p.getLocation();
                    Firework firework = (Firework) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.FIREWORK);
                    FireworkMeta meta = firework.getFireworkMeta();
                    String color1 = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.color-1");
                    String color2 = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.color-2");
                    String color3 = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.color-3");

                    String type = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.type");
                    FireworkEffect effect = FireworkEffect.builder().withColor(Color.AQUA, Color.WHITE, Color.ORANGE).with(Type.valueOf(type)).build();
                    p.sendMessage();
                    Bukkit.getServer().broadcastMessage("\n \n    §e§l⚡ §b§lPLAY.TERRACRAFT.FR §e§l⚡ \n§7➤ §bLe joueur §7"+p.getDisplayName()+" §ba créé son adresse d'affiliation avec /affil et participe donc au concours mensuel !\n \n");
                    meta.addEffect(effect);
                    meta.setPower(1);
                    firework.setFireworkMeta(meta);
                }

            }
        }


    }





}