package fr.manaa.utils.loading.placeholderapi;

import me.clip.placeholderapi.expansion.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlaceHolders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "terraffiliate";
    }

    @Override
    public @NotNull String getAuthor() {
        return "_Maana";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("top")) {
            // Code pour récupérer le classement du joueur depuis votre base de données
            return "Le classement de " + player.getName() + " est 3";
        }
        return null;
    }
}
