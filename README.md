# TerraFfiliate

TerraFfiliate is an affiliation system plugin for the TerraCraft Minecraft server based on Spigot and BungeeCord. It allows players to create a unique affiliation address and participate in a monthly contest based on the number of affiliated players.

## Features

- Creation of unique affiliation addresses for players
  ![image](https://github.com/Maanaaa/TerraFfiliate/assets/123769327/0ed6aa1b-c5d4-4a5b-aad7-dfd0ee398ffb)
- Tracking the number of affiliated players for each player
  ![image](https://github.com/Maanaaa/TerraFfiliate/assets/123769327/3b44f904-adc3-4986-9814-9f254821877a)
  -----
  ![image](https://github.com/Maanaaa/TerraFfiliate/assets/123769327/e8d3d6dc-fb36-445a-920f-d08f85180e18)
- Ranking of players based on the number of affiliated players
- Monthly contest system based on the number of affiliated players
- PlaceHolderAPI integration
- Rewards for players with a high number of affiliated players
- Integration with Cloudflare for DNS record management

## Dependencies

- BungeeCord
- Bungee-TerraFfiliate
- Cloudflare (API)
- MySQL
- PlaceHolderAPI (optional)

## Installation

1. Download the TerraFfiliate.jar plugin.
2. Place the TerraFfiliate.jar file in the plugins folder of your BungeeCord server.
3. Configure the plugin by modifying the config.yml file.
4. Configure the Cloudflare API information in the config.yml file.
5. Restart your BungeeCord server.

## Commands

- `/affiliation` - Opens the affiliation menu for the player.
- `/affiliation reload` - Reloads the plugin configuration (requires permission).
- `/affiliation winner <player>` - Sets the winner of the contest (requires permission).
- `/affiliation view <player>` - Displays affiliation statistics for a player (requires permission).

## PlaceHolderAPI

__LeaderBoard__
- `%affiliation_first%` - Displays the first player in the leaderboard.
- `%affiliation_second%` - Displays the second player in the leaderboard.
- `%affiliation_third%` - Displays the third player in the leaderboard.
- `%affiliation_fourth%` - Displays the fourth player in the leaderboard.
- YOU CAN CONTINUE THIS PATTERN UP TO TEN

## Configuration

The plugin configuration can be found in the config.yml file.
