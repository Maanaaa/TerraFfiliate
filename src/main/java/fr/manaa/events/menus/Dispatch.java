package fr.manaa.events.menus;

import fr.manaa.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.*;
import java.util.*;

public class Dispatch implements Listener {
    private Main main;

    public Dispatch(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) {
            return;
        }
        String title = Objects.requireNonNull(main.getConfig().getString("menus.enableAffil.title")).replace("&", "§");
        Inventory inventory = e.getInventory();
        if(e.getView().getTitle().equals(main.getConfig().getString("menus.enableAffil.title").replace("&","§"))){
            e.setCancelled(true);
            if(clickedItem.getItemMeta().getDisplayName().equals(main.getConfig().getString("menus.enableAffil.items.3.display-name").replace("&","§"))) {
                p.updateInventory();
                p.closeInventory();
                // CLOUDFLARE API
                // SET TO TRUE PLAYER AFFILIATE
                File configFile = new File(main.getDataFolder(), "players.yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                String player = p.getDisplayName();

                config.set("affiliation-adress-list." + player + ".enable", true);
                try {
                    config.save(configFile);
                } catch (IOException et) {
                    et.printStackTrace();
                }

                p.updateInventory();

                for(String messages : main.getConfig().getStringList("affiliateEnabled.message")) {
                    p.sendMessage(messages.replace("&","§").replace("%playerip%", player.replace("_","").toLowerCase()));
                }
                if (main.getConfig().getBoolean("affiliateEnabled.playSound.enable")) {
                    Sound joinSound = Sound.valueOf(main.getConfig().getString("affiliateEnabled.playSound.sound"));
                    p.playSound(p.getLocation(), joinSound, 1.0f, 1.0f);
                }
                if (main.getConfig().getBoolean("affiliateEnabled.fireworks.enable")){
                    Location location = p.getLocation();
                    Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
                    FireworkMeta meta = firework.getFireworkMeta();

                    String color1 = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.color-1");
                    String color2 = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.color-2");
                    String color3 = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.color-3");

                    String type = main.getConfig().getString("affiliateEnabled.fireworks.fireworks.type");

                    FireworkEffect effect = FireworkEffect.builder().withColor(Color.AQUA, Color.WHITE, Color.ORANGE).with(Type.valueOf(type)).build();
                    meta.addEffect(effect);
                    meta.setPower(1);
                    firework.setFireworkMeta(meta);
                }

            }
        }


    }

}
