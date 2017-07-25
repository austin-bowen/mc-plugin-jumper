# Jumper - Minecraft Server Plugin

Jumper is a Minecraft server plugin for CraftBukkit (and others) which allows players to "jump" to distant locations via teleportation, simply by jumping and right-clicking on the destination -- no commands required!

## Features

- Simply jump and right-click to teleport to wherever you are currently looking
- Purely action-based, no commands required to use
- Server admin can choose the range of allowed teleportation, and limit teleportation based on health and hunger

## Commands

- **/jumper**         - Display instructions on how to use Jumper
- **/jumper reload**  - Reload config file
- **/jumper version** - Display the plugin version and authors

## Installation and Upgrade

1. Download the latest Jumper-x.y.jar file.
1. Remove any old Jumper jar file from your server's "plugins" directory.
1. Move the new jar file into your server's "plugins" directory.
1. If your server is not running, start it up.  If it *is* running, restart the server **(/reload is not recommended)**.
1. You can now start using Jumper!

## Permissions

- jumper.reload:
  - description: Allow using the reload subcommand
  - default: op
- jumper.use:
  - description: Allow the player to teleport
  - default: true
- jumper.version:
  - description: Allow using the version subcommand
  - default: op

## Configuration

[View default config file](https://github.com/SaltyHash/mc-plugin-jumper/blob/master/config.yml) on GitHub.

To change Jumper's behavior:
1. Edit Jumper's config file located at "plugins/Jumper/config.yml", and save your changes.
1. Run the "/jumper reload" command in the server's console or as a player with OP permissions.
1. Jumper will now use the new settings.
