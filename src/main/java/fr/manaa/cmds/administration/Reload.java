package fr.manaa.cmds.administration;

import fr.manaa.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class Reload implements CommandExecutor {
    private Main main;
    public Reload(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if(p.hasPermission("ultimatecore.commands.reload")) {
            main.reloadConfig();
            p.sendMessage(Objects.requireNonNull(this.main.getConfig().getString("globalmessages.reload")).replace("&", "§"));
        }else{p.sendMessage(Objects.requireNonNull(Objects.requireNonNull(main.getConfig().getString("globalmessages.noPerm")).replace("&","§")));}
        return false;
    }
}
