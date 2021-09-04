package phonis.cannonliner.listeners;

import com.sk89q.worldedit.Vector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import phonis.cannonliner.tasks.Tick;

public class BlockBreakEvent implements Listener {

    public BlockBreakEvent(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(EntityExplodeEvent blockBreakEvent) {
        if (Tick.currentCannon == null) return;

        blockBreakEvent.blockList().removeIf(block -> Tick.currentCannon.contains(new Vector(block.getX(), block.getY(), block.getZ())));
    }

}
