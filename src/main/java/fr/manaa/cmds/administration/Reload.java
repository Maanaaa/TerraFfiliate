package fr.manaa.cmds.administration;

import fr.manaa.*;
import org.bukkit.command.*;
import org.jdbi.v3.core.*;

public class Reload implements CommandExecutor {

    private Main main;
    public Reload(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        return false;
    }
}