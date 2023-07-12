package fr.manaa.utils.loading.placeholderapi;

import fr.manaa.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;

public class LeaderBoard extends PlaceholderExpansion {

    private final Main main;

    public LeaderBoard(Main main) {
        this.main = main;
    }

    @Override
    public String getIdentifier() {
        return "affiliation";
    }

    @Override
    public String getAuthor() {
        return "_Maana";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (params.equalsIgnoreCase("first")) {
            return getFirstPlayerInfo();
        } else if (params.equalsIgnoreCase("second")) {
            return getSecondPlayerInfo();
        } else if (params.equalsIgnoreCase("third")) {
            return getThirdPlayerInfo();
        } else if (params.equalsIgnoreCase("fourth")) {
            return getFourthPlayerInfo();
        } else if (params.equalsIgnoreCase("fifth")) {
            return getFifthPlayerInfo();
        } else if (params.equalsIgnoreCase("sixth")) {
            return getSixthPlayerInfo();
        } else if (params.equalsIgnoreCase("seventh")) {
            return getSeventhPlayerInfo();
        } else if (params.equalsIgnoreCase("eighth")) {
            return getEighthPlayerInfo();
        } else if (params.equalsIgnoreCase("ninth")) {
            return getNinthPlayerInfo();
        } else if (params.equalsIgnoreCase("tenth")) {
            return getTenthPlayerInfo();
        }
        return null;
    }

    private String getFirstPlayerInfo() {
        return getPlayerInfoByRank(1);
    }

    private String getSecondPlayerInfo() {
        return getPlayerInfoByRank(2);
    }

    private String getThirdPlayerInfo() {
        return getPlayerInfoByRank(3);
    }

    private String getFourthPlayerInfo() {
        return getPlayerInfoByRank(4);
    }

    private String getFifthPlayerInfo() {
        return getPlayerInfoByRank(5);
    }

    private String getSixthPlayerInfo() {
        return getPlayerInfoByRank(6);
    }

    private String getSeventhPlayerInfo() {
        return getPlayerInfoByRank(7);
    }

    private String getEighthPlayerInfo() {
        return getPlayerInfoByRank(8);
    }

    private String getNinthPlayerInfo() {
        return getPlayerInfoByRank(9);
    }

    private String getTenthPlayerInfo() {
        return getPlayerInfoByRank(10);
    }

    private String getPlayerInfoByRank(int rank) {
        try (Connection connection = getConnection()) {
            String query = "SELECT player, affiliated FROM affiliation WHERE affiliated = " +
                    "(SELECT affiliated FROM affiliation ORDER BY affiliated DESC LIMIT ?, 1)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, rank - 1); // Soustraire 1 pour correspondre à l'index 0-based
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String playerName = resultSet.getString("player");
                int affiliatedPlayers = resultSet.getInt("affiliated");
                String formattedRank = getPlayerFormattedRank(rank);
                return formattedRank + " " + ChatColor.GOLD + playerName + ChatColor.GRAY +
                        " (" + ChatColor.GREEN + affiliatedPlayers + ChatColor.GRAY + " affiliés)";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    private Connection getConnection() throws SQLException {
        String host = main.getConfig().getString("database.host");
        int port = main.getConfig().getInt("database.port");
        String database = main.getConfig().getString("database.database");
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        String username = main.getConfig().getString("database.user");
        String password = main.getConfig().getString("database.password");
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    private String getPlayerFormattedRank(int rank) {
        switch (rank) {
            case 1:
                return ChatColor.YELLOW + "⭐1er";
            case 2:
                return ChatColor.DARK_GREEN + "✩2e";
            case 3:
                return ChatColor.DARK_AQUA + "✩3e";
            default:
                return "§7" + rank + "e";
        }
    }
}
