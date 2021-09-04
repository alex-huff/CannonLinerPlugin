package phonis.cannonliner.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import phonis.cannonliner.tasks.Tick;
import phonis.cannonliner.trace.ChangeType;
import phonis.cannonliner.trace.EntityLocation;
import phonis.cannonliner.trace.LocationChange;

public class ExplosionEvent implements Listener {

    private final Tick tick;

    public ExplosionEvent(JavaPlugin plugin, Tick tick) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.tick = tick;
    }

    @EventHandler
    public void onExplosionEvent(ExplosionPrimeEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType().compareTo(EntityType.PRIMED_TNT) == 0) {
            Location loc = entity.getLocation();
            Location oldLoc = loc;
            Location prevTick = loc.clone().add(entity.getVelocity().multiply(-1.0204081434053665D));
            Location prevTick2 = loc.clone().add(entity.getVelocity().multiply(-1));

            if (this.tick.getLastTicks().contains(new EntityLocation(prevTick, entity.getType()))) {
                oldLoc = prevTick;
            } else if (this.tick.getLastTicks().contains(new EntityLocation(prevTick2, entity.getType()))) {
                oldLoc = prevTick2;
            }

            LocationChange change = new LocationChange(entity.getWorld(), oldLoc, loc, entity.getType(), ChangeType.END, entity.getVelocity().length());

            this.tick.addChange(change);
        }
    }

}
