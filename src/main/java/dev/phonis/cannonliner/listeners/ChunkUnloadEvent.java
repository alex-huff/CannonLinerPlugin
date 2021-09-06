package dev.phonis.cannonliner.listeners;

import com.sk89q.worldedit.Vector2D;
import dev.phonis.cannonliner.tasks.Tick;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkUnloadEvent implements Listener {

    public ChunkUnloadEvent(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChunkUnload(org.bukkit.event.world.ChunkUnloadEvent event) {
        if (Tick.currentChunks == null) return;

        if (Tick.currentChunks.contains(new Vector2D(event.getChunk().getX(), event.getChunk().getZ()))) {
            event.setCancelled(true);
        }
    }

}
