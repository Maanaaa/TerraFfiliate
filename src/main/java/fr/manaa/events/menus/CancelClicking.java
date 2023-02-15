package fr.manaa.events.menus;

import fr.manaa.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;

public class CancelClicking implements Listener {

    private Main main;
    public CancelClicking(Main main) {
        this.main = main;
    }

    public void onClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();

        if(e.getView().getTitle().equalsIgnoreCase(main.getConfig().getString("menus.enableAffil.title").replace("&","§"))){
            e.setCancelled(true);
        }


    }
}
