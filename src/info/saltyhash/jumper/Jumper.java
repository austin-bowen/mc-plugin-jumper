package info.saltyhash.jumper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

/**
 * Bukkit plugin that allows players to teleport to line-of-sight destinations.
 */
public class Jumper extends JavaPlugin implements Listener {
    private final Set<Material> blockMaterialsToIgnore = new HashSet<>(Arrays.asList(
            Material.AIR,              Material.CARPET,           Material.DOUBLE_PLANT,
            Material.FIRE,             Material.LADDER,           Material.LONG_GRASS,
            Material.SAPLING,          Material.SUGAR_CANE_BLOCK, Material.SNOW,
            Material.STATIONARY_WATER, Material.TORCH,            Material.VINE,
            Material.WATER
    ));
    
    private int viewDistance;
    
    @Override
    public void onEnable() {
        this.viewDistance = (int)Math.round(
                Math.sqrt(3)*16*this.getServer().getViewDistance());
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("Enabled.");
    }
    
    @Override
    public void onDisable() {
        this.getLogger().info("Disabled.");
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        // Player must have permission
        if (!player.hasPermission("jumper.use")) return;
        
        // Checks are in Frequenty Failed First order
        
        // Event must be for HAND (not OFF_HAND)
        if (event.getHand() != EquipmentSlot.HAND) return;
        
        // Player must be jumping
        if (player.isOnGround()) return;
        
        // Player must be right-clicking air
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        
        // Player must not be riding anything
        if (player.isInsideVehicle()) return;
        
        // Player must not be flying
        if (player.isFlying()) return;
        
        // Determine the destination by finding the block at the crosshairs
        Location dest = null;
        BlockIterator bi = new BlockIterator(
                player.getEyeLocation(), 0, this.viewDistance);
        Block nextBlock, prevBlock = bi.next();
        while (bi.hasNext()) {
            nextBlock = bi.next();
            // Reached the selected block?
            //if (nextBlock.getType() != Material.AIR) {
            if (!this.blockMaterialsToIgnore.contains(nextBlock.getType())) {
                
                // Get the two blocks above the selected block
                Location l  = nextBlock.getLocation();
                Block    b0 = l.add(0.0, 1.0, 0.0).getBlock();
                Block    b1 = l.add(0.0, 1.0, 0.0).getBlock();
                
                // If two blocks above selected block are clear,
                // then teleport ON TOP of the selected block
                if (this.blockMaterialsToIgnore.contains(b0.getType())
                        && this.blockMaterialsToIgnore.contains(b1.getType()))
                    dest = nextBlock.getLocation().add(0.5, 1.1, 0.5);
                
                // Otherwise, teleport IN FRONT of the selected block
                else
                    dest = prevBlock.getLocation().add(0.5, 0.0, 0.5);
                
                break;
            }
            prevBlock = nextBlock;
        }
        if (dest == null) return;
        
        /* Teleport the player to the selected location */
        Location playerLocation = player.getLocation();
        // Play teleport sound at current location
        playerLocation.getWorld().playSound(
                playerLocation, Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
        // Change the player location coords to the destination coords
        playerLocation.setX(dest.getX());
        playerLocation.setY(dest.getY());
        playerLocation.setZ(dest.getZ());
        // Get the player's current velocity
        Vector playerVelocity = player.getVelocity();
        // Teleport the player to the new location
        player.teleport(playerLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        // Set the player's velocity to that before the teleport
        player.setVelocity(playerVelocity);
        // Play teleport sound at the new location
        playerLocation.getWorld().playSound(
                playerLocation, Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
        
        // This will prevent things like snowballs from being thrown
        event.setCancelled(true);
    }
}