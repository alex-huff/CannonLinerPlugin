package dev.phonis.cannonliner.listeners;

import dev.phonis.cannonliner.tasks.Tick;
import dev.phonis.cannonliner.trace.ChangeType;
import dev.phonis.cannonliner.trace.EntityLocation;
import dev.phonis.cannonliner.trace.LocationChange;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SandBlockEvent implements Listener {
    private final Tick tick;

    public SandBlockEvent(JavaPlugin plugin, Tick tick) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.tick = tick;
    }

    @EventHandler
    public void onEntityBlockFormEvent(EntityChangeBlockEvent event) {
        if (!event.getTo().equals(Material.AIR)) {
            Entity entity = event.getEntity();

            if (entity.getType().compareTo(EntityType.FALLING_BLOCK) == 0) {
                Location loc = entity.getLocation();
                EntityLocation el = this.tick.locations.get(entity.getEntityId());
                LocationChange change;

                if (el == null) {
                    change = new LocationChange(entity.getWorld(), loc, loc, entity.getType(), ChangeType.END, entity.getVelocity().length());
                } else {
                    change = new LocationChange(entity.getWorld(), el.getLocation(), loc, entity.getType(), ChangeType.END, entity.getVelocity().length());
                }

                this.tick.addChange(change);
            }
        }
    }

}
