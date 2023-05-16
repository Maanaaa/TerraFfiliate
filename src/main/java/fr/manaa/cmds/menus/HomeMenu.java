package fr.manaa.cmds.menus;

import fr.manaa.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.io.*;
import java.util.*;

public class HomeMenu implements CommandExecutor {
    private Main main;
    public HomeMenu(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        int size = main.getConfig().getInt("menus.enableAffil.size");
        String title = Objects.requireNonNull(main.getConfig().getString("menus.enabledAffil.title")).replace("&", "§");

        int sizeEnabled = main.getConfig().getInt("menus.enableAffil.size");
        String titleEnabled = Objects.requireNonNull(main.getConfig().getString("menus.enabledAffil.title")).replace("&", "§");

        if(sender instanceof Player){
            // Get the file players.yml
            File configFile = new File(main.getDataFolder(), "players.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            Player p = (Player) sender;
            String player = p.getDisplayName();
            boolean hasIp = config.getBoolean("affiliation-adress-list." + player + ".enable");
            if (hasIp == true) {
                Inventory enabledAffilMenu = Bukkit.createInventory(null, sizeEnabled, titleEnabled);

                registerItem(p, enabledAffilMenu, 1, "enabledAffil");
                registerItem(p, enabledAffilMenu, 2, "enabledAffil");
                registerItem(p, enabledAffilMenu, 3, "enabledAffil");
                registerItem(p, enabledAffilMenu, 4, "enabledAffil");
                registerItem(p, enabledAffilMenu, 5, "enabledAffil");

                p.openInventory(enabledAffilMenu);
            } else {
                // OPEN AFFILIATION MENU FIRST
                Inventory enableAffilMenu = Bukkit.createInventory(null, size, title);

                registerItem(p, enableAffilMenu, 1, "enableAffil");
                registerItem(p, enableAffilMenu, 2, "enableAffil");
                registerItem(p, enableAffilMenu, 3, "enableAffil");

                p.openInventory(enableAffilMenu);
            }
            }

        return false;
        }

        // ITEMS

    public void registerItem(Player player, Inventory menu, int itemNumber, String config){
        newItem(Objects.requireNonNull(player.getPlayer()),
                menu,
                main.getConfig().getString("menus."+config+".items."+itemNumber+".material"),
                Objects.requireNonNull(main.getConfig().getString("menus."+config+".items."+itemNumber+".display-name")).replace("&", "§"),
                main.getConfig().getStringList("menus."+config+".items."+itemNumber+".lore"),
                main.getConfig().getIntegerList("menus."+config+".items."+itemNumber+".slots"));
    }

    public void newItem(Player player, Inventory inventory, String material, String name, List<String> description, List<Integer> slots) {
        // SI ON VEUT DONNER LA TËTE DU JOUEUR
        if(material.equals("PLAYER_HEAD:%player%")){
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
            assert itemMeta != null;
            itemMeta.setOwningPlayer(player);
            itemMeta.setDisplayName(name);

            String playerip = player.getDisplayName().replace("_","").toLowerCase();
            for(int i = 0;i < description.size(); i++){
                description.set(i, description.get(i).replace("&","§").replace("%playerip%", playerip).replace("%player%",player.getDisplayName()));
            }

            itemMeta.setLore(description);
            item.setItemMeta(itemMeta);

            for(int slot : slots){
                inventory.setItem(slot, item);
            }
        }
        // SI ON NE VEUT PAS
        else{
            ItemStack item = new ItemStack(Material.valueOf(material));
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(name);

            String playerip = player.getDisplayName().replace("_","").toLowerCase();
            for(int i = 0;i < description.size(); i++){
                description.set(i, description.get(i).replace("&","§").replace("%playerip%", playerip).replace("%player%",player.getDisplayName()));
            }

            itemMeta.setLore(description);
            item.setItemMeta(itemMeta);

            for(int slot : slots){
                inventory.setItem(slot, item);
            }
        }


    }


    }


