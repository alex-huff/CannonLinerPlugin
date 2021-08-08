package phonis.cannonliner.listeners;

import com.sk89q.worldedit.Vector2D;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import phonis.cannonliner.CannonLiner;
import phonis.cannonliner.tasks.Tick;

public class ChunkUnloadEvent implements Listener {

    public ChunkUnloadEvent(CannonLiner cannonLiner) {
        cannonLiner.getServer().getPluginManager().registerEvents(this, cannonLiner);
    }

    @EventHandler
    public void onChunkUnload(org.bukkit.event.world.ChunkUnloadEvent event) {
        if (Tick.currentChunks == null) return;

        if (Tick.currentChunks.contains(new Vector2D(event.getChunk().getX(), event.getChunk().getZ()))) {
            event.setCancelled(true);
        }
    }

}
