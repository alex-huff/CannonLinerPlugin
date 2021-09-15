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
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityChangeFormEvent implements Listener {
    private final Tick tick;

    public EntityChangeFormEvent(JavaPlugin plugin, Tick tick) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.tick = tick;
    }

    @EventHandler
    public void onExplosionEvent(ExplosionPrimeEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType().compareTo(EntityType.PRIMED_TNT) == 0) {
            EntityChangeFormEvent.trackLastTick(entity, this.tick);
        }
    }

    @EventHandler
    public void onEntityBlockFormEvent(EntityChangeBlockEvent event) {
        if (!event.getTo().equals(Material.AIR)) {
            Entity entity = event.getEntity();

            if (entity.getType().compareTo(EntityType.FALLING_BLOCK) == 0) {
                EntityChangeFormEvent.trackLastTick(entity, this.tick);
            }
        }
    }

    private static void trackLastTick(Entity entity, Tick tick) {
        Location loc = entity.getLocation();
        EntityLocation el = tick.locations.get(entity.getEntityId());
        LocationChange change;

        if (el == null) {
            change = new LocationChange(entity.getWorld(), loc, loc, entity.getType(), ChangeType.END, entity.getVelocity().length());
        } else {
            change = new LocationChange(entity.getWorld(), el.getLocation(), loc, entity.getType(), ChangeType.END, entity.getVelocity().length());
        }

        tick.addChange(change);
    }

}

