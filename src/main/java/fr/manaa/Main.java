package fr.manaa;

import fr.manaa.utils.loading.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private final PluginInitializer pluginInitializer = new PluginInitializer(this);

    @Override
    public void onEnable() {
        pluginInitializer.initialize();

    }

    @Override
    public void onDisable() {
        // disable
    }
}
