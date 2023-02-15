package fr.manaa.cmds.menus;

import fr.manaa.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
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
        String title = Objects.requireNonNull(main.getConfig().getString("menus.enableAffil.title")).replace("&", "§");

        if(sender instanceof Player){
            // Get the file affil.yml
            File configFile = new File(main.getDataFolder(), "affil.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            Player p = (Player) sender;
            String player = p.getDisplayName();

            boolean hasIp = config.getBoolean("affiliation-adress-list." + player + ".enable");
            if (hasIp) {
                p.sendMessage("§6Adresse activé : §7"+player+".terracraft.fr");
            } else {
                // OPEN AFFILIATION MENU FIRST
                Inventory enableAffilMenu = Bukkit.createInventory(null, size, title);

                registerItem(p, enableAffilMenu, 1);
                registerItem(p, enableAffilMenu, 2);
                registerItem(p, enableAffilMenu, 3);

                p.openInventory(enableAffilMenu);

            }
            }

        return false;
        }

    public void registerItem(Player player, Inventory menu, int itemNumber){
        newItem(player.getPlayer(),
                menu,
                main.getConfig().getString("menus.enableAffil.items."+itemNumber+".material"),
                Objects.requireNonNull(main.getConfig().getString("menus.enableAffil.items."+itemNumber+".display-name")).replace("&", "§"),
                main.getConfig().getStringList("menus.enableAffil.items."+itemNumber+".lore"),
                main.getConfig().getIntegerList("menus.enableAffil.items."+itemNumber+".slots"));
    }

    public void newItem(Player player, Inventory inventory, String material, String name, List<String> description, List<Integer> slots) {
        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(name);
        for(int i = 0;i < description.size(); i++){
            description.set(i, description.get(i).replace("&","§").replace("%player%", player.getDisplayName()));
        }
        itemMeta.setLore(description);
        item.setItemMeta(itemMeta);

        for(int slot : slots){
            inventory.setItem(slot, item);
        }

    }


    }


